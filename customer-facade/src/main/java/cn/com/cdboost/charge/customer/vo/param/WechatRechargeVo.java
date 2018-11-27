package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信续充入参
 */
@Getter
@Setter
public class WechatRechargeVo implements Serializable{
    private static final long serialVersionUID = -4447703081468257141L;

    /**
     * 客户guid
     */
    private String customerGuid;

    /**
     * 微信支付需要的ip
     */
    private String ip;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 充电唯一标识
     */
    private String chargingGuid;
}
