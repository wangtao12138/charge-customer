package cn.com.cdboost.charge.customer.dao;

import cn.com.cdboost.charge.customer.common.CommonMapper;
import cn.com.cdboost.charge.customer.model.DeviceUsePayRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceUsePayRelationMapper extends CommonMapper<DeviceUsePayRelation> {
    // 查询可退款记录
    List<DeviceUsePayRelation> queryCanRefundList(String chargingGuid);

    // 查询支付取消记录
    List<DeviceUsePayRelation> queryList4PayCancel(@Param("chargingGuid") String chargingGuid,
                                                   @Param("signGuid") String signGuid);
}