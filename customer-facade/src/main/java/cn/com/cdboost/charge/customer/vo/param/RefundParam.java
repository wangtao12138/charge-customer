package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 退款参数
 */

@Getter
@Setter
public class RefundParam {
    /**
     * 商户guid
     */
    private String merchantGuid;

    /**
     * 客户唯一标识
     */
    private String customerGuid;

    /**
     * 针对某次开电的退款
     */
    private String chargingGuid;

    /**
     * signGuid
     */
    private String signGuid;

    /**
     * 关联的支付订单号
     */
    private String tradeNo;

    /**
     * 待退款订单总金额
     */
    private BigDecimal totalMoney;

    /**
     * 本次退款金额
     */
    private BigDecimal refundMoney;

    /**
     * 退款类别（0-系统自动退款，1-手动退款）
     */
    private Integer refundCategory;

    /**
     * 退款备注
     */
    private String remark;

    /**
     * 退款操作用户id（0-系统自动退款）
     */
    private Integer operateUserId = 0;
}
