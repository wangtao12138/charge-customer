package cn.com.cdboost.charge.customer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cp_d_device_log")
@Getter
@Setter
public class DeviceLog implements Serializable {
    /**
     * 标识id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 充电桩唯一标识
     */
    @Column(name = "charging_plie_guid")
    private String chargingPlieGuid;

    /**
     * 充电桩端口号（0-9、a-f 16个端口）
     */
    private String port;

    /**
     * 事件code码（0正常，1告警，-1充电结束）
     */
    @Column(name = "event_code")
    private Integer eventCode;

    /**
     * 事件内容
     */
    @Column(name = "event_content")
    private String eventContent;

    /**
     * 短信发送的事件内容
     */
    @Column(name = "sms_content")
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
    @Column(name = "active_total")
    private BigDecimal activeTotal;

    /**
     * 剩余时长
     */
    @Column(name = "remain_time")
    private BigDecimal remainTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 充电情况
     */
    @Column(name = "charging_percent")
    private BigDecimal chargingPercent;

    /**
     * 充电记录唯一标识
     */
    @Column(name = "charging_guid")
    private String chargingGuid;

    /**
     * 短信发送状态（0未发送，1已发送）
     */
    @Column(name = "sms_status")
    private Integer smsStatus;

    /**
     * 设备温度
     */
    @Column(name = "dev_temperature")
    private Integer devTemperature;

    private static final long serialVersionUID = 1L;


}