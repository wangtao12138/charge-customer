package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 客户查询参数
 */
@Getter
@Setter
public class CustomerQueryParam implements Serializable {
    private static final long serialVersionUID = 4090737418155141969L;

    /**
     * 客户唯一标识
     */
    private String customerGuid;

    /**
     * 手机号
     */
    private String customerContact;

    /**
     * 微信openId
     */
    private String webcharNo;

    /**
     * 阿里支付用户id
     */
    private String alipayUserId;
}
