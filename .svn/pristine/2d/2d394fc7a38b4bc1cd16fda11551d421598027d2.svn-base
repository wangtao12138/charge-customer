package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款参数
 */
@Getter
@Setter
public class RefundVo implements Serializable{
    private static final long serialVersionUID = 3797178535282855862L;

    /**
     * 待退款充电guid
     */
    private String chargingGuid;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款类别（0-系统自动退款，1-手动退款）
     */
    private Integer refundCategory;

    /**
     * 操作用户id
     */
    private Integer operateUserId;
}
