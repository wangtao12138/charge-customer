package cn.com.cdboost.charge.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "cp_d_merchant_zfb_config")
public class MerchantZfbConfig implements Serializable {
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
     * 开发者应用的AppId
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 支付宝授权用户id
     */
    @Column(name = "auth_user_id")
    private String authUserId;

    /**
     * 授权商户的AppId
     */
    @Column(name = "auth_app_id")
    private String authAppId;

    /**
     * 商户授权令牌
     */
    @Column(name = "app_auth_token")
    private String appAuthToken;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 获取开发者应用的AppId
     *
     * @return app_id - 开发者应用的AppId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置开发者应用的AppId
     *
     * @param appId 开发者应用的AppId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取支付宝授权用户id
     *
     * @return auth_user_id - 支付宝授权用户id
     */
    public String getAuthUserId() {
        return authUserId;
    }

    /**
     * 设置支付宝授权用户id
     *
     * @param authUserId 支付宝授权用户id
     */
    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    /**
     * 获取授权商户的AppId
     *
     * @return auth_app_id - 授权商户的AppId
     */
    public String getAuthAppId() {
        return authAppId;
    }

    /**
     * 设置授权商户的AppId
     *
     * @param authAppId 授权商户的AppId
     */
    public void setAuthAppId(String authAppId) {
        this.authAppId = authAppId;
    }

    /**
     * 获取商户授权令牌
     *
     * @return app_auth_token - 商户授权令牌
     */
    public String getAppAuthToken() {
        return appAuthToken;
    }

    /**
     * 设置商户授权令牌
     *
     * @param appAuthToken 商户授权令牌
     */
    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
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