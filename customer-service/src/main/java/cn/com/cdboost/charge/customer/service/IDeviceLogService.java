package cn.com.cdboost.charge.customer.service;

import cn.com.cdboost.charge.customer.common.BaseService;
import cn.com.cdboost.charge.customer.model.DeviceLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备日志单表接口
 */
public interface IDeviceLogService extends BaseService<DeviceLog>{
    // 根据ids，批量查询
    List<DeviceLog> batchQueryByIds(List<Object> ids);

    // 根据ids，批量查询，并转成map
    Map<Integer,DeviceLog> batchQuery2Map(List<Object> ids);



}
