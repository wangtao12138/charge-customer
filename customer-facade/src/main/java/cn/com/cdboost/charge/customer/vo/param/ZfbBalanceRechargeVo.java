package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 账户余额充值接口
 */
@Getter
@Setter
public class ZfbBalanceRechargeVo implements Serializable {
    private static final long serialVersionUID = -4822398677726555197L;

    /**
     * 客户guid
     */
    private String customerGuid;

    /**
     * 方案guid
     */
    private String schemeGuid;

    /**
     * 支付宝用户id
     */
    private String alipayUserId;
}
