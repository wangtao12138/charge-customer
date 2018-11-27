package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ApiModel(value = "ChargingIncomeInfo", description = "站点充电收入情况列表")
public class ChargingIncomeInfo implements Serializable{
    private static final long serialVersionUID = -7648956001974079967L;

    @ApiModelProperty(value = "收入时间")
    private String inComeTime;

    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    @ApiModelProperty(value = "端口号")
    private String port;

    @ApiModelProperty(value = "充电收入金额")
    private BigDecimal inComeMoney;
}
