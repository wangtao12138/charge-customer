package cn.com.cdboost.charge.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cp_d_device_use_pay_relation")
public class DeviceUsePayRelation implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户唯一标识
     */
    @Column(name = "customer_guid")
    private String customerGuid;

    /**
     * 开电唯一标识
     */
    @Column(name = "charging_guid")
    private String chargingGuid;

    /**
     * 充值订单唯一标识
     */
    @Column(name = "pay_flag")
    private String payFlag;

    /**
     * 支付金额（冗余字段）
     */
    @Column(name = "pay_money")
    private BigDecimal payMoney;

    /**
     * 退款金额
     */
    @Column(name = "refund_money")
    private BigDecimal refundMoney;

    /**
     * 退款状态（0-未退款，1-已部分退款，2-已全额退款）
     */
    @Column(name = "refund_state")
    private Integer refundState;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 记录唯一标识
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
     * 获取用户唯一标识
     *
     * @return customer_guid - 用户唯一标识
     */
    public String getCustomerGuid() {
        return customerGuid;
    }

    /**
     * 设置用户唯一标识
     *
     * @param customerGuid 用户唯一标识
     */
    public void setCustomerGuid(String customerGuid) {
        this.customerGuid = customerGuid;
    }

    /**
     * 获取开电唯一标识
     *
     * @return charging_guid - 开电唯一标识
     */
    public String getChargingGuid() {
        return chargingGuid;
    }

    /**
     * 设置开电唯一标识
     *
     * @param chargingGuid 开电唯一标识
     */
    public void setChargingGuid(String chargingGuid) {
        this.chargingGuid = chargingGuid;
    }

    /**
     * 获取充值订单唯一标识
     *
     * @return pay_flag - 充值订单唯一标识
     */
    public String getPayFlag() {
        return payFlag;
    }

    /**
     * 设置充值订单唯一标识
     *
     * @param payFlag 充值订单唯一标识
     */
    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    /**
     * 获取支付金额（冗余字段）
     *
     * @return pay_money - 支付金额（冗余字段）
     */
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    /**
     * 设置支付金额（冗余字段）
     *
     * @param payMoney 支付金额（冗余字段）
     */
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    /**
     * 获取退款金额
     *
     * @return refund_money - 退款金额
     */
    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    /**
     * 设置退款金额
     *
     * @param refundMoney 退款金额
     */
    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    /**
     * 获取退款状态（0-未退款，1-已部分退款，2-已全额退款）
     *
     * @return refund_state - 退款状态（0-未退款，1-已部分退款，2-已全额退款）
     */
    public Integer getRefundState() {
        return refundState;
    }

    /**
     * 设置退款状态（0-未退款，1-已部分退款，2-已全额退款）
     *
     * @param refundState 退款状态（0-未退款，1-已部分退款，2-已全额退款）
     */
    public void setRefundState(Integer refundState) {
        this.refundState = refundState;
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
     * 获取记录唯一标识
     *
     * @return sign_guid - 记录唯一标识
     */
    public String getSignGuid() {
        return signGuid;
    }

    /**
     * 设置记录唯一标识
     *
     * @param signGuid 记录唯一标识
     */
    public void setSignGuid(String signGuid) {
        this.signGuid = signGuid;
    }
}