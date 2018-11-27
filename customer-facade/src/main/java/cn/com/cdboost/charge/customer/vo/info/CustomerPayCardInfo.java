package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class CustomerPayCardInfo implements Serializable {
    private static final long serialVersionUID = 8394654615323305786L;
    /**
     * 标识id
     */
    private Integer id;

    /**
     * 客户唯一标识
     */
    private String customerGuid;

    /**
     * 项目标识
     */
    private String projectGuid;

    /**
     * 商户标识
     */
    private String merchantGuid;

    /**
     * 物业标识
     */
    private String propertyGuid;

    /**
     * ic卡编号
     */
    private String cardId;

    /**
     * 实际支付金额
     */
    private BigDecimal payMoney;

    /**
     * 充值后剩余金额
     */
    private BigDecimal afterRemainAmount;

    /**
     * 充值流水号
     */
    private String serialNum;

    private String payFlag;

    /**
     * 充值方式 1-微信 2-支付宝 3-现金 4-余额
     */
    private Integer payWay;

    /**
     * 支付状态 0-待支付 1-支付成功
     */
    private Integer payState;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}