package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "MessageListInfo", description = "消息列表信息")
public class MessageListInfo implements Serializable{
    private static final long serialVersionUID = -5898314745408506076L;

    @ApiModelProperty(value = "消息类型")
    private Integer messageType;

    @ApiModelProperty(value = "消息类型描述")
    private String messageTypeDesc;

    @ApiModelProperty(value = "消息显示内容")
    private String messageContent;

    @ApiModelProperty(value = "日志创建时间")
    private String createTime;

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "充电唯一标识")
    private String chargingGuid;

    @ApiModelProperty(value = "设备编号（带端口）")
    private String deviceNo;
}
