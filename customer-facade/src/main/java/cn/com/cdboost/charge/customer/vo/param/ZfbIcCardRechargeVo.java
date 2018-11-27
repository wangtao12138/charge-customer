package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZfbIcCardRechargeVo extends IcCardRechargeBaseVo {
    /**
     * 支付宝用户id
     */
    private String alipayUserId;
}
