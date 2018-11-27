package cn.com.cdboost.charge.customer.service;

import cn.com.cdboost.charge.customer.common.BaseService;
import cn.com.cdboost.charge.customer.model.CustomerRefund;

import java.util.List;

/**
 * 交易订单退款记录表单表接口
 */
public interface ICustomerRefundService extends BaseService<CustomerRefund> {
    // 悲观锁，根据退款单号查询
    CustomerRefund queryByRefundNoForUpdate(String refundNo);

    // 查询退款成功的记录
    List<CustomerRefund> queryRefundSuccessList(String chargingGuid);
}