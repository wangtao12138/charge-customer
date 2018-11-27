package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信购买一些公共参数
 */
@Getter
@Setter
public class WxPayBaseVo implements Serializable {
    private static final long serialVersionUID = 2180965085705695062L;

    /**
     * 客户guid
     */
    private String customerGuid;

    /**
     * 方案guid
     */
    private String schemeGuid;

    /**
     * 微信支付需要的ip
     */
    private String ip;

    /**
     * 微信openId
     */
    private String openId;
}
