package cn.com.cdboost.charge.customer.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "RegisterVo", description = "客户注册信息")
public class RegisterVo implements Serializable{
    private static final long serialVersionUID = -4739862113997457654L;

    @ApiModelProperty(value = "客户端类型（1-微信，2-支付宝）")
    private Integer appType;

    @ApiModelProperty(value = "客户端是微信，则为openId，客户端是支付宝，则为支付宝用户id")
    private String openId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "验证码")
    private String verificationCode;
}
