package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@ApiModel(value = "WechatPayOrderInfo", description = "微信下单返回给前端参数")
public class WechatPayOrderInfo implements Serializable{
    private static final long serialVersionUID = 6360703539071971249L;

    // 微信下单返回给前端的参数，用于调起微信支付
    @ApiModelProperty(value = "微信支付appId")
    private String appId;

    @ApiModelProperty(value = "时间戳")
    private String timeStamp;

    @ApiModelProperty(value = "随机字符串")
    private String nonceStr;

    @ApiModelProperty(value = "微信package字段值")
    private String packages;

    @ApiModelProperty(value = "签名类型")
    private String signType;

    @ApiModelProperty(value = "签名")
    private String paySign;
}
