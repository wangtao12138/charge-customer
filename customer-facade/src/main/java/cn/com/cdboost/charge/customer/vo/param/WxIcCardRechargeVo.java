package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WxIcCardRechargeVo extends IcCardRechargeBaseVo{

    /**
     * 微信支付需要的ip
     */
    private String ip;

    /**
     * 微信openId
     */
    private String openId;

}
