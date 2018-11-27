package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@ApiModel(value = "WithdrawCashInfo", description = "提现页面需要返回的信息")
public class WithdrawCashInfo implements Serializable{
    private static final long serialVersionUID = 3186042754335767433L;

    @ApiModelProperty(value = "当前余额")
    private BigDecimal remainAmount;

    @ApiModelProperty(value = "可提现金额")
    private BigDecimal withdrawCashAmount;

    @ApiModelProperty(value = "是否存在其他可提现账户(1-存在，0-不存在)")
    private Integer hasAccount = 0;

    @ApiModelProperty(value = "微信openId或者支付宝userId")
    private String userId;
}
