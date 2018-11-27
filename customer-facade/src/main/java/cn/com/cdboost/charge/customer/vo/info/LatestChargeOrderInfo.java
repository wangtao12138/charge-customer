package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 客户最近一次充电记录信息
 */
@Setter
@Getter
@ApiModel(value = "LatestChargeOrderInfo", description = "最近一次充电记录信息")
public class LatestChargeOrderInfo implements Serializable{
    private static final long serialVersionUID = -3452364115824919436L;

    @ApiModelProperty(value = "异常状态")
    private Integer eventCode;

    @ApiModelProperty(value = "异常状态描述")
    private String eventCodeDesc;

    @ApiModelProperty(value = "开始充电时间")
    private String startTime;

    @ApiModelProperty(value = "充电记录唯一标识")
    private String chargingGuid;

    @ApiModelProperty(value = "充电桩唯一标识")
    private String chargingPlieGuid;

    @ApiModelProperty(value = "是否异常(0-异常，1-正常)")
    private Integer isEvent;
}
