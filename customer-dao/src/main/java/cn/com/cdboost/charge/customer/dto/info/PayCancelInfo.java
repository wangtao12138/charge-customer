package cn.com.cdboost.charge.customer.dto.info;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付取消返回支付相关信息
 */
@Getter
@Setter
public class PayCancelInfo {
    /**
     * 客户唯一标识
     */
    private String customerGuid;

    /**
     * 微信号
     */
    private String webcharNo;

    /**
     * 充值类别 1-购买次数  2-包月充值
     */
    private Integer payCategory;

    private String payFlag;

    /**
     * 支付状态 0-待支付 1-支付成功
     */
    private Integer payState;

    /**
     * 充值方式 1-微信 2-支付宝 3-现金 4-余额
     */
    private Integer payWay;
}
