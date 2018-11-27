package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * IC卡充值通用参数
 */
@Getter
@Setter
public class IcCardRechargeBaseVo implements Serializable{
    private static final long serialVersionUID = 9208061690649608696L;

    /**
     * 客户guid
     */
    private String customerGuid;

    /**
     * 商户guid
     */
    private String merchantGuid;

    /**
     * 物业guid
     */
    private String propertyGuid;

    /**
     * 站点guid
     */
    private String projectGuid;

    /**
     * IC卡卡号
     */
    private String cardId;

    /**
     * IC卡充值金额
     */
    private BigDecimal amount;
}
