package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ApiModel(value = "IcCardIncomeResp",description = "站点ic卡充值收入返回对象")
public class IcCardIncomeInfo implements Serializable{
    private static final long serialVersionUID = -8440415652577917506L;

    @ApiModelProperty(value = "收入时间")
    private String inComeTime;

    @ApiModelProperty(value = "卡号")
    private String cardId;

    @ApiModelProperty(value = "充值收入金额")
    private BigDecimal inComeMoney;
}
