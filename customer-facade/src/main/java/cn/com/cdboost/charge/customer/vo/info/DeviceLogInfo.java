package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Setter
@Getter
public class DeviceLogInfo implements Serializable {
    /**
     * 标识id
     */
    private Integer id;

    /**
     * 充电桩唯一标识
     */
    private String chargingPlieGuid;

    /**
     * 充电桩端口号（0-9、a-f 16个端口）
     */
    private String port;

    /**
     * 事件code码（0正常，1告警，-1充电结束）
     */
    private Integer eventCode;

    /**
     * 事件内容
     */
    private String eventContent;

    /**
     * 短信发送的事件内容
     */
    private String smsContent;

    /**
     * 电流
     */
    private BigDecimal current;

    /**
     * 电压
     */
    private BigDecimal voltage;

    /**
     * 功率
     */
    private BigDecimal power;

    /**
     * 电量
     */
    private BigDecimal activeTotal;

    /**
     * 剩余时长
     */
    private BigDecimal remainTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 充电情况
     */
    private BigDecimal chargingPercent;

    /**
     * 充电记录唯一标识
     */
    private String chargingGuid;

    /**
     * 短信发送状态（0未发送，1已发送）
     */
    private Integer smsStatus;

    /**
     * 设备温度
     */
    private Integer devTemperature;

    private static final long serialVersionUID = 1L;


}