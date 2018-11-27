package cn.com.cdboost.charge.customer.service;

import cn.com.cdboost.charge.customer.common.BaseService;
import cn.com.cdboost.charge.customer.model.DeviceUsePayRelation;

import java.util.List;

/**
 * 开电记录与开电充值订单关联表单表接口
 */
public interface IDeviceUsePayRelationService extends BaseService<DeviceUsePayRelation>{
    DeviceUsePayRelation queryByPayFlag(String payFlag);

    List<DeviceUsePayRelation> queryByParams(String chargingGuid,String signGuid);

    // 查询可退款的明细
    List<DeviceUsePayRelation> queryCanRefundList(String chargingGuid);

    // 查询某次业务导致的开电支付明细
    List<DeviceUsePayRelation> queryBySignGuid(String signGuid);
}
