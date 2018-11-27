package cn.com.cdboost.charge.customer.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "cp_d_merchant_wx_config")
public class MerchantWxConfig implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商户guid
     */
    @Column(name = "merchant_guid")
    private String merchantGuid;

    /**
     * 微信分配的公众账号id
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 微信支付分配的商户号
     */
    @Column(name = "mch_id")
    private String mchId;

    /**
     * 微信分配的子商户公众账号id
     */
    @Column(name = "sub_appid")
    private String subAppid;

    /**
     * 微信支付分配的子商户号
     */
    @Column(name = "sub_mch_id")
    private String subMchId;

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
     * 获取商户guid
     *
     * @return merchant_guid - 商户guid
     */
    public String getMerchantGuid() {
        return merchantGuid;
    }

    /**
     * 设置商户guid
     *
     * @param merchantGuid 商户guid
     */
    public void setMerchantGuid(String merchantGuid) {
        this.merchantGuid = merchantGuid;
    }

    /**
     * 获取微信分配的公众账号id
     *
     * @return app_id - 微信分配的公众账号id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置微信分配的公众账号id
     *
     * @param appId 微信分配的公众账号id
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取微信支付分配的商户号
     *
     * @return mch_id - 微信支付分配的商户号
     */
    public String getMchId() {
        return mchId;
    }

    /**
     * 设置微信支付分配的商户号
     *
     * @param mchId 微信支付分配的商户号
     */
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    /**
     * 获取微信分配的子商户公众账号id
     *
     * @return sub_appid - 微信分配的子商户公众账号id
     */
    public String getSubAppid() {
        return subAppid;
    }

    /**
     * 设置微信分配的子商户公众账号id
     *
     * @param subAppid 微信分配的子商户公众账号id
     */
    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
    }

    /**
     * 获取微信支付分配的子商户号
     *
     * @return sub_mch_id - 微信支付分配的子商户号
     */
    public String getSubMchId() {
        return subMchId;
    }

    /**
     * 设置微信支付分配的子商户号
     *
     * @param subMchId 微信支付分配的子商户号
     */
    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }
}