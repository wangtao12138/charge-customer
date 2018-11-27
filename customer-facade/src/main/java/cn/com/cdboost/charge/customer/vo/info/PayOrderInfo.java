package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户支付订单信息
 */
@Getter
@Setter
public class PayOrderInfo implements Serializable{
    private static final long serialVersionUID = 3920591287739265733L;

    /**
     * 标识id
     */
    private Integer id;

    /**
     * 客户唯一标识
     */
    private String customerGuid;

    /**
     * 微信号
     */
    private String webcharNo;

    /**
     * 针对哪种方案的购买
     */
    private String schemeGuid;

    /**
     * 充值类别 1-购买次数  2-包月充值
     */
    private Integer payCategory;

    /**
     * 充值包月数 pay_category=2时有效
     */
    private Integer numMonths;

    /**
     * 实际支付金额
     */
    private BigDecimal payMoney;

    /**
     * 账户余额充值金额（pay_category=4时跟pay_money可能不一致）
     */
    private BigDecimal accountChargeMoney;

    /**
     * 支付成功后，账户余额需要扣减金额
     */
    private BigDecimal accountDeductMoney;

    /**
     * 充值次数
     */
    private Integer buyCnt;

    /**
     * 0 标识不限 其他标识实时时长,单位小时
     */
    private Integer chargingTime;

    /**
     * 充值流水号
     */
    private String serialNum;

    private String payFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 支付状态 0-待支付 1-支付成功
     */
    private Integer payState;

    /**
     * 充值后剩余金额
     */
    private BigDecimal afterRemainAmount;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 支付类别（0充电时购买，1月卡页面购买，2活动页面购买）
     */
    private Integer type;

    /**
     * 充值方式 1-微信 2-支付宝 3-现金 4-余额
     */
    private Integer payWay;

    /**
     * ic卡编号
     */
    private String cardId;
}
