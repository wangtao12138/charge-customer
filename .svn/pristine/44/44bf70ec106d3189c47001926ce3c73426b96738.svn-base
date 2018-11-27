package cn.com.cdboost.charge.customer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "cp_d_device_signal")
public class DeviceSignal implements Serializable {
    /**
     * 标识id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 通信编号
     */
    @Column(name = "comm_no")
    private String commNo;

    /**
     * 上行信号强度
     */
    private Integer rssi;

    /**
     * 上行信噪比
     */
    private BigDecimal snr;

    /**
     * 1 上行 2下行
     */
    @Column(name = "data_type")
    private Integer dataType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取标识id
     *
     * @return id - 标识id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置标识id
     *
     * @param id 标识id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取通信编号
     *
     * @return comm_no - 通信编号
     */
    public String getCommNo() {
        return commNo;
    }

    /**
     * 设置通信编号
     *
     * @param commNo 通信编号
     */
    public void setCommNo(String commNo) {
        this.commNo = commNo;
    }

    /**
     * 获取上行信号强度
     *
     * @return rssi - 上行信号强度
     */
    public Integer getRssi() {
        return rssi;
    }

    /**
     * 设置上行信号强度
     *
     * @param rssi 上行信号强度
     */
    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    /**
     * 获取上行信噪比
     *
     * @return snr - 上行信噪比
     */
    public BigDecimal getSnr() {
        return snr;
    }

    /**
     * 设置上行信噪比
     *
     * @param snr 上行信噪比
     */
    public void setSnr(BigDecimal snr) {
        this.snr = snr;
    }

    /**
     * 获取1 上行 2下行
     *
     * @return data_type - 1 上行 2下行
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * 设置1 上行 2下行
     *
     * @param dataType 1 上行 2下行
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
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
}