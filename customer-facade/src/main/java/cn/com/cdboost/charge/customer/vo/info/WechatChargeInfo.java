package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 微信充电时返回信息
 */
@Getter
@Setter
public class WechatChargeInfo extends WechatPayOrderInfo{
    private static final long serialVersionUID = -5441332983172340329L;

    /**
     * 是否可充电
     * 0标识余额不足，需要发起微信支付
     * 1表示余额充足，后端直接扣减并发送充电指令
     */
    private Integer isCharge;

    /**
     * 微信支付的金额，单位元
     */
    private BigDecimal pay;

    /**
     * 我方系统订单号
     */
    private String outTradeNo;
}
