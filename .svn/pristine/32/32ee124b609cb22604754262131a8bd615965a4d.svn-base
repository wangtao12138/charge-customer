package cn.com.cdboost.charge.customer.service.impl;

import cn.com.cdboost.charge.customer.common.BaseServiceImpl;
import cn.com.cdboost.charge.customer.dao.MerchantWxConfigMapper;
import cn.com.cdboost.charge.customer.model.MerchantWxConfig;
import cn.com.cdboost.charge.customer.service.IMerchantWxConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 系统商户与微信相关配置表单表接口实现类
 */
@Service
public class IMerchantWxConfigServiceImpl extends BaseServiceImpl<MerchantWxConfig> implements IMerchantWxConfigService {

    @Resource
    private MerchantWxConfigMapper merchantWxConfigMapper;


    @Override
    public MerchantWxConfig queryByMerchantGuid(String merchantGuid) {
        MerchantWxConfig param = new MerchantWxConfig();
        param.setMerchantGuid(merchantGuid);
        return merchantWxConfigMapper.selectOne(param);
    }
}
