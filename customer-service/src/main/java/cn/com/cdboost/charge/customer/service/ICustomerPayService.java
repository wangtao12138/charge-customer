package cn.com.cdboost.charge.customer.service;

import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.common.BaseService;
import cn.com.cdboost.charge.customer.model.CustomerPay;
import cn.com.cdboost.charge.customer.vo.param.PayOrderQueryParam;

import java.util.List;

/**
 * 客户充值记录信息单表接口
 */
public interface ICustomerPayService extends BaseService<CustomerPay>{

    // 根据参数实体对象，查询支付订单列表信息
    List<CustomerPay> queryPayOrders(PayOrderQueryParam queryParam);

    // 分页查询客户充值历史数据
    PageData<CustomerPay> queryPayHistorys(String customerGuid,Integer pageNumber,Integer pageSize);

    // 加悲观锁，根据payFlag查询
    CustomerPay queryByPayFlagForUpdate(String payFlag);

    // 根据payFlag查询
    CustomerPay queryByPayFlag(String payFlag);

    // 根据payFlags批量查询
    List<CustomerPay> batchQuery(List<String> payFlags);
}