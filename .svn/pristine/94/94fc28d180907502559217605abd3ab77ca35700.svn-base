package cn.com.cdboost.charge.customer.dubbo;

import cn.com.cdboost.charge.customer.vo.param.RefundParam;
import cn.com.cdboost.charge.customer.vo.param.RefundVo;

/**
 * 退款服务
 */
public interface RefundService {
    /**
     * 发起退款申请
     * @param refundVo
     */
    void refundApply(RefundVo refundVo);

    /**
     * 开电失败退款
     */
    void cancelPayRefund(String chargingGuid,String signGuid);

    /**
     * 微信退款
     * @param refundParam
     * @return 1标识成功，0失败
     */
    Integer wxRefund(RefundParam refundParam);

    /**
     * 支付宝退款
     * @param refundParam
     * @return 1标识成功，0失败
     */
    Integer zfbRefund(RefundParam refundParam);
}
