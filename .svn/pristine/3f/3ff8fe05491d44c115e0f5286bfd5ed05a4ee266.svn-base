package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 客户修改vo参数
 */
@Getter
@Setter
public class CustomerUpdateVo implements Serializable{
    private static final long serialVersionUID = -2050190773977322700L;

    /**
     * 客户唯一标识
     */
    @NotBlank(message = "customerGuid不能为空")
    private String customerGuid;

    /**
     * 微信昵称
     */
    private String customerName;

    /**
     * 手机号
     */
    private String customerContact;

    /**
     * 是否接收短信（0不接收，1接收）
     */
    private Integer isReceiveSms;

    /**
     * 微信openId
     */
    private String webcharNo;

    /**
     * 阿里支付用户id
     */
    private String alipayUserId;

    /**
     * 阿里支付用户昵称
     */
    private String alipayNickName;

    /**
     * 客户备注信息
     */
    private String remark;

    /**
     * 客户状态 1 正常 0 禁用
     */
    private Integer customerState;
}
