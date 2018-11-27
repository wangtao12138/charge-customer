package cn.com.cdboost.charge.customer.service;

import cn.com.cdboost.charge.customer.common.BaseService;
import cn.com.cdboost.charge.customer.model.CustomerInfo;

/**
 * 客户信息单表接口
 */
public interface ICustomerInfoService  extends BaseService<CustomerInfo>{
    // 根据微信openId，查询用户基本信息
    CustomerInfo queryByOpenId(String openId);

    // 根据支付宝用户id，查询用户基本信息
    CustomerInfo queryByAlipayUserId(String alipayUserId);

    // 根据参数实体对象，查询用户基本信息
    CustomerInfo queryCustomerInfo(CustomerInfo customerInfo);

    // 根据用户手机号查询用户信息
    CustomerInfo queryByCustomerContact(String customerContact);

    // 根据用户唯一标识查询用户信息
    CustomerInfo queryByCustomerGuid(String customerGuid);

    // 根据用户唯一标识更新
    int updateSelectiveByCustomerGuid(CustomerInfo param);
}
