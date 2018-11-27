package cn.com.cdboost.charge.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "cp_d_customer_info")
public class CustomerInfo implements Serializable {
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
     * 微信昵称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 手机号
     */
    @Column(name = "customer_contact")
    private String customerContact;

    /**
     * 是否接收短信（0不接收，1接收）
     */
    @Column(name = "is_receive_sms")
    private Integer isReceiveSms;

    /**
     * 微信openId
     */
    @Column(name = "webchar_no")
    private String webcharNo;

    /**
     * 阿里支付用户id
     */
    @Column(name = "alipay_user_id")
    private String alipayUserId;

    /**
     * 阿里支付用户昵称
     */
    @Column(name = "alipay_nick_name")
    private String alipayNickName;

    /**
     * 车辆类别 1-电瓶车 2-电动车
     */
    @Column(name = "car_category")
    private Integer carCategory;

    /**
     * 车辆功率
     */
    @Column(name = "car_power")
    private Integer carPower;

    /**
     * 客户备注信息
     */
    private String remark;

    /**
     * 客户状态 1 正常 0 禁用
     */
    @Column(name = "customer_state")
    private Integer customerState;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取微信昵称
     *
     * @return customer_name - 微信昵称
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置微信昵称
     *
     * @param customerName 微信昵称
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取手机号
     *
     * @return customer_contact - 手机号
     */
    public String getCustomerContact() {
        return customerContact;
    }

    /**
     * 设置手机号
     *
     * @param customerContact 手机号
     */
    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    /**
     * 获取是否接收短信（0不接收，1接收）
     *
     * @return is_receive_sms - 是否接收短信（0不接收，1接收）
     */
    public Integer getIsReceiveSms() {
        return isReceiveSms;
    }

    /**
     * 设置是否接收短信（0不接收，1接收）
     *
     * @param isReceiveSms 是否接收短信（0不接收，1接收）
     */
    public void setIsReceiveSms(Integer isReceiveSms) {
        this.isReceiveSms = isReceiveSms;
    }

    /**
     * 获取微信openId
     *
     * @return webchar_no - 微信openId
     */
    public String getWebcharNo() {
        return webcharNo;
    }

    /**
     * 设置微信openId
     *
     * @param webcharNo 微信openId
     */
    public void setWebcharNo(String webcharNo) {
        this.webcharNo = webcharNo;
    }

    /**
     * 获取阿里支付用户id
     *
     * @return alipay_user_id - 阿里支付用户id
     */
    public String getAlipayUserId() {
        return alipayUserId;
    }

    /**
     * 设置阿里支付用户id
     *
     * @param alipayUserId 阿里支付用户id
     */
    public void setAlipayUserId(String alipayUserId) {
        this.alipayUserId = alipayUserId;
    }

    /**
     * 获取阿里支付用户昵称
     *
     * @return alipay_nick_name - 阿里支付用户昵称
     */
    public String getAlipayNickName() {
        return alipayNickName;
    }

    /**
     * 设置阿里支付用户昵称
     *
     * @param alipayNickName 阿里支付用户昵称
     */
    public void setAlipayNickName(String alipayNickName) {
        this.alipayNickName = alipayNickName;
    }

    /**
     * 获取车辆类别 1-电瓶车 2-电动车
     *
     * @return car_category - 车辆类别 1-电瓶车 2-电动车
     */
    public Integer getCarCategory() {
        return carCategory;
    }

    /**
     * 设置车辆类别 1-电瓶车 2-电动车
     *
     * @param carCategory 车辆类别 1-电瓶车 2-电动车
     */
    public void setCarCategory(Integer carCategory) {
        this.carCategory = carCategory;
    }

    /**
     * 获取车辆功率
     *
     * @return car_power - 车辆功率
     */
    public Integer getCarPower() {
        return carPower;
    }

    /**
     * 设置车辆功率
     *
     * @param carPower 车辆功率
     */
    public void setCarPower(Integer carPower) {
        this.carPower = carPower;
    }

    /**
     * 获取客户备注信息
     *
     * @return remark - 客户备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置客户备注信息
     *
     * @param remark 客户备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取客户状态 1 正常 0 禁用
     *
     * @return customer_state - 客户状态 1 正常 0 禁用
     */
    public Integer getCustomerState() {
        return customerState;
    }

    /**
     * 设置客户状态 1 正常 0 禁用
     *
     * @param customerState 客户状态 1 正常 0 禁用
     */
    public void setCustomerState(Integer customerState) {
        this.customerState = customerState;
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
}