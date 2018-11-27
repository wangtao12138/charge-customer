package cn.com.cdboost.charge.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "cp_d_device_use_month_relation")
public class DeviceUseMonthRelation implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 充电唯一标识
     */
    @Column(name = "charging_guid")
    private String chargingGuid;

    /**
     * 开电记录类型（0-普通开电记录，1-功率超限提档记录，2-续充开电记录）
     */
    @Column(name = "charge_type")
    private Integer chargeType;

    /**
     * 月卡账户guid
     */
    @Column(name = "account_guid")
    private String accountGuid;

    /**
     * 开电扣除次数
     */
    @Column(name = "charge_cnt")
    private Integer chargeCnt;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 本次操作唯一标识
     */
    @Column(name = "sign_guid")
    private String signGuid;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取充电唯一标识
     *
     * @return charging_guid - 充电唯一标识
     */
    public String getChargingGuid() {
        return chargingGuid;
    }

    /**
     * 设置充电唯一标识
     *
     * @param chargingGuid 充电唯一标识
     */
    public void setChargingGuid(String chargingGuid) {
        this.chargingGuid = chargingGuid;
    }

    /**
     * 获取开电记录类型（0-普通开电记录，1-功率超限提档记录，2-续充开电记录）
     *
     * @return charge_type - 开电记录类型（0-普通开电记录，1-功率超限提档记录，2-续充开电记录）
     */
    public Integer getChargeType() {
        return chargeType;
    }

    /**
     * 设置开电记录类型（0-普通开电记录，1-功率超限提档记录，2-续充开电记录）
     *
     * @param chargeType 开电记录类型（0-普通开电记录，1-功率超限提档记录，2-续充开电记录）
     */
    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    /**
     * 获取月卡账户guid
     *
     * @return account_guid - 月卡账户guid
     */
    public String getAccountGuid() {
        return accountGuid;
    }

    /**
     * 设置月卡账户guid
     *
     * @param accountGuid 月卡账户guid
     */
    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    /**
     * 获取开电扣除次数
     *
     * @return charge_cnt - 开电扣除次数
     */
    public Integer getChargeCnt() {
        return chargeCnt;
    }

    /**
     * 设置开电扣除次数
     *
     * @param chargeCnt 开电扣除次数
     */
    public void setChargeCnt(Integer chargeCnt) {
        this.chargeCnt = chargeCnt;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取本次操作唯一标识
     *
     * @return sign_guid - 本次操作唯一标识
     */
    public String getSignGuid() {
        return signGuid;
    }

    /**
     * 设置本次操作唯一标识
     *
     * @param signGuid 本次操作唯一标识
     */
    public void setSignGuid(String signGuid) {
        this.signGuid = signGuid;
    }
}