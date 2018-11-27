package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 支付宝充电时返回信息
 */
@Getter
@Setter
public class AlipayChargeInfo implements Serializable{
    private static final long serialVersionUID = -480564183918824459L;

    /**
     * 是否可充电 0标识余额不足，需要发起支付宝支付 1表示余额充足，后端直接扣减并发送充电指令
     */
    private Integer isCharge;

    /**
     * 当isCharge=0时，支付宝订单号
     */
    private String tradeNo;

    /**
     * 我方系统订单号
     */
    private String outTradeNo;
}
