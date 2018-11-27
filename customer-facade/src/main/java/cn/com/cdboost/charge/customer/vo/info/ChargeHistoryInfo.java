package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ApiModel(value = "ChargeHistoryInfo", description = "充电使用记录信息")
public class ChargeHistoryInfo implements Serializable{
    private static final long serialVersionUID = -856289610241447339L;

    @ApiModelProperty(value = "是否异常结束")
    private Integer isEvent;

    @ApiModelProperty(value = "异常事件描述")
    private String isEventDesc;

    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    @ApiModelProperty(value = "2月卡使用 非2账户使用")
    private Integer payWay;

    @ApiModelProperty(value = "安装地址")
    private String installAddr;

    @ApiModelProperty(value = "剩余金额")
    private BigDecimal remainAmount;

    @ApiModelProperty(value = "剩余次数")
    private Integer remainCnt;

    @ApiModelProperty(value = "充电日期")
    private String chargeDate;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "充电时长")
    private String chargeTime;

    @ApiModelProperty(value = "支付描述")
    private String payWayDesc;

    @ApiModelProperty(value = "充电唯一标识")
    private String chargingGuid;
}
