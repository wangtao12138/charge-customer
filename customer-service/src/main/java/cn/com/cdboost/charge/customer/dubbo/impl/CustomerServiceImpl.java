package cn.com.cdboost.charge.customer.dubbo.impl;

import cn.com.cdboost.charge.base.exception.BusinessException;
import cn.com.cdboost.charge.base.info.Afn19Object;
import cn.com.cdboost.charge.base.producer.RabbitmqProducer;
import cn.com.cdboost.charge.base.util.DateUtil;
import cn.com.cdboost.charge.base.util.MathUtil;
import cn.com.cdboost.charge.base.util.UuidUtil;
import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.config.SystemConfig;
import cn.com.cdboost.charge.customer.constant.CustomerConstant;
import cn.com.cdboost.charge.customer.dao.CustomerPayCardMapper;
import cn.com.cdboost.charge.customer.dao.CustomerPayMapper;
import cn.com.cdboost.charge.customer.dao.DeviceLogMapper;
import cn.com.cdboost.charge.customer.dao.DeviceUseDetailedMapper;
import cn.com.cdboost.charge.customer.dto.info.MessageListDto;
import cn.com.cdboost.charge.customer.dto.param.MessageListQueryDto;
import cn.com.cdboost.charge.customer.dubbo.CustomerService;
import cn.com.cdboost.charge.customer.dubbo.RefundService;
import cn.com.cdboost.charge.customer.model.*;
import cn.com.cdboost.charge.customer.service.*;
import cn.com.cdboost.charge.customer.vo.info.*;
import cn.com.cdboost.charge.customer.vo.param.*;
import cn.com.cdboost.charge.merchant.constant.DeviceConstant;
import cn.com.cdboost.charge.merchant.constant.MerchantConstant;
import cn.com.cdboost.charge.merchant.constant.SchemeConstant;
import cn.com.cdboost.charge.merchant.dubbo.*;
import cn.com.cdboost.charge.merchant.vo.info.*;
import cn.com.cdboost.charge.merchant.vo.param.CustomerChargingParam;
import cn.com.cdboost.charge.payment.dubbo.AlipayService;
import cn.com.cdboost.charge.payment.dubbo.WechatService;
import cn.com.cdboost.charge.payment.vo.info.RefundResultInfo;
import cn.com.cdboost.charge.payment.vo.param.TradeCreateParam;
import cn.com.cdboost.charge.payment.vo.param.WxUnifiedOrderParam;
import cn.com.cdboost.charge.settlement.constant.AccountDictEnum;
import cn.com.cdboost.charge.settlement.constant.AccountTypeEnum;
import cn.com.cdboost.charge.settlement.dubbo.AccountService;
import cn.com.cdboost.charge.settlement.dubbo.MonthAccountService;
import cn.com.cdboost.charge.settlement.vo.info.MonthPreRechargeInfo;
import cn.com.cdboost.charge.settlement.vo.info.PrePayInfo;
import cn.com.cdboost.charge.settlement.vo.info.UserAccountInfo;
import cn.com.cdboost.charge.settlement.vo.param.*;
import cn.com.cdboost.charge.user.dubbo.SmsService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 客户服务实现
 */
@Slf4j
@Service(version = "1.0",retries = -1,timeout = 5000)
public class CustomerServiceImpl implements CustomerService {

    // 支付宝回调地址
    private static final String ALIPAY_NOTIFY_URL = "/notify/zfbPayNotify";
    // 微信回调地址
    private static final String WECHAT_NOTIFY_URL = "/notify/wxPayNotify";

    @Reference(version = "1.0",url = "dubbo://10.10.1.226:20881")
    private DeviceService deviceService;
    @Reference(version = "1.0",url = "dubbo://10.10.1.226:20881")
    private ProjectService projectService;
    @Reference(version = "1.0",url = "dubbo://10.10.1.226:20881")
    private PaySchemeService paySchemeService;
    @Reference(version = "1.0")
    private SmsService smsService;
    @Reference(version = "1.0",url = "dubbo://10.10.1.226:20881")
    private IcCardService icCardService;
    @Reference(version = "1.0",url = "dubbo://10.10.1.226:20881")
    private MerchantToCustomerService merchantToCustomerService;
    @Reference(version = "1.0")
    private AccountService accountService;
    @Reference(version = "1.0")
    private MonthAccountService monthAccountService;
    @Reference(version = "1.0")
    private WechatService wechatService;
    @Reference(version = "1.0")
    private AlipayService alipayService;
    @Resource
    private SystemConfig systemConfig;
    @Resource
    private RabbitmqProducer rabbitmqProducer;
    @Resource
    private RefundService refundService;
    @Resource
    private ICustomerInfoService iCustomerInfoService;
    @Resource
    private ICustomerPayService iCustomerPayService;
    @Resource
    private ICustomerRefundService iCustomerRefundService;
    @Resource
    private IDeviceUseDetailedService iDeviceUseDetailedService;
    @Resource
    private IDeviceUsePayRelationService iDeviceUsePayRelationService;
    @Resource
    private IDeviceUseMonthRelationService iDeviceUseMonthRelationService;
    @Resource
    private IMerchantWxConfigService iMerchantWxConfigService;
    @Resource
    private IMerchantZfbConfigService iMerchantZfbConfigService;
    @Resource
    private IDeviceLogService iDeviceLogService;
    @Resource
    private ICustomerPayCardService iCustomerPayCardService;
    @Resource
    private DeviceLogMapper deviceLogMapper;
    @Resource
    private DeviceUseDetailedMapper deviceUseDetailedMapper;
    @Resource
    private CustomerPayCardMapper customerPayCardMapper;
    @Resource
    private CustomerPayMapper customerPayMapper;


    @Override
    public CustomerBaseInfo queryByParams(Integer appType, String openId) {
        CustomerInfo customerInfo;
        if (CustomerConstant.AppType.WECHAT.getType().equals(appType)) {
            customerInfo = iCustomerInfoService.queryByOpenId(openId);
        } else {
            customerInfo = iCustomerInfoService.queryByAlipayUserId(openId);
        }

        if (customerInfo != null) {
            CustomerBaseInfo baseInfo = new CustomerBaseInfo();
            BeanUtils.copyProperties(customerInfo,baseInfo);
            return baseInfo;
        }

        return null;
    }

    @Override
    public CustomerBaseInfo queryBaseInfo(CustomerQueryParam queryParam) {
        log.info("客户基本信查询CustomerQueryParam={}", JSON.toJSONString(queryParam));
        String customerGuid = queryParam.getCustomerGuid();
        String customerContact = queryParam.getCustomerContact();
        String webcharNo = queryParam.getWebcharNo();
        String alipayUserId = queryParam.getAlipayUserId();
        if (StringUtils.isBlank(customerGuid)
                && StringUtils.isBlank(customerContact)
                && StringUtils.isBlank(webcharNo)
                && StringUtils.isBlank(alipayUserId)) {
            throw new BusinessException("对象查询字段不能全为空");
        }

        CustomerInfo param = new CustomerInfo();
        BeanUtils.copyProperties(queryParam,param);
        CustomerInfo customerInfo = iCustomerInfoService.queryCustomerInfo(param);
        CustomerBaseInfo baseInfo = new CustomerBaseInfo();
        BeanUtils.copyProperties(customerInfo,baseInfo);
        return baseInfo;
    }

    @Override
    public List<PayOrderInfo> queryPayOrderInfos(PayOrderQueryParam queryParam) {
        log.info("客户充值订单查询PayOrderQueryParam={}",JSON.toJSONString(queryParam));
        List<CustomerPay> customerPays = iCustomerPayService.queryPayOrders(queryParam);
        List<PayOrderInfo> dataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(customerPays)) {
            return dataList;
        }

        for (CustomerPay customerPay : customerPays) {
            PayOrderInfo orderInfo = new PayOrderInfo();
            BeanUtils.copyProperties(customerPay,orderInfo);
            dataList.add(orderInfo);
        }
        return dataList;
    }

    @Override
    public List<ChargeOrderInfo> queryChargeOrderInfos(ChargeOrderQueryParam queryParam) {
        log.info("客户充电订单查询ChargeOrderQueryParam={}",JSON.toJSONString(queryParam));
        List<DeviceUseDetailed> useDetaileds = iDeviceUseDetailedService.queryChargeOrders(queryParam);
        List<ChargeOrderInfo> dataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(useDetaileds)) {
            return dataList;
        }

        for (DeviceUseDetailed useDetailed : useDetaileds) {
            ChargeOrderInfo orderInfo = new ChargeOrderInfo();
            BeanUtils.copyProperties(useDetailed,orderInfo);
            dataList.add(orderInfo);
        }
        return dataList;
    }

    @Override
    public ChargeOrderInfo queryChargingOrderByCustomerGuid(String customerGuid) {
        log.info("客户充电中记录查询customerGuid={}",customerGuid);
        DeviceUseDetailed useDetailed = iDeviceUseDetailedService.queryChargingOrder(customerGuid);
        if (useDetailed == null) {
            return null;
        }
        ChargeOrderInfo info = new ChargeOrderInfo();
        BeanUtils.copyProperties(useDetailed,info);
        return info;
    }

    @Override
    public ChargeOrderInfo queryChargingOrderByChargingGuid(String chargingGuid) {
        log.info("客户充电中记录查询chargingGuid={}",chargingGuid);
        DeviceUseDetailed useDetailed = iDeviceUseDetailedService.queryChargeOrder(chargingGuid);
        ChargeOrderInfo orderInfo = new ChargeOrderInfo();
        BeanUtils.copyProperties(useDetailed,orderInfo);
        return orderInfo;
    }

    @Override
    public ChargeRealDataInfo queryChargeRealDataInfo(String chargingGuid) {
        log.info("客户充电中实时数据查询chargingGuid={}",chargingGuid);
        DeviceUseDetailed useDetailed = iDeviceUseDetailedService.queryChargeOrder(chargingGuid);
        ChargeRealDataInfo dataInfo = new ChargeRealDataInfo();
        BeanUtils.copyProperties(useDetailed,dataInfo);

        // 查询设备充电日志表
        Integer devLogId = useDetailed.getDevLogId();
        if (devLogId != null) {
            DeviceLog deviceLog = iDeviceLogService.selectByPrimaryKey(devLogId);
            dataInfo.setChargingPercent(deviceLog.getChargingPercent());
            dataInfo.setPower(deviceLog.getPower());
        } else {
            dataInfo.setChargingPercent(MathUtil.setPrecision(BigDecimal.ZERO));
            dataInfo.setPower(BigDecimal.ZERO);
        }

        return dataInfo;
    }

    @Override
    public WechatChargeInfo wechatCharge(WechatChargeVo chargeVo) {
        log.info("客户微信开电接口WechatChargeVo={}",JSON.toJSONString(chargeVo));
        // 查询开电设备，方案信息
        CustomerChargingParam param = new CustomerChargingParam();
        param.setDeviceNo(chargeVo.getDeviceNo());
        param.setSchemeGuid(chargeVo.getSchemeGuid());
        ChargingSchemeDeviceVo schemeDeviceVo = merchantToCustomerService.queryChargeDeviceSchemeInfo(param);
        Integer runState = schemeDeviceVo.getRunState();
        if (DeviceConstant.DeviceRunState.CHARGING.getState().equals(runState)) {
            throw new BusinessException("设备处于开电中");
        }

        // 普通商户模式还是服务商模式
        Integer serviceMode = schemeDeviceVo.getServiceMode();
        WechatChargeInfo chargeInfo;
        if (MerchantConstant.ServiceMode.COMMON_MERCHANT.getMode().equals(serviceMode)) {
            // 普通商户模式
            chargeInfo = this.charge4CommonMerchant(chargeVo, schemeDeviceVo);
        } else {
            // 服务商模式
            chargeInfo = this.charge4ServiceMerchant(chargeVo,schemeDeviceVo);
        }
        return chargeInfo;
    }

    @Override
    public WechatChargeInfo wechatReCharge(WechatRechargeVo rechargeVo) {
        log.info("客户微信续开电接口WechatRechargeVo={}",JSON.toJSONString(rechargeVo));
        // 查询
        String chargingGuid = rechargeVo.getChargingGuid();
        DeviceUseDetailed useDetailed = iDeviceUseDetailedService.queryChargeOrder(chargingGuid);
        Integer state = useDetailed.getState();
        if (!CustomerConstant.DeviceUsedRecordState.CHARGING.getState().equals(state)) {
            throw new BusinessException("非充电中状态，不允许发起续充电");
        }

        // 查询开电设备，方案信息
        CustomerChargingParam param = new CustomerChargingParam();
        param.setChargingPlieGuid(useDetailed.getChargingPlieGuid());
        param.setSchemeGuid(useDetailed.getSchemeGuid());
        ChargingSchemeDeviceVo schemeDeviceVo = merchantToCustomerService.queryChargeDeviceSchemeInfo(param);
        Integer serviceMode = schemeDeviceVo.getServiceMode();
        WechatChargeInfo chargeInfo;
        if (MerchantConstant.ServiceMode.COMMON_MERCHANT.getMode().equals(serviceMode)) {
            // 普通商户模式
            chargeInfo = this.recharge4CommonMerchant(rechargeVo, schemeDeviceVo);
        } else {
            // 服务商模式
            chargeInfo = this.recharge4ServiceMerchant(rechargeVo,schemeDeviceVo);
        }
        return chargeInfo;
    }

    @Override
    public void wxPayNotify(Map<String, String> paramMap) {
        log.info("微信支付回调通知参数paramMap={}",JSON.toJSONString(paramMap));

        // 校验签名
        // 商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
        boolean checkSign = wechatService.checkSign(paramMap);
        if (!checkSign) {
           return;
        }

        String outTradeNo = paramMap.get("out_trade_no");
        CustomerPay customerPay = iCustomerPayService.queryByPayFlagForUpdate(outTradeNo);
        if (customerPay == null) {
            // 非我方订单号，忽略
            return;
        }

        BigDecimal payMoney = customerPay.getPayMoney();
        // 微信支付金额单位时分
        String totalFee = paramMap.get("total_fee");
        String temp = MathUtil.fen2yuan(totalFee);
        boolean flag = payMoney.equals(new BigDecimal(temp));
        if (!flag) {
            // 与我方订单实际支付金额不等，忽略
            return;
        }

        // TODO 查询微信appid是否有返回
//        if (!appId.equals("")) {
//            // 与我方appId不等，忽略
//            return;
//        }

        Integer payState = customerPay.getPayState();
        if (CustomerConstant.PayState.PAY_SUCCESS.getState().equals(payState)) {
            // 重复通知，忽略
            return;
        }

        this.payNotifyCommonOperate(customerPay);
    }

    @Override
    public void refundResultNotify(Map<String, String> paramMap) {
        log.info("微信退款结果通知paramMap={}",JSON.toJSONString(paramMap));
        // 解密
        String req_info = paramMap.get("req_info");
        RefundResultInfo resultInfo = wechatService.decrypt(req_info);
        log.info("解密后RefundResultInfo={}",JSON.toJSONString(resultInfo));

        String out_refund_no = resultInfo.getOut_refund_no();

        // 在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱
        CustomerRefund customerRefund = iCustomerRefundService.queryByRefundNoForUpdate(out_refund_no);
        if (customerRefund == null) {
            // 非系统退款申请单，直接返回
            return;
        }

        Integer refundState = customerRefund.getRefundState();
        if (!CustomerConstant.RefundState.PROCESSING.getState().equals(refundState)) {
            // 退款申请已处理，直接返回
            return;
        }

        String refund_status = resultInfo.getRefund_status();
        if ("SUCCESS".equals(refund_status)) {
            // 退款成功
            customerRefund.setRefundState(CustomerConstant.RefundState.SUCCESS.getState());
            customerRefund.setUpdateTime(new Date());
            iCustomerRefundService.updateByPrimaryKeySelective(customerRefund);
        } else if ("CHANGE".equals(refund_status)) {
            // 退款异常
            customerRefund.setRefundState(CustomerConstant.RefundState.FAIL.getState());
            customerRefund.setUpdateTime(new Date());
            customerRefund.setErrorDesc("退款异常");
            iCustomerRefundService.updateByPrimaryKeySelective(customerRefund);
        } else if ("REFUNDCLOSE".equals(refund_status)) {
            // 退款关闭
            customerRefund.setRefundState(CustomerConstant.RefundState.FAIL.getState());
            customerRefund.setUpdateTime(new Date());
            customerRefund.setErrorDesc("退款关闭");
            iCustomerRefundService.updateByPrimaryKeySelective(customerRefund);
        }
    }

    private void payNotifyCommonOperate(CustomerPay customerPay) {
        String schemeGuid = customerPay.getSchemeGuid();
        PaySchemeVo schemeVo = paySchemeService.queryPaySchemeBySchemeGuid(schemeGuid);
        Integer serviceMode = schemeVo.getServiceMode();
        Integer businessType = customerPay.getBusinessType();
        String customerGuid = customerPay.getCustomerGuid();
        Integer payWay = customerPay.getPayWay();
        Integer openMeans = CustomerConstant.OpenMeansConstant.WECHAT.getType();
        if (CustomerConstant.PayWayConstant.ALIPAY.getType().equals(payWay)) {
            openMeans = CustomerConstant.OpenMeansConstant.ALIPAY.getType();
        }
        if (MerchantConstant.ServiceMode.COMMON_MERCHANT.getMode().equals(serviceMode)) {
            // 普通商户模式
            if (CustomerConstant.PayOrderBusinessType.CHARGE_PAY.getType().equals(businessType)) {
                // 查询本次开电设备信息
                DeviceUsePayRelation relation = iDeviceUsePayRelationService.queryByPayFlag(customerPay.getPayFlag());
                String chargingGuid = relation.getChargingGuid();

                // 冻结账户扣减
                ConfirmChargeElectricVo confirmVo = new ConfirmChargeElectricVo();
                confirmVo.setUserGuid(customerGuid);
                confirmVo.setDataGuid(chargingGuid);
                confirmVo.setSignGuid(relation.getSignGuid());
                confirmVo.setThirdPayMoney(customerPay.getPayMoney());
                confirmVo.setThirdPayType(customerPay.getPayWay() );
                confirmVo.setBalancePayMoney(customerPay.getAccountDeductMoney());
                accountService.confirmPay(confirmVo);

                // 更新订单状态
                List<CustomerPay> customerPays = Lists.newArrayList();
                List<DeviceUsePayRelation> usePayRelations = Lists.newArrayList();
                BigDecimal accountDeductMoney = customerPay.getAccountDeductMoney();
                if (MathUtil.isGreateThanZero(accountDeductMoney)) {
                    usePayRelations = iDeviceUsePayRelationService.queryBySignGuid(relation.getSignGuid());
                    List<String> payFlags = Lists.newArrayList();
                    for (DeviceUsePayRelation usePayRelation : usePayRelations) {
                        payFlags.add(usePayRelation.getPayFlag());
                    }
                    customerPays = iCustomerPayService.batchQuery(payFlags);
                    Date date = new Date();
                    for (CustomerPay pay : customerPays) {
                        pay.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
                        pay.setUpdateTime(date);
                    }
                    customerPayMapper.batchUpdate(customerPays);
                } else {
                    customerPay.setUpdateTime(new Date());
                    customerPay.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
                    iCustomerPayService.updateByPrimaryKeySelective(customerPay);
                    customerPays.add(customerPay);
                    usePayRelations.add(relation);
                }

                // 第一次开电，逻辑
                this.commonProcess(customerPays,usePayRelations,schemeVo,openMeans,false);
            } else if (CustomerConstant.PayOrderBusinessType.RE_CHARGE_PAY.getType().equals(businessType)) {
                // 更新订单状态
                customerPay.setUpdateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
//                customerPay.setAfterRemainAmount(remainAmount);
                iCustomerPayService.updateByPrimaryKeySelective(customerPay);

                // 查询本次开电设备信息
                DeviceUsePayRelation relation = iDeviceUsePayRelationService.queryByPayFlag(customerPay.getPayFlag());
                String chargingGuid = relation.getChargingGuid();

                // 冻结账户扣减
                ConfirmChargeElectricVo confirmVo = new ConfirmChargeElectricVo();
                confirmVo.setUserGuid(customerGuid);
                confirmVo.setDataGuid(chargingGuid);
                confirmVo.setSignGuid(relation.getSignGuid());
                confirmVo.setThirdPayMoney(customerPay.getPayMoney());
                confirmVo.setThirdPayType(customerPay.getPayWay());
                accountService.confirmPay(confirmVo);

                // 续开电逻辑
//                this.commonProcess(customerPays,usePayRelations,schemeVo,true);
            } else if (CustomerConstant.PayOrderBusinessType.ACTIVITY_PAY.getType().equals(businessType)) {
                // 活动充值
                // 更新订单状态
                customerPay.setUpdateTime(new Date());
                customerPay.setPayState(1);
//                customerPay.setAfterRemainAmount(remainAmount);
                iCustomerPayService.updateByPrimaryKeySelective(customerPay);

                // 调用账户服务充值
                PaymentVo paymentVo = new PaymentVo();
                paymentVo.setUserGuid(customerPay.getCustomerGuid());
                paymentVo.setAccountType(AccountTypeEnum.CUSTOMER.getType());
                paymentVo.setPayMoney(customerPay.getPayMoney());
                paymentVo.setAccountRechargeMoney(customerPay.getAccountChargeMoney());
                paymentVo.setDataGuid(customerPay.getPayFlag());
                paymentVo.setSignGuid(UuidUtil.getUuid());
                accountService.customerRecharge(paymentVo);

                // 更新IC卡下发标志
                AccountQueryVo queryVo = new AccountQueryVo();
                queryVo.setUserGuid(customerGuid);
                queryVo.setAccountCode(AccountDictEnum.CUSTOMER_AVAILABLE.getAccountCode());
                UserAccountInfo accountInfo = accountService.queryAccountInfo(queryVo);
                BigDecimal remainAmount = accountInfo.getRemainAmount();
                BigDecimal afterRemainAmount = remainAmount.add(customerPay.getPayMoney());

                this.updateIccardFlag(remainAmount,afterRemainAmount,customerGuid);
            } else if (CustomerConstant.PayOrderBusinessType.MONTH_CARD_PAY.getType().equals(businessType)) {
                // 月卡充值
                this.monthCardBuyProcess(customerPay,schemeGuid);
            } else if (CustomerConstant.PayOrderBusinessType.IC_CARD_PAY.getType().equals(businessType)) {
                // IC卡充值
                // 更新订单状态
                customerPay.setUpdateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
//                customerPay.setAfterRemainAmount(totalAmount);
                iCustomerPayService.updateByPrimaryKeySelective(customerPay);
            }
        } else {
            // 服务商模式
            if (CustomerConstant.PayOrderBusinessType.CHARGE_PAY.getType().equals(businessType)) {
                // 更新订单状态
                customerPay.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
                customerPay.setUpdateTime(new Date());
                iCustomerPayService.updateByPrimaryKeySelective(customerPay);

                // 查询本次开电设备信息
                DeviceUsePayRelation relation = iDeviceUsePayRelationService.queryByPayFlag(customerPay.getPayFlag());
                String chargingGuid = relation.getChargingGuid();

                // 第一次开电，逻辑
//                this.commonProcess(customerPay,schemeVo,relation,false);
            } else if (CustomerConstant.PayOrderBusinessType.RE_CHARGE_PAY.getType().equals(businessType)) {
                // 更新订单状态
                customerPay.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
                customerPay.setUpdateTime(new Date());
                iCustomerPayService.updateByPrimaryKeySelective(customerPay);

                // 查询本次开电设备信息
                DeviceUsePayRelation relation = iDeviceUsePayRelationService.queryByPayFlag(customerPay.getPayFlag());
                String chargingGuid = relation.getChargingGuid();

                // 续开电逻辑
//                this.commonProcess(customerPay,schemeVo,relation,true);
            } else if (CustomerConstant.PayOrderBusinessType.MONTH_CARD_PAY.getType().equals(businessType)) {
                // 月卡充值
                this.monthCardBuyProcess(customerPay,schemeGuid);
            } else if (CustomerConstant.PayOrderBusinessType.IC_CARD_PAY.getType().equals(businessType)) {
                // IC卡充值
                // 更新订单状态
                customerPay.setUpdateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
//                customerPay.setAfterRemainAmount(totalAmount);
                iCustomerPayService.updateByPrimaryKeySelective(customerPay);
            }
        }
    }


    private void monthCardBuyProcess(CustomerPay customerPay,String schemeGuid) {
        //调用账户月卡购买确认接口
        MonthConfirmRechargeVo confirmVo = new MonthConfirmRechargeVo();
        confirmVo.setUserGuid(customerPay.getCustomerGuid());
        confirmVo.setDataGuid(customerPay.getPayFlag());
        confirmVo.setSignGuid(UuidUtil.getUuid());
        confirmVo.setThirdPayMoney(customerPay.getPayMoney());
        confirmVo.setBalancePayMoney(customerPay.getAccountDeductMoney());
        confirmVo.setSchemeGuid(schemeGuid);
        confirmVo.setChargeCnt(customerPay.getBuyCnt());
        confirmVo.setExpireTime(customerPay.getExpireTime());
        monthAccountService.confirmRecharge(confirmVo);
    }

    /**
     * 更新IC卡下发标志
     * @param beforeAmount
     * @param afterAmount
     * @param customerGuid
     */
    private void updateIccardFlag(BigDecimal beforeAmount,BigDecimal afterAmount,String customerGuid) {
        boolean lessThanZero = MathUtil.isLessThanZero(beforeAmount);
        boolean greateThanZero = MathUtil.isGreateThanZero(afterAmount);
        if (lessThanZero && greateThanZero) {
            log.info("开始更新IC卡下发标识beforeAmount=" + beforeAmount + ",afterAmount=" + afterAmount);
//            // 查询账户绑定的IC卡列表
//            List<ChargingCard> chargingCards = chargingCardService.queryAllByCustomerGuid(customerGuid);
//            if (!CollectionUtils.isEmpty(chargingCards)) {
//                Set<String> cardIdSet = Sets.newHashSet();
//                Set<Integer> idSet = Sets.newHashSet();
//                for (ChargingCard chargingCard : chargingCards) {
//                    cardIdSet.add(chargingCard.getCardId());
//                    idSet.add(chargingCard.getId());
//                }
//
//                // 批量更新IC卡list表state为正常，下发标志为未下发
//                chargingCardListService.updateSendFlag(cardIdSet);
//
//                // 批量更新IC卡状态为正常
//                chargingCardService.batchUpdateCardOwe(idSet);
//            }
        }
    }

    private void commonProcess(List<CustomerPay> customerPays,List<DeviceUsePayRelation> payRelations,PaySchemeVo schemeVo,Integer openMeans,boolean isRecharge) {
        // 查询使用记录
        DeviceUsePayRelation relation = payRelations.get(0);
        String chargingGuid = relation.getChargingGuid();
        DeviceUseDetailed useDetailed = iDeviceUseDetailedService.queryChargeOrder(chargingGuid);
        Date createTime = useDetailed.getCreateTime();
        if (DateUtil.getSecond() - DateUtil.toSecond(createTime) > 60) {
            log.info("开电已经超过60秒");
            return;
        }

        // 钱够，就直接下发充电指令
        String workMode = String.valueOf(CustomerConstant.ChargingWay.CHARGING_TIME.getWay());
        Integer measure;
        if (isRecharge) {
            measure = CustomerConstant.MeasureType.APPEND_MEASURE.getType();
        } else {
            measure = CustomerConstant.MeasureType.RE_MEASURE.getType();
        }

        String commNo = useDetailed.getCommNo();
        String port = useDetailed.getPort();
        Integer chargingTime = schemeVo.getChargingTime();
        CustomerPay customerPay = customerPays.get(0);
        Integer maxPower = schemeVo.getMaxPower();
        Integer id = schemeVo.getId();
        String openId = customerPay.getWebcharNo();
        log.info("准备发送开电指令chargingGuid={}",chargingGuid);
        // 发送通电指令
        String queueGuid = UuidUtil.getUuid();
        Afn19Object afn19Object = new Afn19Object(queueGuid,
                "19",
                "999999999",
                "42475858fffffa",
                commNo,"20000",
                port,
                "on",
                "",
                openId,
                String.valueOf(id),
                String.valueOf(chargingTime),
                workMode,
                openMeans,
                measure,
                String.valueOf(maxPower));
        Integer result = rabbitmqProducer.sendAfn19(afn19Object);
        if (true) {
//        if (result != 1) {
            // 更新开电失败
            this.chargeFail(useDetailed,customerPays,payRelations);
            throw new BusinessException("开电指令投递队列异常");
        }
    }

    private void cancelPay(List<CustomerPay> customerPays,List<DeviceUsePayRelation> payRelations,DeviceUseDetailed useDetailed) {
        // 分组
        ImmutableMap<String, DeviceUsePayRelation> relationMap = Maps.uniqueIndex(payRelations, new Function<DeviceUsePayRelation, String>() {
            @Nullable
            @Override
            public String apply(@Nullable DeviceUsePayRelation payRelation) {
                return payRelation.getPayFlag();
            }
        });

        BigDecimal refundTotal = BigDecimal.ZERO;
        for (CustomerPay customerPay : customerPays) {
            String payFlag = customerPay.getPayFlag();
            DeviceUsePayRelation payRelation = relationMap.get(payFlag);
            String chargingGuid = payRelation.getChargingGuid();
            String signGuid = payRelation.getSignGuid();
            Integer payWay = customerPay.getPayWay();
            if (CustomerConstant.PayWayConstant.WECHAT.getType().equals(payWay)) {
                // 微信退款
                RefundParam param = new RefundParam();
                param.setMerchantGuid(customerPay.getMerchantGuid());
                param.setCustomerGuid(customerPay.getCustomerGuid());
                param.setChargingGuid(chargingGuid);
                param.setSignGuid(signGuid);
                param.setTradeNo(customerPay.getPayFlag());
                param.setTotalMoney(customerPay.getPayMoney());
                param.setRefundMoney(customerPay.getPayMoney());
                param.setRefundCategory(0);
                param.setRemark("开电失败资金原路退回");
                Integer result = refundService.wxRefund(param);
                if (result == 1) {
                    // TODO 这块需要放到微信支付通知回调里
                    customerPay.setRefundState(CustomerConstant.PayOrderRefundState.FULL_REFUND.getState());
                    customerPay.setUpdateTime(new Date());
                    refundTotal = refundTotal.add(customerPay.getPayMoney());
                }
            } else if (CustomerConstant.PayWayConstant.ALIPAY.getType().equals(payWay)) {
                // 支付宝退款
                RefundParam param = new RefundParam();
                param.setMerchantGuid(customerPay.getMerchantGuid());
                param.setCustomerGuid(customerPay.getCustomerGuid());
                param.setChargingGuid(chargingGuid);
                param.setSignGuid(signGuid);
                param.setTradeNo(customerPay.getPayFlag());
                param.setTotalMoney(customerPay.getPayMoney());
                param.setRefundMoney(customerPay.getPayMoney());
                param.setRefundCategory(0);
                param.setRemark("开电失败资金原路退回");
                Integer result = refundService.zfbRefund(param);
                if (result == 1) {
                    customerPay.setRefundState(CustomerConstant.PayOrderRefundState.FULL_REFUND.getState());
                    customerPay.setUpdateTime(new Date());
                    refundTotal = refundTotal.add(customerPay.getPayMoney());
                }
            } else if (CustomerConstant.PayWayConstant.BALANCE.getType().equals(payWay)) {
                // 余额退款
                log.info("余额退款开始");
                ConfirmRefundVo refundVo = new ConfirmRefundVo();
                refundVo.setUserGuid(customerPay.getCustomerGuid());
                refundVo.setDataGuid(chargingGuid);
                refundVo.setSignGuid(signGuid);
                accountService.refundBalance(refundVo);

                // 新增退款申请记录
                CustomerRefund refund = new CustomerRefund();
                refund.setCustomerGuid(customerPay.getCustomerGuid());
                refund.setRefundType(CustomerConstant.RefundType.ALIPAY.getType());
                refund.setChargingGuid(chargingGuid);
                refund.setTradeNo(payFlag);
                String refundNo = UuidUtil.getUuid();
                refund.setRefundNo(refundNo);
                refund.setRefundAmount(customerPay.getPayMoney());
                refund.setRefundCategory(0);
                refund.setRefundState(CustomerConstant.RefundState.SUCCESS.getState());
                refund.setRemark("开电失败资金原路退回");
                refund.setCreateTime(new Date());
                refund.setOperateUserId(0);
                iCustomerRefundService.insertSelective(refund);

                customerPay.setRefundState(CustomerConstant.PayOrderRefundState.FULL_REFUND.getState());
                customerPay.setUpdateTime(new Date());
                refundTotal = refundTotal.add(customerPay.getPayMoney());
            }
        }

        // 更新订单退款状态
        customerPayMapper.batchUpdate(customerPays);

        // 更新开电失败
        useDetailed.setState(CustomerConstant.DeviceUsedRecordState.FAIL.getState());
        useDetailed.setRefundMoney(refundTotal);
        useDetailed.setRefundState(CustomerConstant.ChargeOrderRefundState.FULL_REFUND.getState());
        iDeviceUseDetailedService.updateByPrimaryKeySelective(useDetailed);
    }


    /**
     * 普通商户模式，续充电
     * @param rechargeVo
     * @param schemeDeviceVo
     * @return
     */
    private WechatChargeInfo recharge4CommonMerchant(WechatRechargeVo rechargeVo,ChargingSchemeDeviceVo schemeDeviceVo) {
        String customerGuid = rechargeVo.getCustomerGuid();
        String openId = rechargeVo.getOpenId();

        Integer chargingTime = schemeDeviceVo.getChargingTime();
        Integer maxPower = schemeDeviceVo.getMaxPower();

        WechatChargeInfo chargeInfo = new WechatChargeInfo();
        chargeInfo.setIsCharge(1);
        Integer openMeans = CustomerConstant.OpenMeansConstant.WECHAT.getType();
        String commNo = schemeDeviceVo.getCommNo();
        String port = schemeDeviceVo.getPort();
        Integer id = schemeDeviceVo.getId();
        Integer measure = CustomerConstant.MeasureType.APPEND_MEASURE.getType();
        String workMode = String.valueOf(CustomerConstant.ChargingWay.CHARGING_TIME.getWay());

        String chargingGuid = rechargeVo.getChargingGuid();
        Integer payCategory = schemeDeviceVo.getPayCategory();
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory) ||
                SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {

            //获取用户余额，和传入价格方案的金额进行对比
            PrePayVo prePayVo = new PrePayVo();
            prePayVo.setPayMoney(schemeDeviceVo.getRealMoney());
            prePayVo.setDataGuid(chargingGuid);
            String signGuid = UuidUtil.getUuid();
            prePayVo.setSignGuid(signGuid);
            prePayVo.setUserGuid(customerGuid);
            prePayVo.setAccountType(2);
            prePayVo.setOperateType(1);
            PrePayInfo prePayInfo = accountService.prePay(prePayVo);
            BigDecimal thirdPayMoney = prePayInfo.getThirdPayMoney();
            boolean greateThanZero = MathUtil.isGreateThanZero(thirdPayMoney);
            if (greateThanZero) {
                //获取差价
                chargeInfo.setIsCharge(0);
                chargeInfo.setPay(MathUtil.setPrecision(thirdPayMoney));

                // 统一下单
                WxUnifiedOrderParam orderParam = new WxUnifiedOrderParam();
                orderParam.setBody("充电桩续充电");
                String outTradeNo = UuidUtil.getUuid();
                orderParam.setOutTradeNo(outTradeNo);
                Integer totalFee = MathUtil.yuan2Fen(String.valueOf(thirdPayMoney));
                orderParam.setTotalFee(totalFee);
                orderParam.setSpbillCreateIp(rechargeVo.getIp());
                String notifyUrl = systemConfig.getDomain() + WECHAT_NOTIFY_URL;
                orderParam.setNotifyUrl(notifyUrl);
                orderParam.setTradeType("JSAPI");
                orderParam.setOpenId(openId);
                Map<String, String> orderMap = wechatService.unifiedOrder(orderParam);
                if (!orderMap.isEmpty()) {
                    // 插入支付记录
                    CustomerPay customerPay = new CustomerPay();
                    customerPay.setCustomerGuid(customerGuid);
                    customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.RE_CHARGE_PAY.getType());
                    customerPay.setWebcharNo(openId);
                    customerPay.setProjectGuid(schemeDeviceVo.getProjectGuid());
                    customerPay.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
                    customerPay.setPayCategory(payCategory);
                    customerPay.setPayMoney(thirdPayMoney);
                    customerPay.setAccountChargeMoney(thirdPayMoney);
                    customerPay.setAccountDeductMoney(BigDecimal.ZERO);
                    customerPay.setChargingTime(schemeDeviceVo.getChargingTime());
                    customerPay.setSerialNum(DateUtil.getSerialNum());
                    customerPay.setPayFlag(outTradeNo);
                    customerPay.setCreateTime(new Date());
                    customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());

                    // 充值后剩余金额，此处是先将充值前剩余金额赋值上，避免用户点击支付，然后又取消了，导致支付失败的订单上该字段为null
                    // 充值成功后，在回调处还会更新此字段
                    customerPay.setAfterRemainAmount(prePayInfo.getBalancePayMoney());
                    customerPay.setType(CustomerConstant.PayScene.CHARGE_ELEC.getType());
                    customerPay.setPayWay(CustomerConstant.PayWayConstant.WECHAT.getType());
                    iCustomerPayService.insertSelective(customerPay);

                    //设置微信下单参数
                    chargeInfo.setAppId(orderMap.get("appId"));
                    chargeInfo.setNonceStr(orderMap.get("nonceStr"));
                    chargeInfo.setTimeStamp(orderMap.get("timeStamp"));
                    chargeInfo.setPackages(orderMap.get("package"));
                    chargeInfo.setPaySign(orderMap.get("paySign"));
                    chargeInfo.setSignType(orderMap.get("signType"));

                    // 新增开电记录与支付订单关联表记录
                    DeviceUsePayRelation relation = new DeviceUsePayRelation();
                    relation.setChargingGuid(chargingGuid);
                    relation.setPayFlag(outTradeNo);
                    relation.setPayMoney(thirdPayMoney);
                    relation.setCreateTime(new Date());
                    relation.setSignGuid(signGuid);
                    iDeviceUsePayRelationService.insertSelective(relation);
                }
            } else {
                // 钱够，就直接下发续充电指令
                String queueGuid = UuidUtil.getUuid();
                Afn19Object afn19Object = new Afn19Object(queueGuid,
                        "19",
                        "999999999",
                        "42475858fffffa",
                        commNo,"20000",
                        port,
                        "on",
                        "",
                        openId,
                        String.valueOf(id),
                        String.valueOf(chargingTime),
                        workMode,
                        openMeans,
                        measure,
                        String.valueOf(maxPower));
                Integer result = rabbitmqProducer.sendAfn19(afn19Object);
                if (result != 1) {
                    // TODO 退款

                    throw new BusinessException("下发充电指令失败");
                }
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            // 包月用户的处理
            this.monthCardProcess4ReCharge(schemeDeviceVo, customerGuid, openMeans, measure, openId, chargingGuid);
        }
        return chargeInfo;
    }

    /**
     * 服务商模式，续充电
     * @param rechargeVo
     * @param schemeDeviceVo
     * @return
     */
    private WechatChargeInfo recharge4ServiceMerchant(WechatRechargeVo rechargeVo,ChargingSchemeDeviceVo schemeDeviceVo) {
        WechatChargeInfo chargeInfo = new WechatChargeInfo();
        chargeInfo.setIsCharge(1);
        Integer openMeans = CustomerConstant.OpenMeansConstant.WECHAT.getType();
        Integer payCategory = schemeDeviceVo.getPayCategory();
        String openId = rechargeVo.getOpenId();
        String chargingGuid = rechargeVo.getChargingGuid();
        String customerGuid = rechargeVo.getCustomerGuid();
        Integer measure = CustomerConstant.MeasureType.APPEND_MEASURE.getType();
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory) ||
                SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            chargeInfo.setIsCharge(0);
            BigDecimal realMoney = schemeDeviceVo.getRealMoney();
            chargeInfo.setPay(MathUtil.setPrecision(realMoney));

            // 查询服务商模式微信配置
            MerchantWxConfig wxConfig = iMerchantWxConfigService.queryByMerchantGuid(schemeDeviceVo.getMerchantGuid());

            // 统一下单
            WxUnifiedOrderParam orderParam = new WxUnifiedOrderParam();
            orderParam.setSubMchId(wxConfig.getSubMchId());
            orderParam.setBody("充电桩续充电");
            String outTradeNo = UuidUtil.getUuid();
            orderParam.setOutTradeNo(outTradeNo);
            Integer totalFee = MathUtil.yuan2Fen(String.valueOf(realMoney));
            orderParam.setTotalFee(totalFee);
            orderParam.setSpbillCreateIp(rechargeVo.getIp());
            String notifyUrl = systemConfig.getDomain() + WECHAT_NOTIFY_URL;
            orderParam.setNotifyUrl(notifyUrl);
            orderParam.setTradeType("JSAPI");
            orderParam.setOpenId(openId);
            Map<String, String> orderMap = wechatService.unifiedOrder(orderParam);
            if (!orderMap.isEmpty()) {
                // 插入支付记录
                CustomerPay customerPay = new CustomerPay();
                customerPay.setCustomerGuid(customerGuid);
                customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.RE_CHARGE_PAY.getType());
                customerPay.setWebcharNo(openId);
                customerPay.setProjectGuid(schemeDeviceVo.getProjectGuid());
                customerPay.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
                customerPay.setPayCategory(payCategory);
                customerPay.setPayMoney(realMoney);
                customerPay.setAccountChargeMoney(BigDecimal.ZERO);
                customerPay.setAccountDeductMoney(BigDecimal.ZERO);
                customerPay.setChargingTime(schemeDeviceVo.getChargingTime());
                customerPay.setSerialNum(DateUtil.getSerialNum());
                customerPay.setPayFlag(outTradeNo);
                customerPay.setCreateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
                customerPay.setAfterRemainAmount(BigDecimal.ZERO);
                customerPay.setType(CustomerConstant.PayScene.CHARGE_ELEC.getType());
                customerPay.setPayWay(CustomerConstant.PayWayConstant.WECHAT.getType());
                iCustomerPayService.insertSelective(customerPay);

                //设置微信下单参数
                chargeInfo.setAppId(orderMap.get("appId"));
                chargeInfo.setNonceStr(orderMap.get("nonceStr"));
                chargeInfo.setTimeStamp(orderMap.get("timeStamp"));
                chargeInfo.setPackages(orderMap.get("package"));
                chargeInfo.setPaySign(orderMap.get("paySign"));
                chargeInfo.setSignType(orderMap.get("signType"));


                // 新增开电记录与支付订单关联表记录
                DeviceUsePayRelation relation = new DeviceUsePayRelation();
                relation.setChargingGuid(chargingGuid);
                relation.setPayFlag(outTradeNo);
                relation.setPayMoney(realMoney);
                relation.setCreateTime(new Date());
                relation.setSignGuid(UuidUtil.getUuid());
                iDeviceUsePayRelationService.insertSelective(relation);
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            // 包月用户的处理
            this.monthCardProcess4ReCharge(schemeDeviceVo, customerGuid, openMeans, measure, openId, chargingGuid);
        }
        return chargeInfo;
    }

    /**
     * 微信，支付宝，月卡续充处理
     * @param schemeDeviceVo
     * @param customerGuid
     * @param openMeans
     * @param measure
     * @param openId
     * @param chargingGuid
     */
    private void monthCardProcess4ReCharge(ChargingSchemeDeviceVo schemeDeviceVo,String customerGuid,Integer openMeans,Integer measure, String openId,String chargingGuid) {
        // 包月用户的处理
        MonthPreChargeVo preChargeVo = new MonthPreChargeVo();
        preChargeVo.setUserGuid(customerGuid);
        preChargeVo.setDataGuid(chargingGuid);
        String signGuid = UuidUtil.getUuid();
        preChargeVo.setSignGuid(signGuid);
        MonthPreChargeInfo preChargeInfo = monthAccountService.preChargeElectric(preChargeVo);
        String accountGuid = preChargeInfo.getAccountGuid();

        // 新增开电记录与月卡使用关联表
        DeviceUseMonthRelation relation = new DeviceUseMonthRelation();
        relation.setChargingGuid(chargingGuid);
        relation.setChargeType(CustomerConstant.ChargeType.RECHARGE.getType());
        relation.setAccountGuid(accountGuid);
        relation.setChargeCnt(1);
        relation.setCreateTime(new Date());
        relation.setSignGuid(UuidUtil.getUuid());
        iDeviceUseMonthRelationService.insertSelective(relation);

        String commNo = schemeDeviceVo.getCommNo();
        String port = schemeDeviceVo.getPort();
        Integer id = schemeDeviceVo.getId();
        Integer maxPower = schemeDeviceVo.getMaxPower();
        Integer chargingTime = schemeDeviceVo.getChargingTime();
        String queueGuid = UuidUtil.getUuid();
        String workMode = String.valueOf(CustomerConstant.ChargingWay.CHARGING_TIME.getWay());
        Afn19Object afn19Object = new Afn19Object(queueGuid,
                "19",
                "999999999",
                "42475858fffffa",
                commNo,"20000",
                port,
                "on",
                "",
                openId,
                String.valueOf(id),
                String.valueOf(chargingTime),
                workMode,
                openMeans,
                measure,
                String.valueOf(maxPower));
        Integer result = rabbitmqProducer.sendAfn19(afn19Object);
        if (result != 1) {
            // 执行退款
            MonthConfirmRefundVo refundVo = new MonthConfirmRefundVo();
            refundVo.setUserGuid(customerGuid);
            refundVo.setDataGuid(chargingGuid);
            refundVo.setSignGuid(signGuid);
            monthAccountService.confirmRefund(refundVo);
            throw new BusinessException("续开电指令投递队列异常");
        }
    }

    /**
     * 普通商户模式开电
     * @param chargeVo
     * @param schemeDeviceVo
     */
    private WechatChargeInfo charge4CommonMerchant(WechatChargeVo chargeVo,ChargingSchemeDeviceVo schemeDeviceVo) {
        // 普通商户模式
        WechatChargeInfo chargeInfo = new WechatChargeInfo();
        chargeInfo.setIsCharge(1);

        String customerGuid = chargeVo.getCustomerGuid();
        Integer openMeans = CustomerConstant.OpenMeansConstant.WECHAT.getType();
        String openId = chargeVo.getOpenId();
        Integer measure = CustomerConstant.MeasureType.RE_MEASURE.getType();

        //针对不包月购买次数的用户
        Integer payCategory = schemeDeviceVo.getPayCategory();
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory) ||
                SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            //获取用户余额，和传入价格方案的金额进行对比
            PrePayVo prePayVo = new PrePayVo();
            prePayVo.setPayMoney(schemeDeviceVo.getRealMoney());
            String chargingGuid = UuidUtil.getUuid();
            prePayVo.setDataGuid(chargingGuid);
            String signGuid = UuidUtil.getUuid();
            prePayVo.setSignGuid(signGuid);
            prePayVo.setUserGuid(customerGuid);
            prePayVo.setAccountType(AccountTypeEnum.CUSTOMER.getType());
            prePayVo.setOperateType(1);
            PrePayInfo prePayInfo = accountService.prePay(prePayVo);
            BigDecimal thirdPayMoney = prePayInfo.getThirdPayMoney();
            boolean greateThanZero = MathUtil.isGreateThanZero(thirdPayMoney);
            if (greateThanZero) {
                // 钱不够，需要第三方支付
                String ip = chargeVo.getIp();
                this.moneyNotEnough4Wx(chargeInfo,schemeDeviceVo,prePayInfo,chargingGuid,signGuid,customerGuid,openId,ip);
            } else {
                // 钱够，就直接下发充电指令
                this.moneyEnough(schemeDeviceVo,prePayInfo,chargingGuid,signGuid,customerGuid,openMeans,openId,measure);
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            this.monthCardProcess4Charge(schemeDeviceVo,customerGuid, openMeans,openId, measure);
        }

        return chargeInfo;
    }

    private void moneyNotEnough4Wx(WechatChargeInfo chargeInfo,ChargingSchemeDeviceVo schemeDeviceVo,PrePayInfo prePayInfo,String chargingGuid,String signGuid,String customerGuid,String openId,String ip) {
        // 需要第三方支付
        chargeInfo.setIsCharge(0);
        BigDecimal thirdPayMoney = prePayInfo.getThirdPayMoney();
        chargeInfo.setPay(MathUtil.setPrecision(thirdPayMoney));

        // 统一下单
        WxUnifiedOrderParam orderParam = new WxUnifiedOrderParam();
        orderParam.setBody("充电桩充电");
        String outTradeNo = UuidUtil.getUuid();
        orderParam.setOutTradeNo(outTradeNo);
        Integer totalFee = MathUtil.yuan2Fen(String.valueOf(thirdPayMoney));
        orderParam.setTotalFee(totalFee);
        orderParam.setSpbillCreateIp(ip);
        String notifyUrl = systemConfig.getDomain() + WECHAT_NOTIFY_URL;
        orderParam.setNotifyUrl(notifyUrl);
        orderParam.setTradeType("JSAPI");
        orderParam.setOpenId(openId);
        Map<String, String> orderMap = wechatService.unifiedOrder(orderParam);
        BigDecimal balancePayMoney = prePayInfo.getBalancePayMoney();
        if (!orderMap.isEmpty()) {
            //设置微信下单参数
            chargeInfo.setAppId(orderMap.get("appId"));
            chargeInfo.setNonceStr(orderMap.get("nonceStr"));
            chargeInfo.setTimeStamp(orderMap.get("timeStamp"));
            chargeInfo.setPackages(orderMap.get("package"));
            chargeInfo.setPaySign(orderMap.get("paySign"));
            chargeInfo.setSignType(orderMap.get("signType"));

            // 新增充电记录
            Integer openMeans = CustomerConstant.OpenMeansConstant.WECHAT.getType();
            this.addChargeRecord(schemeDeviceVo,chargingGuid,BigDecimal.ZERO,customerGuid,openMeans,openId);

            Integer payWay = CustomerConstant.PayWayConstant.WECHAT.getType();
            this.thirdPay(schemeDeviceVo, prePayInfo, chargingGuid, signGuid, outTradeNo, customerGuid, payWay, openId);

            // 存在部分余额支付
            if (MathUtil.isGreateThanZero(balancePayMoney)) {
                // 插入余额支付记录
                CustomerPay customerPay = this.balancePay(schemeDeviceVo, prePayInfo, customerGuid, openId);

                // 新增开电记录与支付订单关联表记录
                String payFlag = customerPay.getPayFlag();
                this.usePayRelationAdd(chargingGuid, payFlag, signGuid, balancePayMoney);
                // 传给前端，用户取消支付时，需要
                chargeInfo.setOutTradeNo(customerPay.getPayFlag());
            }
        } else {
            // 调用支付服务生成订单失败
            log.info("调用微信支付服务生成订单失败");
            if (MathUtil.isGreateThanZero(balancePayMoney)) {
                this.cancelPrePay(customerGuid,chargingGuid,signGuid);
            }
            throw new BusinessException("调用支付服务接口异常");
        }
    }

    /**
     * 开电指令发送失败处理逻辑
     * @param useDetailed
     */
    private void chargeFail(DeviceUseDetailed useDetailed,List<CustomerPay> customerPays,List<DeviceUsePayRelation> payRelations) {


        // 退款
        this.cancelPay(customerPays,payRelations,useDetailed);
    }

    private void thirdPay(ChargingSchemeDeviceVo schemeDeviceVo,PrePayInfo prePayInfo,String chargingGuid,String signGuid,String outTradeNo,String customerGuid,Integer payWay,String openId) {
        // 插入支付记录
        CustomerPay customerPay = new CustomerPay();
        customerPay.setCustomerGuid(customerGuid);
        customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.CHARGE_PAY.getType());
        if (CustomerConstant.PayWayConstant.WECHAT.getType().equals(payWay)) {
            customerPay.setPayWay(CustomerConstant.PayWayConstant.WECHAT.getType());
        } else if (CustomerConstant.PayWayConstant.ALIPAY.getType().equals(payWay)){
            customerPay.setPayWay(CustomerConstant.PayWayConstant.ALIPAY.getType());
        }
        customerPay.setWebcharNo(openId);
        customerPay.setMerchantGuid(schemeDeviceVo.getMerchantGuid());
        customerPay.setPropertyGuid(schemeDeviceVo.getPropertyGuid());
        customerPay.setProjectGuid(schemeDeviceVo.getProjectGuid());
        customerPay.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
        customerPay.setPayCategory(schemeDeviceVo.getPayCategory());
        BigDecimal thirdPayMoney = prePayInfo.getThirdPayMoney();
        customerPay.setPayMoney(thirdPayMoney);
        customerPay.setAccountChargeMoney(BigDecimal.ZERO);
        customerPay.setAccountDeductMoney(prePayInfo.getBalancePayMoney());
        customerPay.setChargingTime(schemeDeviceVo.getChargingTime());
        customerPay.setSerialNum(DateUtil.getSerialNum());
        customerPay.setPayFlag(outTradeNo);
        customerPay.setCreateTime(new Date());
        customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
        customerPay.setAfterRemainAmount(prePayInfo.getBalancePayMoney());
        customerPay.setType(CustomerConstant.PayScene.CHARGE_ELEC.getType());
        iCustomerPayService.insertSelective(customerPay);

        // 新增开电记录与支付订单关联表记录
        DeviceUsePayRelation relation = new DeviceUsePayRelation();
        relation.setChargingGuid(chargingGuid);
        relation.setPayFlag(outTradeNo);
        relation.setPayMoney(thirdPayMoney);
        relation.setCreateTime(new Date());
        relation.setSignGuid(signGuid);
        iDeviceUsePayRelationService.insertSelective(relation);
    }


    /**
     * 服务商模式模式开电
     * @param chargeVo
     * @param schemeDeviceVo
     */
    private WechatChargeInfo charge4ServiceMerchant(WechatChargeVo chargeVo,ChargingSchemeDeviceVo schemeDeviceVo) {
        // 普通商户模式
        String customerGuid = chargeVo.getCustomerGuid();
        String openId = chargeVo.getOpenId();

        WechatChargeInfo chargeInfo = new WechatChargeInfo();
        chargeInfo.setIsCharge(1);

        Integer openMeans = CustomerConstant.OpenMeansConstant.WECHAT.getType();
        //针对不包月购买次数的用户
        Integer payCategory = schemeDeviceVo.getPayCategory();
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory) ||
                SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            // 需要第三方支付
            chargeInfo.setIsCharge(0);
            BigDecimal realMoney = schemeDeviceVo.getRealMoney();
            chargeInfo.setPay(realMoney);

            // 查询服务商信息
            MerchantWxConfig wxConfig = iMerchantWxConfigService.queryByMerchantGuid(schemeDeviceVo.getMerchantGuid());

            // 统一下单
            WxUnifiedOrderParam orderParam = new WxUnifiedOrderParam();
            orderParam.setSubMchId(wxConfig.getSubMchId());
            orderParam.setBody("充电桩充电");
            String outTradeNo = UuidUtil.getUuid();
            orderParam.setOutTradeNo(outTradeNo);
            Integer totalFee = MathUtil.yuan2Fen(String.valueOf(realMoney));
            orderParam.setTotalFee(totalFee);
            orderParam.setSpbillCreateIp(chargeVo.getIp());
            String notifyUrl = systemConfig.getDomain() + WECHAT_NOTIFY_URL;
            orderParam.setNotifyUrl(notifyUrl);
            orderParam.setTradeType("JSAPI");
            orderParam.setOpenId(openId);
            Map<String, String> orderMap = wechatService.unifiedOrder(orderParam);
            if (!orderMap.isEmpty()) {
                // 插入支付记录
                CustomerPay customerPay = new CustomerPay();
                customerPay.setCustomerGuid(customerGuid);
                customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.CHARGE_PAY.getType());
                customerPay.setWebcharNo(openId);
                customerPay.setProjectGuid(schemeDeviceVo.getProjectGuid());
                customerPay.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
                customerPay.setPayCategory(payCategory);
                customerPay.setPayMoney(realMoney);
                customerPay.setAccountChargeMoney(BigDecimal.ZERO);
                customerPay.setAccountDeductMoney(BigDecimal.ZERO);
                customerPay.setChargingTime(schemeDeviceVo.getChargingTime());
                customerPay.setSerialNum(DateUtil.getSerialNum());
                customerPay.setPayFlag(outTradeNo);
                customerPay.setCreateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
                customerPay.setAfterRemainAmount(BigDecimal.ZERO);
                customerPay.setType(CustomerConstant.PayScene.CHARGE_ELEC.getType());
                customerPay.setPayWay(CustomerConstant.PayWayConstant.WECHAT.getType());
                iCustomerPayService.insertSelective(customerPay);

                //设置微信下单参数
                chargeInfo.setAppId(orderMap.get("appId"));
                chargeInfo.setNonceStr(orderMap.get("nonceStr"));
                chargeInfo.setTimeStamp(orderMap.get("timeStamp"));
                chargeInfo.setPackages(orderMap.get("package"));
                chargeInfo.setPaySign(orderMap.get("paySign"));
                chargeInfo.setSignType(orderMap.get("signType"));

                // 新增充电记录
                String chargingGuid = UuidUtil.getUuid();
                this.addChargeRecord(schemeDeviceVo,chargingGuid,BigDecimal.ZERO,customerGuid,openMeans,openId);

                // 新增开电记录与支付订单关联表记录
                DeviceUsePayRelation relation = new DeviceUsePayRelation();
                relation.setChargingGuid(chargingGuid);
                relation.setPayFlag(outTradeNo);
                relation.setPayMoney(realMoney);
                relation.setCreateTime(new Date());
                relation.setSignGuid(UuidUtil.getUuid());
                iDeviceUsePayRelationService.insertSelective(relation);
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            // 包月用户的处理
            Integer measure = CustomerConstant.MeasureType.RE_MEASURE.getType();
            this.monthCardProcess4Charge(schemeDeviceVo,customerGuid, openMeans,openId, measure);

        }

        return chargeInfo;
    }


    private DeviceUseDetailed addChargeRecord(ChargingSchemeDeviceVo schemeDeviceVo,String chargingGuid,BigDecimal afterRemainAmount,String customerGuid,Integer openMeans,String openId) {
        // 新增充电使用记录表
        DeviceUseDetailed detailed = new DeviceUseDetailed();
        detailed.setProjectGuid(schemeDeviceVo.getProjectGuid());
        detailed.setMerchantGuid(schemeDeviceVo.getMerchantGuid());
        detailed.setCustomerGuid(customerGuid);
        detailed.setChargingPlieGuid(schemeDeviceVo.getChargingPlieGuid());
        detailed.setDeviceNo(schemeDeviceVo.getDeviceNo());
        String port = schemeDeviceVo.getPort();
        detailed.setPort(port);
        detailed.setCommNo(schemeDeviceVo.getCommNo());
        Integer payCategory = schemeDeviceVo.getPayCategory();
        detailed.setPayCategory(payCategory);
        detailed.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
        if (SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory)) {
            // 临时充电
            detailed.setChargingWay(1);
            // 充电扣除费用
            detailed.setDeductMoney(schemeDeviceVo.getRealMoney());
        } else if (SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            // 一次充满
            detailed.setChargingWay(3);
            // 充电扣除费用
            detailed.setDeductMoney(schemeDeviceVo.getRealMoney());
        }
        detailed.setChargingTime(schemeDeviceVo.getChargingTime());

        // 本次充电后剩余金额
        detailed.setAfterRemainAmount(afterRemainAmount);
        detailed.setRealPrice(schemeDeviceVo.getPrice());
        detailed.setState(CustomerConstant.DeviceUsedRecordState.WAITING.getState());
        //设置充电时间

        detailed.setChargingGuid(chargingGuid);
        detailed.setCreateTime(new Date());
        detailed.setOpenMeans(openMeans);
        detailed.setOpenNo(openId);
        detailed.setPropertyGuid(schemeDeviceVo.getPropertyGuid());

        iDeviceUseDetailedService.insertSelective(detailed);
        return detailed;
    }

    /**
     * 微信支付宝、月卡处理
     * @param schemeDeviceVo
     * @param openMeans
     * @param measure
     */
    private void monthCardProcess4Charge(ChargingSchemeDeviceVo schemeDeviceVo,String customerGuid,Integer openMeans,String openId,Integer measure) {
        // 包月用户的处理
        MonthPreChargeVo preChargeVo = new MonthPreChargeVo();
        preChargeVo.setUserGuid(customerGuid);
        String chargingGuid = UuidUtil.getUuid();
        preChargeVo.setDataGuid(chargingGuid);
        String signGuid = UuidUtil.getUuid();
        preChargeVo.setSignGuid(signGuid);
        MonthPreChargeInfo preChargeInfo = monthAccountService.preChargeElectric(preChargeVo);
        Integer remainCnt = preChargeInfo.getRemainCnt();
        String accountGuid = preChargeInfo.getAccountGuid();

        // 新增月卡开电记录
        this.addChargeRecord4Month(schemeDeviceVo,chargingGuid,remainCnt,accountGuid,customerGuid,openMeans,openId);

        // 新增开电记录与月卡使用关联表
        DeviceUseMonthRelation relation = new DeviceUseMonthRelation();
        relation.setChargingGuid(chargingGuid);
        relation.setChargeType(CustomerConstant.ChargeType.FIRST_CHARGE.getType());
        relation.setAccountGuid(accountGuid);
        relation.setChargeCnt(1);
        relation.setCreateTime(new Date());
        relation.setSignGuid(signGuid);
        iDeviceUseMonthRelationService.insertSelective(relation);

        // 发送通电指令
        String workMode = String.valueOf(CustomerConstant.ChargingWay.CHARGING_TIME.getWay());
        Integer maxPower = schemeDeviceVo.getMaxPower();
        String commNo = schemeDeviceVo.getCommNo();
        String port = schemeDeviceVo.getPort();
        Integer id = schemeDeviceVo.getId();
        Integer chargingTime = schemeDeviceVo.getChargingTime();
        String queueGuid = UuidUtil.getUuid();
        Afn19Object afn19Object = new Afn19Object(queueGuid,
                "19",
                "999999999",
                "42475858fffffa",
                commNo,"20000",
                port,
                "on",
                "",
                openId,
                String.valueOf(id),
                String.valueOf(chargingTime),
                workMode,
                openMeans,
                measure,
                String.valueOf(maxPower));
        Integer result = rabbitmqProducer.sendAfn19(afn19Object);
        if (result != 1) {
            // 执行退款
            MonthConfirmRefundVo refundVo = new MonthConfirmRefundVo();
            refundVo.setUserGuid(customerGuid);
            refundVo.setDataGuid(chargingGuid);
            refundVo.setSignGuid(signGuid);
            monthAccountService.confirmRefund(refundVo);
        }
    }


    private void addChargeRecord4Month(ChargingSchemeDeviceVo schemeDeviceVo,String chargingGuid,Integer afterRemainCnt,String monthAcctGuid,String customerGuid,Integer openMeans,String openId) {
        // 新增充电使用记录表
        DeviceUseDetailed detailed = new DeviceUseDetailed();
        detailed.setCustomerGuid(customerGuid);
        detailed.setProjectGuid(schemeDeviceVo.getProjectGuid());
        detailed.setMerchantGuid(schemeDeviceVo.getMerchantGuid());
        detailed.setChargingPlieGuid(schemeDeviceVo.getChargingPlieGuid());
        String port = schemeDeviceVo.getPort();
        detailed.setPort(port);
        detailed.setCommNo(schemeDeviceVo.getCommNo());
        detailed.setDeviceNo(schemeDeviceVo.getDeviceNo());
        Integer payCategory = schemeDeviceVo.getPayCategory();
        detailed.setPayCategory(payCategory);
        detailed.setMonthAcctGuid(monthAcctGuid);
        detailed.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
        detailed.setChargingWay(3);
        Integer chargingCnt = schemeDeviceVo.getChargingCnt();
        BigDecimal realMoney = schemeDeviceVo.getRealMoney();
        BigDecimal deductMoney = MathUtil.divide(realMoney, new BigDecimal(chargingCnt));
        if (afterRemainCnt <= 0) {
            // 说明是月卡最后一次充电
            BigDecimal temp = new BigDecimal(chargingCnt-1);
            // 计算除了最后一次，月卡累计使用的金额
            BigDecimal total = deductMoney.multiply(temp);
            // 计算最后一次月卡开电，使用的金额
            deductMoney = realMoney.subtract(total);
        }

        // 充电扣除费用
        detailed.setDeductMoney(deductMoney);
        detailed.setDeductCnt(1);

        // 本次充电后剩余金额
        detailed.setAfterRemainCnt(afterRemainCnt);
        detailed.setRealPrice(schemeDeviceVo.getPrice());
        detailed.setState(CustomerConstant.DeviceUsedRecordState.WAITING.getState());
        //设置充电时间
        detailed.setChargingTime(schemeDeviceVo.getChargingTime());
        detailed.setChargingGuid(chargingGuid);
        detailed.setCreateTime(new Date());
        detailed.setOpenMeans(openMeans);
        detailed.setOpenNo(openId);
        detailed.setPropertyGuid(schemeDeviceVo.getPropertyGuid());
        iDeviceUseDetailedService.insertSelective(detailed);
    }

    @Override
    public AlipayChargeInfo alipayCharge(AlipayChargeVo chargeVo) {
        log.info("客户支付宝开电接口AlipayChargeVo={}",JSON.toJSONString(chargeVo));
        // 查询充电设备，方案信息
        CustomerChargingParam chargingParam = new CustomerChargingParam();
        chargingParam.setDeviceNo(chargeVo.getDeviceNo());
        chargingParam.setSchemeGuid(chargeVo.getSchemeGuid());
        ChargingSchemeDeviceVo schemeDeviceVo = merchantToCustomerService.queryChargeDeviceSchemeInfo(chargingParam);
        Integer runState = schemeDeviceVo.getRunState();
        if (DeviceConstant.DeviceRunState.CHARGING.getState().equals(runState)) {
            throw new BusinessException("设备处于开电中");
        }

        Integer serviceMode = schemeDeviceVo.getServiceMode();
        AlipayChargeInfo chargeInfo;
        if (MerchantConstant.ServiceMode.COMMON_MERCHANT.getMode().equals(serviceMode)) {
            // 普通商户模式
            chargeInfo = this.zfbCharge4CommonMerchant(chargeVo,schemeDeviceVo);
        } else {
            // 服务商模式
            chargeInfo = this.zfbCharge4ServiceMerchant(chargeVo,schemeDeviceVo);
        }

        return chargeInfo;
    }

    @Override
    public AlipayChargeInfo alipayReCharge(AlipayRechargeVo rechargeVo) {
        log.info("客户支付宝续开电接口AlipayRechargeVo={}",JSON.toJSONString(rechargeVo));

        // 查询
        String chargingGuid = rechargeVo.getChargingGuid();
        DeviceUseDetailed useDetailed = iDeviceUseDetailedService.queryChargeOrder(chargingGuid);
        Integer state = useDetailed.getState();
        if (!CustomerConstant.DeviceUsedRecordState.CHARGING.getState().equals(state)) {
            throw new BusinessException("非充电中状态，不允许发起续充电");
        }

        // 查询开电设备，方案信息
        CustomerChargingParam param = new CustomerChargingParam();
        param.setChargingPlieGuid(useDetailed.getChargingPlieGuid());
        param.setSchemeGuid(useDetailed.getSchemeGuid());
        ChargingSchemeDeviceVo schemeDeviceVo = merchantToCustomerService.queryChargeDeviceSchemeInfo(param);
        Integer serviceMode = schemeDeviceVo.getServiceMode();
        AlipayChargeInfo chargeInfo;
        if (MerchantConstant.ServiceMode.COMMON_MERCHANT.getMode().equals(serviceMode)) {
            // 普通商户模式
            chargeInfo = this.zfbRecharge4CommonMerchant(rechargeVo,schemeDeviceVo);
        } else {
            // 服务商模式
            chargeInfo = this.zfbRecharge4ServiceMerchant(rechargeVo,schemeDeviceVo);
        }
        return chargeInfo;
    }

    @Override
    public void zfbPayNotify(ZfbPayNoticeParam param) {
        log.info("支付宝回调通知ZfbPayNoticeParam={}",JSON.toJSONString(param));

        boolean checkResult = alipayService.checkSign(param.getParams());
        if (!checkResult) {
            // 验证不通过
            return;
        }

        CustomerPay customerPay = iCustomerPayService.queryByPayFlagForUpdate(param.getOutTradeNo());
        if (customerPay == null) {
            // 非我方订单号，忽略
            return;
        }
        BigDecimal payMoney = customerPay.getPayMoney();
        boolean flag = payMoney.equals(param.getTotalAmount());
        if (!flag) {
            // 与我方订单实际支付金额不等，忽略
            return;
        }

        // 校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        // TODO 暂时注释
//        String alipayAppId = "";
//        if (!alipayAppId.equals(param.getAppId())) {
//            // 与我方appId不等，忽略
//            return;
//        }

        Integer payState = customerPay.getPayState();
        if (CustomerConstant.PayState.PAY_SUCCESS.getState().equals(payState)) {
            // 重复通知，忽略
            return;
        }

        String tradeStatus = param.getTradeStatus();
        //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
        if(tradeStatus.equals("TRADE_FINISHED")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            this.payNotifyCommonOperate(customerPay);
        } else if (tradeStatus.equals("TRADE_SUCCESS")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            this.payNotifyCommonOperate(customerPay);
        }
    }

    @Override
    public void userCancelPay(String payFlag) {
        log.info("前端用户调起支付后，点击取消支付");

        // 查询开电
        DeviceUsePayRelation usePayRelation = iDeviceUsePayRelationService.queryByPayFlag(payFlag);

        CancelPrePayVo cancelPrePayVo = new CancelPrePayVo();
        cancelPrePayVo.setUserGuid(usePayRelation.getCustomerGuid());
        cancelPrePayVo.setDataGuid(usePayRelation.getChargingGuid());
        cancelPrePayVo.setSignGuid(usePayRelation.getSignGuid());
        accountService.cancelPrePay(cancelPrePayVo);
    }

    /**
     * 普通商户模式，支付宝续充电
     * @param rechargeVo
     * @param schemeDeviceVo
     * @return
     */
    private AlipayChargeInfo zfbRecharge4CommonMerchant(AlipayRechargeVo rechargeVo,ChargingSchemeDeviceVo schemeDeviceVo) {
        String customerGuid = rechargeVo.getCustomerGuid();
        Integer chargingTime = schemeDeviceVo.getChargingTime();


        AlipayChargeInfo chargeInfo = new AlipayChargeInfo();
        chargeInfo.setIsCharge(1);
        String commNo = schemeDeviceVo.getCommNo();
        String port = schemeDeviceVo.getPort();
        Integer maxPower = schemeDeviceVo.getMaxPower();
        Integer id = schemeDeviceVo.getId();
        Integer openMeans = CustomerConstant.OpenMeansConstant.ALIPAY.getType();
        Integer measure = CustomerConstant.MeasureType.APPEND_MEASURE.getType();
        String workMode = String.valueOf(CustomerConstant.ChargingWay.CHARGING_TIME.getWay());

        String chargingGuid = rechargeVo.getChargingGuid();
        String alipayUserId = rechargeVo.getAlipayUserId();
        Integer payCategory = schemeDeviceVo.getPayCategory();
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory)
                || SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            PrePayVo prePayVo = new PrePayVo();
            prePayVo.setPayMoney(schemeDeviceVo.getRealMoney());
            prePayVo.setDataGuid(chargingGuid);
            String signGuid = UuidUtil.getUuid();
            prePayVo.setSignGuid(signGuid);
            prePayVo.setUserGuid(customerGuid);
            prePayVo.setAccountType(2);
            prePayVo.setOperateType(1);
            PrePayInfo prePayInfo = accountService.prePay(prePayVo);
            BigDecimal thirdPayMoney = prePayInfo.getThirdPayMoney();
            boolean greateThanZero = MathUtil.isGreateThanZero(thirdPayMoney);
            if (greateThanZero) {
                // 余额不足
                chargeInfo.setIsCharge(0);

                // 生成订单
                TradeCreateParam param = new TradeCreateParam();
                String outTradeNo = UuidUtil.getUuid();
                param.setOutTradeNo(outTradeNo);
                param.setTotalAmount(String.valueOf(thirdPayMoney));
                param.setSubject("充电桩续充电");
                param.setBuyerId(rechargeVo.getAlipayUserId());
                String notifyUrl = systemConfig.getDomain() + ALIPAY_NOTIFY_URL;
                param.setNotifyUrl(notifyUrl);
                String tradeNo = alipayService.tradeCreate(param);
                if (StringUtils.isNotBlank(tradeNo)) {
                    chargeInfo.setTradeNo(tradeNo);

                    // 插入支付记录
                    CustomerPay customerPay = new CustomerPay();
                    customerPay.setCustomerGuid(customerGuid);
                    customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.RE_CHARGE_PAY.getType());
                    customerPay.setWebcharNo(rechargeVo.getAlipayUserId());
                    customerPay.setProjectGuid(schemeDeviceVo.getProjectGuid());
                    customerPay.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
                    customerPay.setPayCategory(payCategory);
                    customerPay.setPayMoney(thirdPayMoney);
                    customerPay.setAccountChargeMoney(BigDecimal.ZERO);
                    customerPay.setAccountDeductMoney(BigDecimal.ZERO);
                    customerPay.setChargingTime(chargingTime);
                    customerPay.setSerialNum(DateUtil.getSerialNum());
                    customerPay.setPayFlag(outTradeNo);
                    customerPay.setCreateTime(new Date());
                    customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
                    customerPay.setAfterRemainAmount(prePayInfo.getBalancePayMoney());
                    customerPay.setType(CustomerConstant.PayScene.CHARGE_ELEC.getType());
                    customerPay.setPayWay(CustomerConstant.PayWayConstant.ALIPAY.getType());
                    iCustomerPayService.insertSelective(customerPay);

                    // 新增开电记录与支付订单关联表记录
                    DeviceUsePayRelation relation = new DeviceUsePayRelation();
                    relation.setChargingGuid(chargingGuid);
                    relation.setPayFlag(outTradeNo);
                    relation.setPayMoney(thirdPayMoney);
                    relation.setCreateTime(new Date());
                    relation.setSignGuid(signGuid);
                    iDeviceUsePayRelationService.insertSelective(relation);
                }
            } else {
                // 钱够，就直接下发续充电指令
                String queueGuid = UuidUtil.getUuid();
                Afn19Object afn19Object = new Afn19Object(queueGuid,
                        "19",
                        "999999999",
                        "42475858fffffa",
                        commNo,"20000",
                        port,
                        "on",
                        "",
                        alipayUserId,
                        String.valueOf(id),
                        String.valueOf(chargingTime),
                        workMode,
                        openMeans,
                        measure,
                        String.valueOf(maxPower));
                Integer result = rabbitmqProducer.sendAfn19(afn19Object);
                if (result != 1) {
                    // TODO 退款

                    throw new BusinessException("下发充电指令失败");
                }
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            // 包月用户的处理
            this.monthCardProcess4ReCharge(schemeDeviceVo, customerGuid, openMeans, measure, alipayUserId, chargingGuid);
        }
        return chargeInfo;
    }


    /**
     * 服务商模式，支付宝续充电
     * @param rechargeVo
     * @param schemeDeviceVo
     * @return
     */
    private AlipayChargeInfo zfbRecharge4ServiceMerchant(AlipayRechargeVo rechargeVo,ChargingSchemeDeviceVo schemeDeviceVo) {
        String customerGuid = rechargeVo.getCustomerGuid();
        Integer payCategory = schemeDeviceVo.getPayCategory();
        Integer chargingTime = schemeDeviceVo.getChargingTime();

        Integer openMeans = CustomerConstant.OpenMeansConstant.ALIPAY.getType();
        AlipayChargeInfo chargeInfo = new AlipayChargeInfo();
        chargeInfo.setIsCharge(1);
        String chargingGuid = rechargeVo.getChargingGuid();
        Integer measure = CustomerConstant.MeasureType.APPEND_MEASURE.getType();
        String alipayUserId = rechargeVo.getAlipayUserId();
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory)
                || SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            // 余额不足
            chargeInfo.setIsCharge(0);

            // 查询商户支付宝配置信息
            MerchantZfbConfig zfbConfig = iMerchantZfbConfigService.queryByMerchantGuid(schemeDeviceVo.getMerchantGuid());

            // 生成订单
            TradeCreateParam param = new TradeCreateParam();
            param.setAppAuthToken(zfbConfig.getAppAuthToken());
            String outTradeNo = UuidUtil.getUuid();
            param.setOutTradeNo(outTradeNo);
            BigDecimal realMoney = schemeDeviceVo.getRealMoney();
            param.setTotalAmount(String.valueOf(realMoney));
            param.setSubject("充电桩续充电");
            param.setBuyerId(rechargeVo.getAlipayUserId());
            String notifyUrl = systemConfig.getDomain() + ALIPAY_NOTIFY_URL;
            param.setNotifyUrl(notifyUrl);
            String tradeNo = alipayService.tradeCreate(param);
            if (StringUtils.isNotBlank(tradeNo)) {
                chargeInfo.setTradeNo(tradeNo);

                // 插入支付记录
                CustomerPay customerPay = new CustomerPay();
                customerPay.setCustomerGuid(customerGuid);
                customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.RE_CHARGE_PAY.getType());
                customerPay.setWebcharNo(rechargeVo.getAlipayUserId());
                customerPay.setProjectGuid(schemeDeviceVo.getProjectGuid());
                customerPay.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
                customerPay.setPayCategory(payCategory);
                customerPay.setPayMoney(realMoney);
                customerPay.setAccountChargeMoney(BigDecimal.ZERO);
                customerPay.setAccountDeductMoney(BigDecimal.ZERO);
                customerPay.setChargingTime(chargingTime);
                customerPay.setSerialNum(DateUtil.getSerialNum());
                customerPay.setPayFlag(outTradeNo);
                customerPay.setCreateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
                customerPay.setAfterRemainAmount(BigDecimal.ZERO);
                customerPay.setType(CustomerConstant.PayScene.CHARGE_ELEC.getType());
                customerPay.setPayWay(CustomerConstant.PayWayConstant.ALIPAY.getType());
                iCustomerPayService.insertSelective(customerPay);

                // 新增开电记录与支付订单关联表记录
                DeviceUsePayRelation relation = new DeviceUsePayRelation();
                relation.setChargingGuid(chargingGuid);
                relation.setPayFlag(outTradeNo);
                relation.setPayMoney(realMoney);
                relation.setCreateTime(new Date());
                relation.setSignGuid(UuidUtil.getUuid());
                iDeviceUsePayRelationService.insertSelective(relation);
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            // 包月用户的处理
            this.monthCardProcess4ReCharge(schemeDeviceVo, customerGuid, openMeans, measure, alipayUserId, chargingGuid);
        }
        return chargeInfo;
    }

    /**
     * 普通商户模式，支付宝开电
     * @param chargeVo
     * @param schemeDeviceVo
     * @return
     */
    private AlipayChargeInfo zfbCharge4CommonMerchant(AlipayChargeVo chargeVo,ChargingSchemeDeviceVo schemeDeviceVo) {
        String customerGuid = chargeVo.getCustomerGuid();
        String alipayUserId = chargeVo.getAlipayUserId();

        // 查询用户账户信息
        AlipayChargeInfo chargeInfo = new AlipayChargeInfo();
        chargeInfo.setIsCharge(1);
        Integer openMeans = CustomerConstant.OpenMeansConstant.ALIPAY.getType();
        Integer measure = CustomerConstant.MeasureType.RE_MEASURE.getType();

        Integer payCategory = schemeDeviceVo.getPayCategory();
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory)
                || SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            //获取用户余额，和传入价格方案的金额进行对比
            PrePayVo prePayVo = new PrePayVo();
            prePayVo.setPayMoney(schemeDeviceVo.getRealMoney());
            String chargingGuid = UuidUtil.getUuid();
            prePayVo.setDataGuid(chargingGuid);
            String signGuid = UuidUtil.getUuid();
            prePayVo.setSignGuid(signGuid);
            prePayVo.setUserGuid(customerGuid);
            prePayVo.setAccountType(2);
            prePayVo.setOperateType(1);
            PrePayInfo prePayInfo = accountService.prePay(prePayVo);
            BigDecimal thirdPayMoney = prePayInfo.getThirdPayMoney();
            boolean greateThanZero = MathUtil.isGreateThanZero(thirdPayMoney);
            if (greateThanZero) {
                // 钱不够，需要第三方支付
                this.moneyNotEnough4Zfb(chargeInfo,schemeDeviceVo,prePayInfo,chargingGuid,signGuid,customerGuid,alipayUserId);
            } else {
                // 钱够，就直接下发充电指令
                this.moneyEnough(schemeDeviceVo,prePayInfo,chargingGuid,signGuid,customerGuid,openMeans,alipayUserId,measure);
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            // 包月用户的处理
            this.monthCardProcess4Charge(schemeDeviceVo,customerGuid, openMeans,alipayUserId, measure);
        }

        return chargeInfo;
    }

    /**
     * 支付宝开电，钱不够的处理逻辑
     * @param chargeInfo
     * @param schemeDeviceVo
     * @param prePayInfo
     * @param chargingGuid
     * @param signGuid
     */
    private void moneyNotEnough4Zfb(AlipayChargeInfo chargeInfo,ChargingSchemeDeviceVo schemeDeviceVo,PrePayInfo prePayInfo,String chargingGuid,String signGuid,String customerGuid,String alipayUserId) {
        // 需要第三方支付
        TradeCreateParam param = new TradeCreateParam();
        String outTradeNo = UuidUtil.getUuid();
        param.setOutTradeNo(outTradeNo);
        BigDecimal thirdPayMoney = prePayInfo.getThirdPayMoney();
        param.setTotalAmount(String.valueOf(thirdPayMoney));
        param.setSubject("充电桩充电");
        param.setBuyerId(alipayUserId);
        String notifyUrl = systemConfig.getDomain() + ALIPAY_NOTIFY_URL;
        param.setNotifyUrl(notifyUrl);
        String tradeNo = alipayService.tradeCreate(param);
        BigDecimal balancePayMoney = prePayInfo.getBalancePayMoney();
        if (StringUtils.isNotBlank(tradeNo)) {
            chargeInfo.setIsCharge(0);
            chargeInfo.setTradeNo(tradeNo);

            // 新增充电记录
            Integer openMeans = CustomerConstant.OpenMeansConstant.ALIPAY.getType();
            this.addChargeRecord(schemeDeviceVo,chargingGuid,BigDecimal.ZERO,customerGuid,openMeans,alipayUserId);

            // 插入支付记录
            Integer payWay = CustomerConstant.PayWayConstant.ALIPAY.getType();
            this.thirdPay(schemeDeviceVo, prePayInfo, chargingGuid, signGuid, outTradeNo, customerGuid, payWay, alipayUserId);

            // 存在部分余额支付
            if (MathUtil.isGreateThanZero(balancePayMoney)) {
                // 插入余额支付记录
                CustomerPay tempPay = this.balancePay(schemeDeviceVo, prePayInfo, customerGuid,alipayUserId);

                // 新增开电记录与支付订单关联表记录
                String payFlag = tempPay.getPayFlag();
                this.usePayRelationAdd(chargingGuid,payFlag,signGuid,balancePayMoney);
                // 传给前端，用户取消支付时，需要
                chargeInfo.setOutTradeNo(tempPay.getPayFlag());
            }
        } else {
            // 调用支付服务生成订单失败
            log.info("调用支付宝支付服务生成订单失败");
            if (MathUtil.isGreateThanZero(balancePayMoney)) {
                this.cancelPrePay(customerGuid,chargingGuid,signGuid);
            }
            throw new BusinessException("调用支付服务接口异常");
        }
    }

    /**
     * 调用支付服务生成订单失败时，取消之前冻结的余额
     * @param customerGuid
     * @param chargingGuid
     * @param signGuid
     */
    private void cancelPrePay(String customerGuid,String chargingGuid,String signGuid) {
        CancelPrePayVo cancelPrePayVo = new CancelPrePayVo();
        cancelPrePayVo.setUserGuid(customerGuid);
        cancelPrePayVo.setDataGuid(chargingGuid);
        cancelPrePayVo.setSignGuid(signGuid);
        accountService.cancelPrePay(cancelPrePayVo);
    }

    /**
     * 开电账户余额足够处理逻辑
     * @param schemeDeviceVo
     * @param prePayInfo
     * @param customerGuid
     * @param openMeans
     * @param openId
     * @param measure
     */
    private void moneyEnough(ChargingSchemeDeviceVo schemeDeviceVo,PrePayInfo prePayInfo,String chargingGuid,String signGuid,String customerGuid,Integer openMeans,String openId,Integer measure) {
        BigDecimal remainAmount = prePayInfo.getRemainAmount();
        // 新增充电使用记录
        DeviceUseDetailed useDetailed = this.addChargeRecord(schemeDeviceVo, chargingGuid, remainAmount, customerGuid,openMeans,openId);

        // 生成余额支付订单
        CustomerPay customerPay = this.balancePay(schemeDeviceVo, prePayInfo,customerGuid,openId);

        // 新增开电记录与支付订单关联表记录
        String payFlag = customerPay.getPayFlag();
        BigDecimal balancePayMoney = prePayInfo.getBalancePayMoney();
        DeviceUsePayRelation relation = this.usePayRelationAdd(chargingGuid, payFlag, signGuid, balancePayMoney);

        // 发送通电指令
        String queueGuid = UuidUtil.getUuid();
        String commNo = schemeDeviceVo.getCommNo();
        String port = schemeDeviceVo.getPort();
        Integer maxPower = schemeDeviceVo.getMaxPower();
        Integer id = schemeDeviceVo.getId();
        Integer chargingTime = schemeDeviceVo.getChargingTime();
        String workMode = String.valueOf(CustomerConstant.ChargingWay.CHARGING_TIME.getWay());
        Afn19Object afn19Object = new Afn19Object(queueGuid,
                "19",
                "999999999",
                "42475858fffffa",
                commNo,"20000",
                port,
                "on",
                "",
                openId,
                String.valueOf(id),
                String.valueOf(chargingTime),
                workMode,
                openMeans,
                measure,
                String.valueOf(maxPower));
        Integer result = rabbitmqProducer.sendAfn19(afn19Object);
        if (true) {
//                if (result != 1) {
            List<CustomerPay> customerPays = Lists.newArrayList(customerPay);
            List<DeviceUsePayRelation> payRelations = Lists.newArrayList(relation);
            this.chargeFail(useDetailed,customerPays,payRelations);
            throw new BusinessException("开电指令投递队列异常");
        }
    }

    private CustomerPay balancePay(ChargingSchemeDeviceVo schemeDeviceVo, PrePayInfo prePayInfo, String customerGuid,String openId) {
        CustomerPay customerPay = new CustomerPay();
        customerPay.setCustomerGuid(customerGuid);
        customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.CHARGE_PAY.getType());
        customerPay.setWebcharNo(openId);
        customerPay.setMerchantGuid(schemeDeviceVo.getMerchantGuid());
        customerPay.setPropertyGuid(schemeDeviceVo.getPropertyGuid());
        customerPay.setProjectGuid(schemeDeviceVo.getProjectGuid());
        customerPay.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
        customerPay.setPayCategory(schemeDeviceVo.getPayCategory());
        BigDecimal balancePayMoney = prePayInfo.getBalancePayMoney();
        customerPay.setPayMoney(balancePayMoney);
        customerPay.setAccountChargeMoney(BigDecimal.ZERO);
        customerPay.setAccountDeductMoney(balancePayMoney);
        customerPay.setChargingTime(schemeDeviceVo.getChargingTime());
        customerPay.setSerialNum(DateUtil.getSerialNum());
        String payFlag = UuidUtil.getUuid();
        customerPay.setPayFlag(payFlag);
        customerPay.setCreateTime(new Date());
        customerPay.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
        customerPay.setAfterRemainAmount(prePayInfo.getRemainAmount());
        customerPay.setType(CustomerConstant.PayScene.CHARGE_ELEC.getType());
        customerPay.setPayWay(CustomerConstant.PayWayConstant.BALANCE.getType());
        iCustomerPayService.insertSelective(customerPay);
        return customerPay;
    }

    /**
     * 新增开电记录与支付订单关联表记录
     * @param chargingGuid
     * @param payFlag
     * @param signGuid
     * @param payMoney
     */
    private DeviceUsePayRelation usePayRelationAdd(String chargingGuid,String payFlag,String signGuid,BigDecimal payMoney) {
        DeviceUsePayRelation relation = new DeviceUsePayRelation();
        relation.setChargingGuid(chargingGuid);
        relation.setPayFlag(payFlag);
        relation.setPayMoney(payMoney);
        relation.setCreateTime(new Date());
        relation.setSignGuid(signGuid);
        iDeviceUsePayRelationService.insertSelective(relation);
        return relation;
    }

    /**
     * 服务商模式，支付宝开电
     * @param chargeVo
     * @param schemeDeviceVo
     * @return
     */
    private AlipayChargeInfo zfbCharge4ServiceMerchant(AlipayChargeVo chargeVo,ChargingSchemeDeviceVo schemeDeviceVo) {
        String customerGuid = chargeVo.getCustomerGuid();
        String alipayUserId = chargeVo.getAlipayUserId();

        Integer chargingTime = schemeDeviceVo.getChargingTime();
        // 查询用户账户信息
        AlipayChargeInfo chargeInfo = new AlipayChargeInfo();
        chargeInfo.setIsCharge(1);
        Integer payCategory = schemeDeviceVo.getPayCategory();
        Integer openMeans = CustomerConstant.OpenMeansConstant.ALIPAY.getType();
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory)
                || SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            // 查询商户支付宝配置信息
            MerchantZfbConfig zfbConfig = iMerchantZfbConfigService.queryByMerchantGuid(schemeDeviceVo.getMerchantGuid());
            // 需要第三方支付
            TradeCreateParam param = new TradeCreateParam();
            param.setAppAuthToken(zfbConfig.getAppAuthToken());
            String outTradeNo = UuidUtil.getUuid();
            param.setOutTradeNo(outTradeNo);
            BigDecimal realMoney = schemeDeviceVo.getRealMoney();
            param.setTotalAmount(String.valueOf(realMoney));
            param.setSubject("充电桩充电");
            param.setBuyerId(alipayUserId);
            String notifyUrl = systemConfig.getDomain() + ALIPAY_NOTIFY_URL;
            param.setNotifyUrl(notifyUrl);
            String tradeNo = alipayService.tradeCreate(param);
            if (StringUtils.isNotBlank(tradeNo)) {
                chargeInfo.setIsCharge(0);
                chargeInfo.setTradeNo(tradeNo);

                // 插入支付记录
                CustomerPay customerPay = new CustomerPay();
                customerPay.setCustomerGuid(customerGuid);
                customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.CHARGE_PAY.getType());
                customerPay.setWebcharNo(chargeVo.getAlipayUserId());
                customerPay.setMerchantGuid(schemeDeviceVo.getMerchantGuid());
                customerPay.setPropertyGuid(schemeDeviceVo.getPropertyGuid());
                customerPay.setProjectGuid(schemeDeviceVo.getProjectGuid());
                customerPay.setSchemeGuid(schemeDeviceVo.getSchemeGuid());
                customerPay.setPayCategory(payCategory);
                customerPay.setPayMoney(realMoney);
                customerPay.setAccountChargeMoney(BigDecimal.ZERO);
                customerPay.setAccountDeductMoney(BigDecimal.ZERO);
                customerPay.setChargingTime(chargingTime);
                customerPay.setSerialNum(DateUtil.getSerialNum());
                customerPay.setPayFlag(outTradeNo);
                customerPay.setCreateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
                customerPay.setAfterRemainAmount(BigDecimal.ZERO);
                customerPay.setType(CustomerConstant.PayScene.CHARGE_ELEC.getType());
                customerPay.setPayWay(CustomerConstant.PayWayConstant.ALIPAY.getType());
                iCustomerPayService.insertSelective(customerPay);

                // 新增充电记录
                String chargingGuid = UuidUtil.getUuid();
                this.addChargeRecord(schemeDeviceVo,chargingGuid,BigDecimal.ZERO,customerGuid,openMeans,alipayUserId);

                // 新增开电记录与支付订单关联表记录
                DeviceUsePayRelation relation = new DeviceUsePayRelation();
                relation.setChargingGuid(chargingGuid);
                relation.setPayFlag(outTradeNo);
                relation.setPayMoney(realMoney);
                relation.setCreateTime(new Date());
                relation.setSignGuid(UuidUtil.getUuid());
                iDeviceUsePayRelationService.insertSelective(relation);
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            // 包月用户的处理
            Integer measure = CustomerConstant.MeasureType.RE_MEASURE.getType();
            this.monthCardProcess4Charge(schemeDeviceVo,customerGuid, openMeans,alipayUserId, measure);
        }

        return chargeInfo;
    }

    @Override
    public PageData<ChargeHistoryInfo> queryChargeHistory(ChargeHistoryVo historyVo) {
        log.info("分页查询客户充电历史记录ChargeHistoryVo={}",JSON.toJSONString(historyVo));

        ChargeOrderQueryParam param = new ChargeOrderQueryParam();
        BeanUtils.copyProperties(historyVo,param);
        param.setState(1);
        PageData<DeviceUseDetailed> tempPageData = iDeviceUseDetailedService.queryChargeHistory(historyVo.getCustomerGuid(), historyVo.getPageNumber(), historyVo.getPageSize());
        List<DeviceUseDetailed> list = tempPageData.getList();
        PageData<ChargeHistoryInfo> pageData = new PageData<>();
        if (CollectionUtils.isEmpty(list)) {
            pageData.setTotal(0L);
            pageData.setList(new ArrayList<>());
            return pageData;
        }

        Set<String> set = Sets.newHashSet();
        List<Object> devLogIds = Lists.newArrayList();
        for (DeviceUseDetailed useDetailed : list) {
            set.add(useDetailed.getChargingPlieGuid());
            devLogIds.add(useDetailed.getDevLogId());
        }

        // 批量查询设备信息
        List<String> deviceGuids = Lists.newArrayList(set);
        List<DeviceVo> deviceVos = deviceService.queryDevices(deviceGuids);

        // 转map
        Map<String,DeviceVo> deviceMap = Maps.newHashMap();
        for (DeviceVo deviceVo : deviceVos) {
            deviceMap.put(deviceVo.getChargingPlieGuid(),deviceVo);
        }

        // 批量查询设备日志表信息
        Map<Integer, DeviceLog> deviceLogMap = iDeviceLogService.batchQuery2Map(devLogIds);

        List<ChargeHistoryInfo> dataList = Lists.newArrayList();
        for (DeviceUseDetailed useDetailed : list) {
            ChargeHistoryInfo historyInfo = new ChargeHistoryInfo();
            Integer devLogId = useDetailed.getDevLogId();
            DeviceLog deviceLog = deviceLogMap.get(devLogId);
            Integer eventCode = deviceLog.getEventCode();
            if (eventCode != null) {
                if (eventCode == -2) {
                    // 表示异常导致充电结束
                    historyInfo.setIsEvent(1);
                    historyInfo.setIsEventDesc(deviceLog.getSmsContent());
                } else if (eventCode == -1) {
                    // 正常结束
                    historyInfo.setIsEvent(0);
                } else {
                    // 正常情况，代码不可能执行到此处
                    historyInfo.setIsEvent(0);
                    log.info("代码执行到此处，系统存在bug");
                }
            }

            // 设置设备信息
            DeviceVo deviceVo = deviceMap.get(useDetailed.getChargingPlieGuid());
            historyInfo.setDeviceNo(deviceVo.getDeviceNo() + deviceVo.getPort());
            historyInfo.setInstallAddr(deviceVo.getInstallAddr());
            // 其他信息
            Integer payCategory = useDetailed.getPayCategory();
            historyInfo.setPayWay(payCategory);
            historyInfo.setRemainAmount(useDetailed.getAfterRemainAmount());
            historyInfo.setRemainCnt(useDetailed.getAfterRemainCnt());
            String chargeDate = DateUtil.formatDay(useDetailed.getCreateTime());
            historyInfo.setChargeDate(chargeDate);
            String startTime = DateUtil.formatDate(useDetailed.getStartTime());
            historyInfo.setStartTime(startTime);
            String endTime = DateUtil.formatDate(useDetailed.getEndTime());
            historyInfo.setEndTime(endTime);
            String chargeTime = DateUtil.subTime(useDetailed.getEndTime(), useDetailed.getStartTime());
            historyInfo.setChargeTime(chargeTime);
            historyInfo.setChargingGuid(useDetailed.getChargingGuid());

            this.setPayWayDesc(historyInfo,useDetailed);
            dataList.add(historyInfo);
        }

        pageData.setTotal(tempPageData.getTotal());
        pageData.setList(dataList);
        return pageData;
    }

    private void setPayWayDesc(ChargeHistoryInfo historyInfo,DeviceUseDetailed useDetailed) {
        Integer payCategory = useDetailed.getPayCategory();
        BigDecimal refundMoney = useDetailed.getRefundMoney();
        boolean greateThanZero = MathUtil.isGreateThanZero(refundMoney);
        if (SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory)
                || SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)) {
            if (greateThanZero) {
                // 有退款
                BigDecimal payMoney = MathUtil.sub(useDetailed.getDeductMoney(), refundMoney);
                String desc = CustomerConstant.ChargeDeductWay.PAY_USER_BALANCE.getPayWayDesc() + payMoney.toString() +  "元";
                historyInfo.setPayWayDesc(desc);
            } else {
                String desc = CustomerConstant.ChargeDeductWay.PAY_USER_BALANCE.getPayWayDesc() + useDetailed.getDeductMoney() +  "元";
                historyInfo.setPayWayDesc(desc);
            }
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)) {
            if (greateThanZero) {
                // 有退款
                String desc = CustomerConstant.ChargeDeductWay.PAY_MONTH_CNT.getPayWayDesc()  +  "0次";
                historyInfo.setPayWayDesc(desc);
            } else {
                String desc = CustomerConstant.ChargeDeductWay.PAY_MONTH_CNT.getPayWayDesc() + useDetailed.getDeductCnt()+  "次";
                historyInfo.setPayWayDesc(desc);
            }
        } else if (SchemeConstant.SchemePayCategory.IC_RECHARGE.getType().equals(payCategory)) {
            if (greateThanZero) {
                // 有退款
                BigDecimal payMoney = MathUtil.sub(useDetailed.getDeductMoney(), refundMoney);
                String desc = CustomerConstant.ChargeDeductWay.IC_CARD_BALANCE.getPayWayDesc() + payMoney.toString() +  "元";
                historyInfo.setPayWayDesc(desc);
            } else {
                String desc = CustomerConstant.ChargeDeductWay.IC_CARD_BALANCE.getPayWayDesc() + useDetailed.getDeductMoney() +  "元";
                historyInfo.setPayWayDesc(desc);
            }
        }
    }

    @Override
    public PageData<PayHistoryInfo> queryPayHistory(PayHistoryVo historyVo) {
        log.info("客户支付历史记录查询PayHistoryVo={}",JSON.toJSONString(historyVo));
        PageData<CustomerPay> tempPageData = iCustomerPayService.queryPayHistorys(historyVo.getCustomerGuid(), historyVo.getPageNumber(), historyVo.getPageSize());
        List<CustomerPay> list = tempPageData.getList();
        PageData<PayHistoryInfo> pageData = new PageData<>();
        if (CollectionUtils.isEmpty(list)) {
            pageData.setTotal(0L);
            pageData.setList(new ArrayList<>());
            return pageData;
        }

        List<PayHistoryInfo> dataList = Lists.newArrayList();
        for (CustomerPay customerPay : list) {
            PayHistoryInfo historyInfo = new PayHistoryInfo();
            Integer payCategory = customerPay.getPayCategory();
            if (SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)) {
                historyInfo.setPayCategoryDesc("月卡包月");
                String expireDate = DateUtil.formatDay(customerPay.getExpireTime());
                historyInfo.setExpireTime(expireDate);
            } else {
                historyInfo.setPayCategoryDesc("账户充值");
            }
            historyInfo.setPayCategory(customerPay.getPayCategory());
            historyInfo.setPayMoney(customerPay.getPayMoney());
            historyInfo.setRemainAmount(customerPay.getAfterRemainAmount());
            String payDate = DateUtil.formatDay(customerPay.getCreateTime());
            historyInfo.setPayDate(payDate);
            historyInfo.setPayWay(customerPay.getPayWay());
            StringBuffer sb = new StringBuffer();
            Integer payWay = customerPay.getPayWay();
            String descByType = CustomerConstant.PayWayConstant.getDescByType(payWay);
            BigDecimal payMoney = customerPay.getPayMoney();
            BigDecimal accountDeductMoney = customerPay.getAccountDeductMoney();
            sb.append(descByType).append(payMoney).append("元");
            if (accountDeductMoney != null) {
                if (MathUtil.isGreateThanZero(accountDeductMoney)) {
                    sb.append(",余额支付").append(accountDeductMoney).append("元");
                }
            }
            historyInfo.setPayWayDesc(sb.toString());
            dataList.add(historyInfo);
        }

        pageData.setTotal(tempPageData.getTotal());
        pageData.setList(dataList);
        return pageData;
    }

    @Override
    public PageData<MessageListInfo> queryMessageListInfo(MessageListVo listVo) {
        log.info("客户充电日志消息列表查询MessageListVo={}",JSON.toJSONString(listVo));
        MessageListQueryDto queryDto = new MessageListQueryDto();
        queryDto.setCustomerGuid(listVo.getCustomerGuid());
        queryDto.setPageIndex(listVo.getPageNumber() - 1);
        queryDto.setPageSize(listVo.getPageSize());
        List<MessageListDto> messageList = deviceLogMapper.queryLogMessageList(queryDto);
        List<MessageListInfo> dataList = Lists.newArrayList();
        PageData<MessageListInfo> pageData = new PageData<>();
        if (CollectionUtils.isEmpty(messageList)) {
            pageData.setTotal(0L);
            pageData.setList(dataList);
            return pageData;
        }

        Set<String> set = Sets.newHashSet();
        for (MessageListDto dto : messageList) {
            set.add(dto.getChargingPlieGuid());
        }

        List<String> pileGuids = Lists.newArrayList(set);
        List<DeviceVo> deviceVos = deviceService.queryDevices(pileGuids);
        Map<String,DeviceVo> deviceMap = Maps.newHashMap();
        for (DeviceVo deviceVo : deviceVos) {
            deviceMap.put(deviceVo.getChargingPlieGuid(),deviceVo);
        }

        for (MessageListDto dto : messageList) {
            MessageListInfo info = new MessageListInfo();
            DeviceVo deviceVo = deviceMap.get(dto.getChargingPlieGuid());
            info.setDeviceNo(deviceVo.getDeviceNo() + deviceVo.getPort());
            info.setMessageContent(dto.getSmsContent());
            Integer eventCode = dto.getEventCode();
            info.setMessageType(eventCode);
            String createTime = DateUtil.formatDate(dto.getCreateTime());
            info.setCreateTime(createTime);
            String descByType = CustomerConstant.MessageType.getDescByType(eventCode);
            info.setMessageTypeDesc(descByType);
            info.setChargingGuid(dto.getChargingGuid());

            String date = DateUtil.formatDay(dto.getCreateTime());
            info.setDate(date);
            String time = DateUtil.formatDate(dto.getCreateTime());
            info.setTime(time);
            dataList.add(info);
        }

        // 查询总数
        Long total = deviceLogMapper.queryLogMessageListCount(queryDto);
        pageData.setTotal(total);
        pageData.setList(dataList);
        return pageData;
    }

    @Override
    public LatestChargeOrderInfo queryLatestChargeRecord(String customerGuid) {
        log.info("客户最近一次开电记录查询customerGuid={}",customerGuid);
        DeviceUseDetailed useDetailed = iDeviceUseDetailedService.queryLatestChargeOrder(customerGuid);
        if (useDetailed == null) {
            return null;
        }

        LatestChargeOrderInfo recordInfo = new LatestChargeOrderInfo();
        recordInfo.setChargingGuid(useDetailed.getChargingGuid());
        recordInfo.setChargingPlieGuid(useDetailed.getChargingPlieGuid());
        String startTime = DateUtil.formatDate(useDetailed.getStartTime());
        recordInfo.setStartTime(startTime);
        DeviceLog deviceLog = iDeviceLogService.selectByPrimaryKey(useDetailed.getDevLogId());
        Integer eventCode = deviceLog.getEventCode();
        if (eventCode == -2) {
            // 表示异常导致充电结束
            recordInfo.setIsEvent(0);
            recordInfo.setEventCodeDesc(deviceLog.getSmsContent());
            recordInfo.setEventCode(deviceLog.getEventCode());
        } else if (eventCode == -1) {
            // 正常结束
            recordInfo.setIsEvent(1);
            recordInfo.setEventCodeDesc(deviceLog.getSmsContent());
            recordInfo.setEventCode(deviceLog.getEventCode());
        } else {
            // 正常情况，代码不可能执行到此处
            recordInfo.setEventCodeDesc(deviceLog.getSmsContent());
            recordInfo.setIsEvent(1);
            recordInfo.setEventCode(deviceLog.getEventCode());
            log.info("代码执行到此处，系统存在bug");
        }
        return recordInfo;
    }

    @Override
    public WechatPayOrderInfo wechatBalanceRecharge(WxBalanceRechargeVo rechargeVo) {
        log.info("微信发起账户余额充值WxBalanceRechargeVo={}",JSON.toJSONString(rechargeVo));
        // 查询方案
        String schemeGuid = rechargeVo.getSchemeGuid();
        PaySchemeVo paySchemeVo = paySchemeService.queryPaySchemeBySchemeGuid(schemeGuid);

        // 统一下单
        WxUnifiedOrderParam orderParam = new WxUnifiedOrderParam();
        orderParam.setBody("充电用户账户余额充值");
        String outTradeNo = UuidUtil.getUuid();
        orderParam.setOutTradeNo(outTradeNo);
        BigDecimal payMoney = paySchemeVo.getPayMoney();
        Integer totalFee = MathUtil.yuan2Fen(String.valueOf(payMoney));
        orderParam.setTotalFee(totalFee);
        orderParam.setSpbillCreateIp(rechargeVo.getIp());
        // TODO 配置文件中获取
        orderParam.setNotifyUrl("");
        orderParam.setTradeType("JSAPI");
        String openId = rechargeVo.getOpenId();
        orderParam.setOpenId(openId);
        Map<String, String> orderMap = wechatService.unifiedOrder(orderParam);
        WechatPayOrderInfo orderInfo = new WechatPayOrderInfo();
        if (!orderMap.isEmpty()) {
            // 插入支付记录
            CustomerPay customerPay = new CustomerPay();
            String customerGuid = rechargeVo.getCustomerGuid();
            customerPay.setCustomerGuid(customerGuid);
            customerPay.setPayMoney(payMoney);
            customerPay.setAccountChargeMoney(paySchemeVo.getRealMoney());
            customerPay.setSchemeGuid(schemeGuid);
            customerPay.setAccountDeductMoney(BigDecimal.ZERO);
            customerPay.setChargingTime(paySchemeVo.getChargingTime());
            customerPay.setPayCategory(paySchemeVo.getPayCategory());
            customerPay.setWebcharNo(openId);
            customerPay.setCreateTime(new Date());
            customerPay.setPayFlag(outTradeNo);
            customerPay.setPayState(0);
            customerPay.setSerialNum(DateUtil.getSerialNum());

            AccountQueryVo queryVo = new AccountQueryVo();
            queryVo.setUserGuid(customerGuid);
            queryVo.setAccountCode(AccountDictEnum.CUSTOMER_AVAILABLE.getAccountCode());
            UserAccountInfo accountInfo = accountService.queryAccountInfo(queryVo);
            // 充值后剩余金额，此处是先将充值前剩余金额赋值上，避免用户点击支付，然后又取消了，导致支付失败的订单上该字段为null
            // 充值成功后，在回调处还会更新此字段
            customerPay.setAfterRemainAmount(accountInfo.getRemainAmount());
            customerPay.setType(CustomerConstant.PayScene.ACTIVITY_CHARGE.getType());
            customerPay.setPayWay(CustomerConstant.PayWayConstant.WECHAT.getType());
            iCustomerPayService.insertSelective(customerPay);

            //设置微信下单参数
            orderInfo.setAppId(orderMap.get("appId"));
            orderInfo.setNonceStr(orderMap.get("nonceStr"));
            orderInfo.setTimeStamp(orderMap.get("timeStamp"));
            orderInfo.setPackages(orderMap.get("package"));
            orderInfo.setPaySign(orderMap.get("paySign"));
            orderInfo.setSignType(orderMap.get("signType"));
        }
        return orderInfo;
    }

    @Override
    public String alipayBalanceRecharge(ZfbBalanceRechargeVo rechargeVo) {
        log.info("支付宝发起账户余额充值ZfbBalanceRechargeVo={}",JSON.toJSONString(rechargeVo));
        // 查询方案
        String schemeGuid = rechargeVo.getSchemeGuid();
        PaySchemeVo paySchemeVo = paySchemeService.queryPaySchemeBySchemeGuid(schemeGuid);

        TradeCreateParam param = new TradeCreateParam();
        String outTradeNo = UuidUtil.getUuid();
        param.setOutTradeNo(outTradeNo);
        BigDecimal payMoney = paySchemeVo.getPayMoney();
        param.setTotalAmount(String.valueOf(payMoney));
        param.setSubject("充电用户账户余额充值");
        param.setBuyerId(rechargeVo.getAlipayUserId());
        String tradeNo = alipayService.tradeCreate(param);
        if (StringUtils.isNotBlank(tradeNo)) {
            // 插入支付记录
            CustomerPay customerPay = new CustomerPay();
            String customerGuid = rechargeVo.getCustomerGuid();
            customerPay.setCustomerGuid(customerGuid);
            customerPay.setWebcharNo(rechargeVo.getAlipayUserId());
            customerPay.setSchemeGuid(schemeGuid);
            customerPay.setPayCategory(paySchemeVo.getPayCategory());
            customerPay.setPayMoney(payMoney);
            customerPay.setAccountChargeMoney(paySchemeVo.getRealMoney());
            customerPay.setAccountDeductMoney(BigDecimal.ZERO);
            customerPay.setChargingTime(paySchemeVo.getChargingTime());
            customerPay.setSerialNum(DateUtil.getSerialNum());
            customerPay.setPayFlag(outTradeNo);
            customerPay.setCreateTime(new Date());
            customerPay.setPayState(0);

            AccountQueryVo queryVo = new AccountQueryVo();
            queryVo.setUserGuid(customerGuid);
            queryVo.setAccountCode(AccountDictEnum.CUSTOMER_AVAILABLE.getAccountCode());
            UserAccountInfo accountInfo = accountService.queryAccountInfo(queryVo);
            // 充值后剩余金额，此处是先将充值前剩余金额赋值上，避免用户点击支付，然后又取消了，导致支付失败的订单上该字段为null
            // 充值成功后，在回调处还会更新此字段
            customerPay.setAfterRemainAmount(accountInfo.getRemainAmount());
            customerPay.setType(CustomerConstant.PayScene.ACTIVITY_CHARGE.getType());
            customerPay.setPayWay(CustomerConstant.PayWayConstant.ALIPAY.getType());
            iCustomerPayService.insertSelective(customerPay);
        }
        return tradeNo;
    }

    @Override
    public ChargeCompleteInfo queryChargeCompleteInfo(String chargingGuid) {
        DeviceUseDetailed useDetailed = iDeviceUseDetailedService.queryChargeOrder(chargingGuid);

        //设置选择充电的套餐类型
        ChargeCompleteInfo completeInfo = new ChargeCompleteInfo();
        this.setCompleteParams(useDetailed,completeInfo);
        Integer devLogId = useDetailed.getDevLogId();
        if (devLogId != null) {
            DeviceLog deviceLog = iDeviceLogService.selectByPrimaryKey(devLogId);
            Integer eventCode = deviceLog.getEventCode();
            if (eventCode == -1) {
                // 手动结束充电
                completeInfo.setIsEvent(0);
            } else if (eventCode == -2) {
                // 异常结束
                //设置异常
                completeInfo.setIsEvent(1);
                completeInfo.setEventCodeDesc(deviceLog.getSmsContent());
            } else {
                // 正常情况，代码不可能进入此处
                completeInfo.setIsEvent(0);
                log.info("代码执行到此处，系统存在bug");
            }
        } else {
            completeInfo.setIsEvent(0);
        }

        return completeInfo;
    }

    private void setCompleteParams(DeviceUseDetailed useDetailed, ChargeCompleteInfo completeInfo){
        BigDecimal refundMoney = useDetailed.getRefundMoney();
        boolean flag = MathUtil.isGreateThanZero(refundMoney);

        Integer payCategory = useDetailed.getPayCategory();
        completeInfo.setPayCategory(payCategory);
        if(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getType().equals(payCategory)){
            completeInfo.setPayCategoryDesc(SchemeConstant.SchemePayCategory.TEMPORARY_RECHARGE.getDesc());
            //设置充电金额
            if (flag) {
                BigDecimal sub = MathUtil.sub(useDetailed.getDeductMoney(), refundMoney);
                completeInfo.setPayMoney(sub);
            } else {
                completeInfo.setPayMoney(useDetailed.getDeductMoney());
            }

            //设置剩余金额
            completeInfo.setRemainAmount(useDetailed.getAfterRemainAmount());
        } else if(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)){
            completeInfo.setPayCategoryDesc(SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getDesc());
            //设置扣除次数
            if (flag) {
                completeInfo.setUseCnt(0);
            } else {
                completeInfo.setUseCnt(useDetailed.getDeductCnt());
            }

            //设置剩余次数
            completeInfo.setRemainCnt(useDetailed.getAfterRemainCnt());
        } else if(SchemeConstant.SchemePayCategory.RECHARGE_FULL.getType().equals(payCategory)){
            completeInfo.setPayCategoryDesc(SchemeConstant.SchemePayCategory.RECHARGE_FULL.getDesc());
            //设置充电金额
            if (flag) {
                BigDecimal sub = MathUtil.sub(useDetailed.getDeductMoney(), refundMoney);
                completeInfo.setPayMoney(sub);
            } else {
                completeInfo.setPayMoney(useDetailed.getDeductMoney());
            }
            //设置剩余金额
            completeInfo.setRemainAmount(useDetailed.getAfterRemainAmount());
        } else if(SchemeConstant.SchemePayCategory.IC_RECHARGE.getType().equals(payCategory)) {
            completeInfo.setPayCategoryDesc(SchemeConstant.SchemePayCategory.IC_RECHARGE.getDesc());
            //设置充电金额
            if (flag) {
                BigDecimal sub = MathUtil.sub(useDetailed.getDeductMoney(), refundMoney);
                completeInfo.setPayMoney(sub);
            } else {
                completeInfo.setPayMoney(useDetailed.getDeductMoney());
            }
            //设置剩余金额
            completeInfo.setRemainAmount(useDetailed.getAfterRemainAmount());
        }
        //设置充电时长
        String chargeTime = DateUtil.subTime(useDetailed.getEndTime(), useDetailed.getStartTime());
        completeInfo.setChargeTime(chargeTime);

        //设置开始充电时间
        completeInfo.setStartTime(DateUtil.formatDate(useDetailed.getStartTime()));
        //设置结束充电时间
        completeInfo.setEndTime(DateUtil.formatDate(useDetailed.getEndTime()));
        //设置联系电话
        DeviceDetailVo deviceDetailVo = deviceService.queryDeviceDetail(useDetailed.getChargingPlieGuid());
        completeInfo.setDeviceNo(deviceDetailVo.getDeviceNo() + deviceDetailVo.getPort());
        // TODO
        ProjectVo projectVo = projectService.queryProjectDetail("");
        completeInfo.setContact(projectVo.getContactPhone());
    }


    @Override
    public WxMonthRechargeInfo wechatBuyMonthCard(WxBuyMonthCardVo cardVo) {
        log.info("客户微信月卡购买WxBuyMonthCardVo={}",JSON.toJSONString(cardVo));
        //查询价格方案
        String schemeGuid = cardVo.getSchemeGuid();
        PaySchemeVo schemeVo = paySchemeService.queryPaySchemeBySchemeGuid(schemeGuid);
        Integer payCategory = schemeVo.getPayCategory();
        if (!SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)) {
            throw new BusinessException("价格方案id错误");
        }

        MonthPreRechargeVo preRechargeVo = new MonthPreRechargeVo();
        String customerGuid = cardVo.getCustomerGuid();
        preRechargeVo.setUserGuid(customerGuid);
        String outTradeNo = UuidUtil.getUuid();
        preRechargeVo.setDataGuid(outTradeNo);
        preRechargeVo.setSignGuid(UuidUtil.getUuid());
        preRechargeVo.setPayMoney(schemeVo.getRealMoney());
        MonthPreRechargeInfo preRechargeInfo = monthAccountService.preRecharge(preRechargeVo);
        BigDecimal thirdPayMoney = preRechargeInfo.getThirdPayMoney();
        boolean greateThanZero = MathUtil.isGreateThanZero(thirdPayMoney);
        String openId = cardVo.getOpenId();
        WxMonthRechargeInfo rechargeInfo = new WxMonthRechargeInfo();
        if (greateThanZero) {
            // 统一下单
            WxUnifiedOrderParam orderParam = new WxUnifiedOrderParam();
            orderParam.setBody("充电客户月卡购买");
            orderParam.setOutTradeNo(outTradeNo);
            Integer totalFee = MathUtil.yuan2Fen(String.valueOf(thirdPayMoney));
            orderParam.setTotalFee(totalFee);
            orderParam.setSpbillCreateIp(cardVo.getIp());
            // TODO 配置文件中获取
            orderParam.setNotifyUrl("");
            orderParam.setTradeType("JSAPI");
            orderParam.setOpenId(openId);
            Map<String, String> orderMap = wechatService.unifiedOrder(orderParam);
            if (!orderMap.isEmpty()) {
                // 插入支付记录
                CustomerPay customerPay = new CustomerPay();
                customerPay.setCustomerGuid(customerGuid);
                customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.MONTH_CARD_PAY.getType());
                customerPay.setWebcharNo(openId);
                customerPay.setProjectGuid(schemeVo.getProjectGuid());
                customerPay.setSchemeGuid(schemeGuid);
                customerPay.setPayCategory(payCategory);
                customerPay.setNumMonths(1);
                customerPay.setBuyCnt(schemeVo.getChargingCnt());
                Date expireDate = DateUtil.getNextMonthDate();
                customerPay.setExpireTime(expireDate);
                customerPay.setPayMoney(thirdPayMoney);
                customerPay.setAccountChargeMoney(BigDecimal.ZERO);
                customerPay.setAccountDeductMoney(preRechargeInfo.getBalancePayMoney());
                customerPay.setChargingTime(schemeVo.getChargingTime());
                customerPay.setSerialNum(DateUtil.getSerialNum());
                customerPay.setPayFlag(outTradeNo);
                customerPay.setCreateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
                customerPay.setAfterRemainAmount(preRechargeInfo.getRemainAmount());
                customerPay.setType(CustomerConstant.PayScene.MONTH_CHARGE.getType());
                customerPay.setPayWay(CustomerConstant.PayWayConstant.WECHAT.getType());
                iCustomerPayService.insertSelective(customerPay);

                //设置微信下单参数
                rechargeInfo.setIsPay(1);
                rechargeInfo.setAppId(orderMap.get("appId"));
                rechargeInfo.setNonceStr(orderMap.get("nonceStr"));
                rechargeInfo.setTimeStamp(orderMap.get("timeStamp"));
                rechargeInfo.setPackages(orderMap.get("package"));
                rechargeInfo.setPaySign(orderMap.get("paySign"));
                rechargeInfo.setSignType(orderMap.get("signType"));
            }
        } else {
            rechargeInfo.setIsPay(0);

            // 纯余额购买月卡，生成余额支付订单
            CustomerPay customerPay = new CustomerPay();
            customerPay.setCustomerGuid(customerGuid);
            customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.MONTH_CARD_PAY.getType());
            customerPay.setWebcharNo(openId);
            customerPay.setProjectGuid(schemeVo.getProjectGuid());
            customerPay.setSchemeGuid(schemeGuid);
            customerPay.setPayCategory(payCategory);
            customerPay.setNumMonths(1);
            Integer chargingCnt = schemeVo.getChargingCnt();
            customerPay.setBuyCnt(chargingCnt);
            Date expireDate = DateUtil.getNextMonthDate();
            customerPay.setExpireTime(expireDate);
            BigDecimal balancePayMoney = preRechargeInfo.getBalancePayMoney();
            customerPay.setPayMoney(balancePayMoney);
            customerPay.setAccountChargeMoney(BigDecimal.ZERO);
            customerPay.setAccountDeductMoney(balancePayMoney);
            customerPay.setChargingTime(schemeVo.getChargingTime());
            customerPay.setSerialNum(DateUtil.getSerialNum());
            customerPay.setPayFlag(outTradeNo);
            customerPay.setCreateTime(new Date());
            customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
            customerPay.setAfterRemainAmount(preRechargeInfo.getRemainAmount());
            customerPay.setType(CustomerConstant.PayScene.MONTH_CHARGE.getType());
            customerPay.setPayWay(CustomerConstant.PayWayConstant.WECHAT.getType());
            iCustomerPayService.insertSelective(customerPay);

            //调用账户月卡购买确认接口
            MonthConfirmRechargeVo confirmVo = new MonthConfirmRechargeVo();
            confirmVo.setUserGuid(customerGuid);
            confirmVo.setDataGuid(outTradeNo);
            confirmVo.setSignGuid(UuidUtil.getUuid());
            confirmVo.setThirdPayMoney(BigDecimal.ZERO);
            confirmVo.setBalancePayMoney(balancePayMoney);
            confirmVo.setSchemeGuid(schemeGuid);
            confirmVo.setChargeCnt(chargingCnt);
            confirmVo.setExpireTime(expireDate);
            monthAccountService.confirmRecharge(confirmVo);
        }
        return rechargeInfo;
    }

    @Override
    public AlipayMonthRechargeInfo alipayBuyMonthCard(ZfbBuyMonthCardVo cardVo) {
        log.info("客户支付宝月卡购买ZfbBuyMonthCardVo={}",JSON.toJSONString(cardVo));
        //查询价格方案
        String schemeGuid = cardVo.getSchemeGuid();
        PaySchemeVo schemeVo = paySchemeService.queryPaySchemeBySchemeGuid(schemeGuid);
        Integer payCategory = schemeVo.getPayCategory();
        if (!SchemeConstant.SchemePayCategory.MONTH_RECHARGE.getType().equals(payCategory)) {
            throw new BusinessException("价格方案id错误");
        }

        MonthPreRechargeVo preRechargeVo = new MonthPreRechargeVo();
        String customerGuid = cardVo.getCustomerGuid();
        preRechargeVo.setUserGuid(customerGuid);
        String outTradeNo = UuidUtil.getUuid();
        preRechargeVo.setDataGuid(outTradeNo);
        preRechargeVo.setSignGuid(UuidUtil.getUuid());
        preRechargeVo.setPayMoney(schemeVo.getRealMoney());
        MonthPreRechargeInfo preRechargeInfo = monthAccountService.preRecharge(preRechargeVo);
        BigDecimal thirdPayMoney = preRechargeInfo.getThirdPayMoney();
        boolean greateThanZero = MathUtil.isGreateThanZero(thirdPayMoney);
        String alipayUserId = cardVo.getAlipayUserId();
        AlipayMonthRechargeInfo rechargeInfo = new AlipayMonthRechargeInfo();
        if (greateThanZero) {
            // 统一下单
            TradeCreateParam param = new TradeCreateParam();
            param.setOutTradeNo(outTradeNo);
            param.setTotalAmount(String.valueOf(thirdPayMoney));
            param.setSubject("充电用户账户余额充值");
            param.setBuyerId(alipayUserId);
            String tradeNo = alipayService.tradeCreate(param);
            if (StringUtils.isNotBlank(tradeNo)) {
                // 插入支付记录
                CustomerPay customerPay = new CustomerPay();
                customerPay.setCustomerGuid(customerGuid);
                customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.MONTH_CARD_PAY.getType());
                customerPay.setWebcharNo(alipayUserId);
                customerPay.setProjectGuid(schemeVo.getProjectGuid());
                customerPay.setSchemeGuid(schemeGuid);
                customerPay.setPayCategory(payCategory);
                customerPay.setNumMonths(1);
                customerPay.setBuyCnt(schemeVo.getChargingCnt());
                Date expireDate = DateUtil.getNextMonthDate();
                customerPay.setExpireTime(expireDate);
                customerPay.setPayMoney(thirdPayMoney);
                customerPay.setAccountChargeMoney(BigDecimal.ZERO);
                customerPay.setAccountDeductMoney(preRechargeInfo.getBalancePayMoney());
                customerPay.setChargingTime(schemeVo.getChargingTime());
                customerPay.setSerialNum(DateUtil.getSerialNum());
                customerPay.setPayFlag(outTradeNo);
                customerPay.setCreateTime(new Date());
                customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
                customerPay.setAfterRemainAmount(preRechargeInfo.getRemainAmount());
                customerPay.setType(CustomerConstant.PayScene.MONTH_CHARGE.getType());
                customerPay.setPayWay(CustomerConstant.PayWayConstant.ALIPAY.getType());
                iCustomerPayService.insertSelective(customerPay);
            }
        } else {
            rechargeInfo.setIsPay(0);

            // 纯余额购买月卡，生成余额支付订单
            CustomerPay customerPay = new CustomerPay();
            customerPay.setCustomerGuid(customerGuid);
            customerPay.setBusinessType(CustomerConstant.PayOrderBusinessType.MONTH_CARD_PAY.getType());
            customerPay.setWebcharNo(alipayUserId);
            customerPay.setProjectGuid(schemeVo.getProjectGuid());
            customerPay.setSchemeGuid(schemeGuid);
            customerPay.setPayCategory(payCategory);
            customerPay.setNumMonths(1);
            Integer chargingCnt = schemeVo.getChargingCnt();
            customerPay.setBuyCnt(chargingCnt);
            Date expireDate = DateUtil.getNextMonthDate();
            customerPay.setExpireTime(expireDate);
            BigDecimal balancePayMoney = preRechargeInfo.getBalancePayMoney();
            customerPay.setPayMoney(balancePayMoney);
            customerPay.setAccountChargeMoney(BigDecimal.ZERO);
            customerPay.setAccountDeductMoney(balancePayMoney);
            customerPay.setChargingTime(schemeVo.getChargingTime());
            customerPay.setSerialNum(DateUtil.getSerialNum());
            customerPay.setPayFlag(outTradeNo);
            customerPay.setCreateTime(new Date());
            customerPay.setPayState(CustomerConstant.PayState.WAIT_PAY.getState());
            customerPay.setAfterRemainAmount(preRechargeInfo.getRemainAmount());
            customerPay.setType(CustomerConstant.PayScene.MONTH_CHARGE.getType());
            customerPay.setPayWay(CustomerConstant.PayWayConstant.BALANCE.getType());
            iCustomerPayService.insertSelective(customerPay);

            //调用账户月卡购买确认接口
            MonthConfirmRechargeVo confirmVo = new MonthConfirmRechargeVo();
            confirmVo.setUserGuid(customerGuid);
            confirmVo.setDataGuid(outTradeNo);
            confirmVo.setSignGuid(UuidUtil.getUuid());
            confirmVo.setThirdPayMoney(BigDecimal.ZERO);
            confirmVo.setBalancePayMoney(balancePayMoney);
            confirmVo.setSchemeGuid(schemeGuid);
            confirmVo.setChargeCnt(chargingCnt);
            confirmVo.setExpireTime(expireDate);
            monthAccountService.confirmRecharge(confirmVo);
        }
        return rechargeInfo;
    }


    @Override
    public boolean register(RegisterVo registerVo) {
        log.info("客户注册RegisterVo={}",JSON.toJSONString(registerVo));
        //验证验证码是否正确
        String phoneNumber = registerVo.getPhoneNumber();
        String verificationCode = registerVo.getVerificationCode();
        boolean flag = smsService.verifySmsCode(phoneNumber, verificationCode);
        if (!flag) {
            // 验证码校验不通过
            return flag;
        }

        Integer appType = registerVo.getAppType();
        String nickName = registerVo.getNickName();
        String openId = registerVo.getOpenId();
        CustomerInfo customerInfo = iCustomerInfoService.queryByCustomerContact(phoneNumber);
        if (customerInfo == null) {
            // 手机号第一次注册
            if (CustomerConstant.AppType.WECHAT.getType().equals(appType)) {
                CustomerInfo info = new CustomerInfo();
                info.setCustomerName(nickName);
                info.setWebcharNo(openId);
                info.setCustomerGuid(UuidUtil.getUuid());
                info.setCustomerContact(phoneNumber);
                info.setCreateTime(new Date());
                iCustomerInfoService.insertSelective(info);

                // 生成对应账户
                accountService.createAccount(info.getCustomerGuid(),AccountTypeEnum.CUSTOMER.getType());
            } else {
                CustomerInfo info = new CustomerInfo();
                info.setAlipayNickName(nickName);
                info.setAlipayUserId(openId);
                info.setCustomerGuid(UuidUtil.getUuid());
                info.setCustomerContact(phoneNumber);
                info.setCreateTime(new Date());
                iCustomerInfoService.insertSelective(info);

                // 生成对应账户
                accountService.createAccount(info.getCustomerGuid(),AccountTypeEnum.CUSTOMER.getType());
            }

        } else {
            if (CustomerConstant.AppType.WECHAT.getType().equals(appType)) {
                // 更新账户绑定微信信息
                customerInfo.setWebcharNo(openId);
                customerInfo.setCustomerName(nickName);
                customerInfo.setUpdateTime(new Date());
                iCustomerInfoService.updateByPrimaryKeySelective(customerInfo);
            } else {
                // 更新账户绑定支付宝信息
                customerInfo.setAlipayUserId(openId);
                customerInfo.setAlipayNickName(nickName);
                customerInfo.setUpdateTime(new Date());
                iCustomerInfoService.updateByPrimaryKeySelective(customerInfo);
            }
        }

        return flag;
    }

    @Override
    public boolean updateCustomerPhone(String customerGuid, String mobilePhone,String validateCode) {
        log.info("设置客户是否接口短信customerGuid={},mobilePhone={}",customerGuid,mobilePhone);
        CustomerInfo customerInfo = iCustomerInfoService.queryByCustomerGuid(customerGuid);
        boolean flag = smsService.verifySmsCode(customerInfo.getCustomerContact(), validateCode);
        if (!flag) {
            return flag;
        }

        // 更新
        CustomerInfo param = new CustomerInfo();
        param.setCustomerGuid(customerGuid);
        param.setCustomerContact(mobilePhone);
        int i = iCustomerInfoService.updateSelectiveByCustomerGuid(param);
        if (i <= 0) {
            throw new BusinessException("更新失败");
        }
        return flag;
    }

    @Override
    public void updateByCustomerGuid(CustomerUpdateVo updateVo) {
        log.info("设置客户是否接口短信CustomerUpdateVo={}",JSON.toJSONString(updateVo));
        CustomerInfo param = new CustomerInfo();
        BeanUtils.copyProperties(updateVo,param);
        int i = iCustomerInfoService.updateSelectiveByCustomerGuid(param);
        if (i <= 0) {
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public WithdrawCashInfo queryWithdrawCashInfo(Integer appType,String customerGuid) {
        log.info("查询客户提现页面信息customerGuid={}",customerGuid);
        CustomerInfo customerInfo = iCustomerInfoService.queryByCustomerGuid(customerGuid);

        WithdrawCashInfo cashInfo = new WithdrawCashInfo();
        if (CustomerConstant.AppType.WECHAT.getType().equals(appType)) {
            String alipayUserId = customerInfo.getAlipayUserId();
            if (StringUtils.isNotBlank(alipayUserId)) {
                cashInfo.setHasAccount(1);
                cashInfo.setUserId(alipayUserId);
            }
        } else {
            String webcharNo = customerInfo.getWebcharNo();
            if (StringUtils.isNotBlank(webcharNo)) {
                cashInfo.setHasAccount(1);
                cashInfo.setUserId(webcharNo);
            }
        }

        AccountQueryVo queryVo = new AccountQueryVo();
        queryVo.setUserGuid(customerGuid);
        queryVo.setAccountCode(AccountDictEnum.CUSTOMER_AVAILABLE.getAccountCode());
        UserAccountInfo accountInfo = accountService.queryAccountInfo(queryVo);
        cashInfo.setRemainAmount(accountInfo.getRemainAmount());
        cashInfo.setWithdrawCashAmount(accountInfo.getRemainAmount());
        return cashInfo;
    }

    @Override
    public WechatPayOrderInfo iccardRecharge(WxIcCardRechargeVo rechargeVo) {
        log.info("客户微信IC卡充值WxIcCardRechargeVo={}",JSON.toJSONString(rechargeVo));
        // 统一下单
        WxUnifiedOrderParam orderParam = new WxUnifiedOrderParam();
        orderParam.setBody("IC卡账户余额充值");
        String outTradeNo = UuidUtil.getUuid();
        orderParam.setOutTradeNo(outTradeNo);
        BigDecimal amount = rechargeVo.getAmount();
        Integer totalFee = MathUtil.yuan2Fen(String.valueOf(amount));
        orderParam.setTotalFee(totalFee);
        orderParam.setSpbillCreateIp(rechargeVo.getIp());
        // TODO 配置文件中获取
        orderParam.setNotifyUrl("");
        orderParam.setTradeType("JSAPI");
        String openId = rechargeVo.getOpenId();
        orderParam.setOpenId(openId);
        Map<String, String> orderMap = wechatService.unifiedOrder(orderParam);
        WechatPayOrderInfo orderInfo = new WechatPayOrderInfo();
        if (!orderMap.isEmpty()) {
            // 插入支付记录
            CustomerPayCard payCard = new CustomerPayCard();
            String customerGuid = rechargeVo.getCustomerGuid();
            payCard.setCustomerGuid(customerGuid);
            payCard.setProjectGuid(rechargeVo.getProjectGuid());
            payCard.setMerchantGuid(rechargeVo.getMerchantGuid());
            payCard.setPropertyGuid(rechargeVo.getPropertyGuid());
            payCard.setCardId(rechargeVo.getCardId());
            payCard.setPayMoney(amount);
            // TODO 查询IC卡账户余额
            payCard.setAfterRemainAmount(BigDecimal.ZERO);
            payCard.setSerialNum(DateUtil.getSerialNum());
            payCard.setPayFlag(outTradeNo);
            payCard.setPayWay(CustomerConstant.PayWayConstant.WECHAT.getType());
            payCard.setPayState(0);
            payCard.setCreateTime(new Date());
            iCustomerPayCardService.insertSelective(payCard);

            //设置微信下单参数
            orderInfo.setAppId(orderMap.get("appId"));
            orderInfo.setNonceStr(orderMap.get("nonceStr"));
            orderInfo.setTimeStamp(orderMap.get("timeStamp"));
            orderInfo.setPackages(orderMap.get("package"));
            orderInfo.setPaySign(orderMap.get("paySign"));
            orderInfo.setSignType(orderMap.get("signType"));
        }
        return orderInfo;
    }

    @Override
    public String alipayIccardRecharge(ZfbIcCardRechargeVo rechargeVo) {
        log.info("支付宝IC卡充值ZfbIcCardRechargeVo={}",rechargeVo);
        TradeCreateParam param = new TradeCreateParam();
        String outTradeNo = UuidUtil.getUuid();
        param.setOutTradeNo(outTradeNo);
        BigDecimal amount = rechargeVo.getAmount();
        param.setTotalAmount(String.valueOf(amount));
        param.setSubject("IC卡账户余额充值");
        param.setBuyerId(rechargeVo.getAlipayUserId());
        String tradeNo = alipayService.tradeCreate(param);
        if (StringUtils.isNotBlank(tradeNo)) {
            // 插入支付记录
            CustomerPayCard payCard = new CustomerPayCard();
            String customerGuid = rechargeVo.getCustomerGuid();
            payCard.setCustomerGuid(customerGuid);
            payCard.setProjectGuid(rechargeVo.getProjectGuid());
            payCard.setMerchantGuid(rechargeVo.getMerchantGuid());
            payCard.setPropertyGuid(rechargeVo.getPropertyGuid());
            payCard.setCardId(rechargeVo.getCardId());
            payCard.setPayMoney(amount);
            // TODO 查询IC卡账户余额
            payCard.setAfterRemainAmount(BigDecimal.ZERO);
            payCard.setSerialNum(DateUtil.getSerialNum());
            payCard.setPayFlag(outTradeNo);
            payCard.setPayWay(CustomerConstant.PayWayConstant.ALIPAY.getType());
            payCard.setPayState(0);
            payCard.setCreateTime(new Date());
            iCustomerPayCardService.insertSelective(payCard);
        }
        return tradeNo;
    }

    @Override
    public IcCardInfo queryIcCardInfo(String cardId) {
        log.info("查询IC卡使用记录、充值记录等信息cardId={}",cardId);

        IcCardInfo icCardInfo = new IcCardInfo();
        // 查询IC卡可用账户
        AccountQueryVo queryVo = new AccountQueryVo();
        queryVo.setUserGuid(cardId);
        queryVo.setAccountCode(AccountDictEnum.CUSTOMER_IC_CARD_AVAILABLE.getAccountCode());
        UserAccountInfo accountInfo = accountService.queryAccountInfo(queryVo);
        icCardInfo.setRemainAmount(accountInfo.getRemainAmount());

        // 最近3条使用记录
        List<DeviceUseDetailed> useDetaileds = iDeviceUseDetailedService.queryRecentNList(cardId, 3);
        List<IcCardUseInfo> useInfoList = this.getIcCardUseInfoList(useDetaileds);
        icCardInfo.setUseInfoList(useInfoList);

        // 最近3条充值记录
        List<CustomerPayCard> payCards = iCustomerPayCardService.queryRecentNList(cardId, 3);
        List<IcCardPayInfo> payInfoList = this.getIcCardPayInfoList(payCards);
        icCardInfo.setPayInfoList(payInfoList);
        return icCardInfo;
    }

    /**
     * 获取IC卡使用支付列表
     * @param payList
     * @return
     */
    private List<IcCardPayInfo> getIcCardPayInfoList(List<CustomerPayCard> payList) {
        List<IcCardPayInfo> payInfoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(payList)) {
            return payInfoList;
        }

        for (CustomerPayCard payCard : payList) {
            IcCardPayInfo payInfo = new IcCardPayInfo();
            payInfo.setCardId(payCard.getCardId());
            payInfo.setPayMoney(payCard.getPayMoney());
            String payMethod = CustomerConstant.PayWayConstant.getDescByType(payCard.getPayWay());
            payInfo.setPayMethod(payMethod);
            payInfo.setPayTime(payCard.getUpdateTime());
            payInfo.setAfterRemainAmount(payCard.getAfterRemainAmount());
            payInfoList.add(payInfo);
        }
        return payInfoList;
    }

    /**
     * 获取IC卡使用记录列表
     * @param useDetaileds
     * @return
     */
    private List<IcCardUseInfo> getIcCardUseInfoList(List<DeviceUseDetailed> useDetaileds) {
        List<IcCardUseInfo> useInfoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(useDetaileds)) {
            return useInfoList;
        }

        for (DeviceUseDetailed useDetailed : useDetaileds) {
            IcCardUseInfo useInfo = new IcCardUseInfo();
            useInfo.setCardId(useDetailed.getOpenNo());
            useInfo.setDeductMoney(useDetailed.getDeductMoney());
            useInfo.setBeginTime(useDetailed.getStartTime());
            useInfo.setEndTime(useDetailed.getEndTime());
            useInfo.setAfterRemainAmount(useDetailed.getAfterRemainAmount());
            useInfoList.add(useInfo);
        }

        return useInfoList;
    }

    @Override
    public PageData<IcCardUseInfo> queryIcCardUseInfo(CardUseQueryVo queryVo) {
        log.info("分页查询客户IC卡使用记录CardUseQueryVo={}",JSON.toJSONString(queryVo));

        Condition condition = new Condition(DeviceUseDetailed.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.orEqualTo("customerGuid",queryVo.getCustomerGuid());
        criteria.orEqualTo("customerGuid","");
        criteria.orIsNull("customerGuid");

        Example.Criteria criteria1 = condition.createCriteria();
        criteria1.andEqualTo("openNo",queryVo.getCardId());
        criteria1.andEqualTo("state",CustomerConstant.DeviceUsedRecordState.FINISH.getState());
        condition.and(criteria1);

        PageHelper.startPage(queryVo.getPageNumber(),queryVo.getPageSize(),"end_time desc");
        List<DeviceUseDetailed> list = deviceUseDetailedMapper.selectByCondition(condition);
        PageInfo<DeviceUseDetailed> pageInfo = new PageInfo(list);

        List<IcCardUseInfo> icCardUseInfo = this.getIcCardUseInfoList(list);
        PageData<IcCardUseInfo> pageData = new PageData<>();
        pageData.setTotal(pageInfo.getTotal());
        pageData.setList(icCardUseInfo);
        return pageData;
    }

    @Override
    public PageData<IcCardPayInfo> queryIcCardPayInfo(CardPayQueryVo queryVo) {
        log.info("分页查询客户IC卡支付记录CardPayQueryVo={}",JSON.toJSONString(queryVo));
        Condition condition = new Condition(CustomerPayCard.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.orEqualTo("customerGuid",queryVo.getCustomerGuid());
        criteria.orEqualTo("customerGuid","");
        criteria.orIsNull("customerGuid");

        Example.Criteria criteria1 = condition.createCriteria();
        criteria1.andEqualTo("cardId",queryVo.getCardId());
        criteria1.andEqualTo("payState",CustomerConstant.PayState.PAY_SUCCESS.getState());
        condition.and(criteria1);

        PageHelper.startPage(queryVo.getPageNumber(),queryVo.getPageSize(),"id desc");
        List<CustomerPayCard> payCards = customerPayCardMapper.selectByCondition(condition);
        PageInfo<CustomerPayCard> pageInfo = new PageInfo(payCards);

        List<IcCardPayInfo> icCardPayInfoList = this.getIcCardPayInfoList(payCards);
        PageData<IcCardPayInfo> pageData = new PageData<>();
        pageData.setTotal(pageInfo.getTotal());
        pageData.setList(icCardPayInfoList);
        return pageData;
    }

    @Override
    public List<ChargingIcCardInfo> queryChargingIcCardInfo(String customerGuid) {
        log.info(" 查询客户所有充电找中的IC卡列表customerGuid={}",customerGuid);

        // 查询所有充电中的记录
        DeviceUseDetailed param = new DeviceUseDetailed();
        param.setCustomerGuid(customerGuid);
        param.setOpenMeans(CustomerConstant.OpenMeansConstant.ICCARD.getType());
        param.setState(CustomerConstant.DeviceUsedRecordState.CHARGING.getState());
        List<DeviceUseDetailed> useDetaileds = deviceUseDetailedMapper.select(param);

        List<ChargingIcCardInfo> dataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(useDetaileds)) {
            return dataList;
        }

        for (DeviceUseDetailed useDetailed : useDetaileds) {
            ChargingIcCardInfo info = new ChargingIcCardInfo();
            info.setChargingGuid(useDetailed.getChargingGuid());
            info.setCardId(useDetailed.getOpenNo());
            dataList.add(info);
        }
        return dataList;
    }

}