package cn.com.cdboost.charge.customer.service.impl;

import cn.com.cdboost.charge.customer.common.BaseServiceImpl;
import cn.com.cdboost.charge.customer.dao.DeviceLogMapper;
import cn.com.cdboost.charge.customer.model.DeviceLog;
import cn.com.cdboost.charge.customer.service.IDeviceLogService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 设备日志单表接口实现
 */
@Service
public class IDeviceLogServiceImpl extends BaseServiceImpl<DeviceLog> implements IDeviceLogService {

    @Resource
    private DeviceLogMapper deviceLogMapper;

    @Override
    public List<DeviceLog> batchQueryByIds(List<Object> ids) {
        return deviceLogMapper.selectByIdList(ids);
    }

    @Override
    public Map<Integer, DeviceLog> batchQuery2Map(List<Object> ids) {
        List<DeviceLog> deviceLogs = deviceLogMapper.selectByIdList(ids);
        Map<Integer, DeviceLog> dataMap = Maps.newHashMap();
        for (DeviceLog deviceLog : deviceLogs) {
            dataMap.put(deviceLog.getId(),deviceLog);
        }
        return dataMap;
    }

}
