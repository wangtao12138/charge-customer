package cn.com.cdboost.charge.customer.service.impl;

import cn.com.cdboost.charge.customer.common.BaseServiceImpl;
import cn.com.cdboost.charge.customer.dao.MerchantZfbConfigMapper;
import cn.com.cdboost.charge.customer.model.MerchantZfbConfig;
import cn.com.cdboost.charge.customer.service.IMerchantZfbConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 系统商户与支付宝相关信息配置表单表接口实现类
 */
@Service
public class IMerchantZfbConfigServiceImpl extends BaseServiceImpl<MerchantZfbConfig> implements IMerchantZfbConfigService {

    @Resource
    private MerchantZfbConfigMapper merchantZfbConfigMapper;

    @Override
    public MerchantZfbConfig queryByMerchantGuid(String merchantGuid) {
        MerchantZfbConfig param = new MerchantZfbConfig();
        param.setMerchantGuid(merchantGuid);
        return merchantZfbConfigMapper.selectOne(param);
    }
}
