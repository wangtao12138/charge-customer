package cn.com.cdboost.charge.customer.service.impl;

import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.common.BaseServiceImpl;
import cn.com.cdboost.charge.customer.constant.CustomerConstant;
import cn.com.cdboost.charge.customer.dao.CustomerPayMapper;
import cn.com.cdboost.charge.customer.model.CustomerPay;
import cn.com.cdboost.charge.customer.service.ICustomerPayService;
import cn.com.cdboost.charge.customer.vo.param.PayOrderQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户充值记录信息单表接口实现
 */
@Service
public class ICustomerPayServiceImpl extends BaseServiceImpl<CustomerPay> implements ICustomerPayService {

    @Resource
    private CustomerPayMapper customerPayMapper;

    @Override
    public List<CustomerPay> queryPayOrders(PayOrderQueryParam queryParam) {
        CustomerPay param = new CustomerPay();
        BeanUtils.copyProperties(queryParam,param);

        // 分页
        String sortName = queryParam.getSortName();
        if (StringUtils.isBlank(sortName)) {
            PageHelper.startPage(queryParam.getPageNumber(),queryParam.getPageSize());
        } else {
            String orderBy = sortName + queryParam.getSortOrder();
            PageHelper.startPage(queryParam.getPageNumber(),queryParam.getPageSize(),orderBy);
        }
        return customerPayMapper.select(param);
    }

    @Override
    public PageData<CustomerPay> queryPayHistorys(String customerGuid, Integer pageNumber, Integer pageSize) {
        CustomerPay param = new CustomerPay();
        param.setCustomerGuid(customerGuid);
        param.setPayState(CustomerConstant.PayState.PAY_SUCCESS.getState());
        PageHelper.startPage(pageNumber,pageSize,"id desc");
        List<CustomerPay> list = customerPayMapper.select(param);
        PageInfo<CustomerPay> pageInfo = new PageInfo<>(list);
        PageData<CustomerPay> pageData = new PageData<>();
        pageData.setTotal(pageInfo.getTotal());
        pageData.setList(list);
        return pageData;
    }

    @Override
    public CustomerPay queryByPayFlagForUpdate(String payFlag) {
        Condition condition = new Condition(CustomerPay.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("payFlag",payFlag);
        condition.setForUpdate(true);
        List<CustomerPay> payList = customerPayMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(payList)) {
            return null;
        }
        CustomerPay customerPay = payList.get(0);
        return customerPay;
    }

    @Override
    public CustomerPay queryByPayFlag(String payFlag) {
        CustomerPay param = new CustomerPay();
        param.setPayFlag(payFlag);
        return customerPayMapper.selectOne(param);
    }

    @Override
    public List<CustomerPay> batchQuery(List<String> payFlags) {
        Condition condition = new Condition(CustomerPay.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("payFlag",payFlags);
        List<CustomerPay> customerPays = customerPayMapper.selectByCondition(condition);
        return customerPays;
    }
}
