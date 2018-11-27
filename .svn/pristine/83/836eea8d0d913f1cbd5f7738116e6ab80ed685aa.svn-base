package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ApiModel(value = "ChargeCompleteInfo", description = "充电完成弹窗或者使用记录点击弹窗")
public class ChargeCompleteInfo implements Serializable{
    private static final long serialVersionUID = -5084086486269075190L;

    @ApiModelProperty(value = "选择充电的套餐类型（1-临时充值 2-包月充值 3-一次充满）")
    private Integer payCategory;

    @ApiModelProperty(value = "（1-临时充值 2-包月充值 3-一次充满）")
    private String payCategoryDesc;

    @ApiModelProperty(value = "异常值(1-异常,0-正常)")
    private Integer isEvent;

    @ApiModelProperty(value = "异常描述（1-电流告警 2-率告警 3-中途更换充电电瓶车 4-设备短路 5-充电结束 0-其他）")
    private String eventCodeDesc;

    @ApiModelProperty(value = "充值时长")
    private String chargeTime;

    @ApiModelProperty(value = "充电金额")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "剩余金额")
    private BigDecimal remainAmount;

    @ApiModelProperty(value = "扣除次数")
    private Integer useCnt;

    @ApiModelProperty(value = "剩余次数")
    private Integer remainCnt;

    @ApiModelProperty(value = "开始充电时间")
    private String startTime;

    @ApiModelProperty(value = "结束充电时间")
    private String endTime;

    @ApiModelProperty(value = "联系电话")
    private String contact;

    @ApiModelProperty(value = "设备编号（带端口）")
    private String deviceNo;
}
