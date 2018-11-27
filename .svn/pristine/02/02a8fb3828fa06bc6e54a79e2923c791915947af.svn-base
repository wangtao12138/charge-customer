package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户充值历史记录
 */
@Getter
@Setter
public class PayHistoryInfo implements Serializable{
    private static final long serialVersionUID = -6602715972438805877L;

    /**
     * 充值方式描述
     */
    private String payCategoryDesc;

    /**
     * 充值方式
     */
    private Integer payCategory;

    /**
     * 充值金额
     */
    private BigDecimal payMoney;

    /**
     * 账户余额
     */
    private BigDecimal remainAmount;

    /**
     * 包月用户有效日期
     */
    private String expireTime;

    /**
     * 支付日期
     */
    private String payDate;

    /**
     * 充值方式 1-微信 2-支付宝 3-现金
     */
    private Integer payWay;

    /**
     * 描述
     */
    private String payWayDesc;
}
