package cn.com.cdboost.charge.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "cp_d_customer_pay_card")
public class CustomerPayCard implements Serializable {
    /**
     * 标识id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户唯一标识
     */
    @Column(name = "customer_guid")
    private String customerGuid;

    /**
     * 项目标识
     */
    @Column(name = "project_guid")
    private String projectGuid;

    /**
     * 商户标识
     */
    @Column(name = "merchant_guid")
    private String merchantGuid;

    /**
     * 物业标识
     */
    @Column(name = "property_guid")
    private String propertyGuid;

    /**
     * ic卡编号
     */
    @Column(name = "card_id")
    private String cardId;

    /**
     * 实际支付金额
     */
    @Column(name = "pay_money")
    private BigDecimal payMoney;

    /**
     * 充值后剩余金额
     */
    @Column(name = "after_remain_amount")
    private BigDecimal afterRemainAmount;

    /**
     * 充值流水号
     */
    @Column(name = "serial_num")
    private String serialNum;

    @Column(name = "pay_flag")
    private String payFlag;

    /**
     * 充值方式 1-微信 2-支付宝 3-现金 4-余额
     */
    @Column(name = "pay_way")
    private Integer payWay;

    /**
     * 支付状态 0-待支付 1-支付成功
     */
    @Column(name = "pay_state")
    private Integer payState;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取客户唯一标识
     *
     * @return customer_guid - 客户唯一标识
     */
    public String getCustomerGuid() {
        return customerGuid;
    }

    /**
     * 设置客户唯一标识
     *
     * @param customerGuid 客户唯一标识
     */
    public void setCustomerGuid(String customerGuid) {
        this.customerGuid = customerGuid;
    }

    /**
     * 获取项目标识
     *
     * @return project_guid - 项目标识
     */
    public String getProjectGuid() {
        return projectGuid;
    }

    /**
     * 设置项目标识
     *
     * @param projectGuid 项目标识
     */
    public void setProjectGuid(String projectGuid) {
        this.projectGuid = projectGuid;
    }

    /**
     * 获取商户标识
     *
     * @return merchant_guid - 商户标识
     */
    public String getMerchantGuid() {
        return merchantGuid;
    }

    /**
     * 设置商户标识
     *
     * @param merchantGuid 商户标识
     */
    public void setMerchantGuid(String merchantGuid) {
        this.merchantGuid = merchantGuid;
    }

    /**
     * 获取物业标识
     *
     * @return property_guid - 物业标识
     */
    public String getPropertyGuid() {
        return propertyGuid;
    }

    /**
     * 设置物业标识
     *
     * @param propertyGuid 物业标识
     */
    public void setPropertyGuid(String propertyGuid) {
        this.propertyGuid = propertyGuid;
    }

    /**
     * 获取ic卡编号
     *
     * @return card_id - ic卡编号
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * 设置ic卡编号
     *
     * @param cardId ic卡编号
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * 获取实际支付金额
     *
     * @return pay_money - 实际支付金额
     */
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    /**
     * 设置实际支付金额
     *
     * @param payMoney 实际支付金额
     */
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    /**
     * 获取充值后剩余金额
     *
     * @return after_remain_amount - 充值后剩余金额
     */
    public BigDecimal getAfterRemainAmount() {
        return afterRemainAmount;
    }

    /**
     * 设置充值后剩余金额
     *
     * @param afterRemainAmount 充值后剩余金额
     */
    public void setAfterRemainAmount(BigDecimal afterRemainAmount) {
        this.afterRemainAmount = afterRemainAmount;
    }

    /**
     * 获取充值流水号
     *
     * @return serial_num - 充值流水号
     */
    public String getSerialNum() {
        return serialNum;
    }

    /**
     * 设置充值流水号
     *
     * @param serialNum 充值流水号
     */
    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    /**
     * @return pay_flag
     */
    public String getPayFlag() {
        return payFlag;
    }

    /**
     * @param payFlag
     */
    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    /**
     * 获取充值方式 1-微信 2-支付宝 3-现金 4-余额
     *
     * @return pay_way - 充值方式 1-微信 2-支付宝 3-现金 4-余额
     */
    public Integer getPayWay() {
        return payWay;
    }

    /**
     * 设置充值方式 1-微信 2-支付宝 3-现金 4-余额
     *
     * @param payWay 充值方式 1-微信 2-支付宝 3-现金 4-余额
     */
    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    /**
     * 获取支付状态 0-待支付 1-支付成功
     *
     * @return pay_state - 支付状态 0-待支付 1-支付成功
     */
    public Integer getPayState() {
        return payState;
    }

    /**
     * 设置支付状态 0-待支付 1-支付成功
     *
     * @param payState 支付状态 0-待支付 1-支付成功
     */
    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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