package cn.com.cdboost.charge.customer.dubbo.impl;

import cn.com.cdboost.charge.customer.dubbo.CustomerPayService;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户支付服务接口实现类
 */
@Slf4j
@Service(version = "1.0",retries = -1,timeout = 5000)
public class CustomerPayServiceImpl implements CustomerPayService {

}
