package cn.com.cdboost.charge.customer.service;

import cn.com.cdboost.charge.customer.common.BaseService;
import cn.com.cdboost.charge.customer.model.MerchantWxConfig;

/**
 * 系统商户与微信相关配置表单表接口
 */
public interface IMerchantWxConfigService extends BaseService<MerchantWxConfig> {
    MerchantWxConfig queryByMerchantGuid(String merchantGuid);
}
