package cn.com.cdboost.charge.customer.service.impl;

import cn.com.cdboost.charge.customer.common.BaseServiceImpl;
import cn.com.cdboost.charge.customer.constant.CustomerConstant;
import cn.com.cdboost.charge.customer.dao.CustomerRefundMapper;
import cn.com.cdboost.charge.customer.model.CustomerRefund;
import cn.com.cdboost.charge.customer.service.ICustomerRefundService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 交易订单退款记录表单表接口实现类
 */
@Service
public class ICustomerRefundServiceImpl extends BaseServiceImpl<CustomerRefund> implements ICustomerRefundService {

    @Resource
    private CustomerRefundMapper customerRefundMapper;

    @Override
    public CustomerRefund queryByRefundNoForUpdate(String refundNo) {
        Condition condition = new Condition(CustomerRefund.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("refundNo",refundNo);
        condition.setForUpdate(true);
        List<CustomerRefund> customerRefunds = customerRefundMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(customerRefunds)) {
            return null;
        }
        CustomerRefund customerRefund = customerRefunds.get(0);
        return customerRefund;
    }

    @Override
    public List<CustomerRefund> queryRefundSuccessList(String chargingGuid) {
        Condition condition = new Condition(CustomerRefund.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("chargingGuid",chargingGuid);
        criteria.andEqualTo("refundState", CustomerConstant.RefundState.SUCCESS.getState());
        return customerRefundMapper.selectByCondition(condition);
    }
}
