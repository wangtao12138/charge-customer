package cn.com.cdboost.charge.customer.vo.info;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * IC卡充值记录信息
 */
@Getter
@Setter
public class IcCardPayInfo implements Serializable{
    private static final long serialVersionUID = 3416338188059434392L;

    /**
     * IC卡号
     */
    private String cardId;

    /**
     * 充值金额
     */
    private BigDecimal payMoney;

    /**
     * 支付方式 1微信 2支付宝
     */
    private String payMethod;

    /**
     * 支付完成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 本次充值后剩余金额
     */
    private BigDecimal afterRemainAmount;
}
