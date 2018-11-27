package cn.com.cdboost.charge.customer.service.impl;

import cn.com.cdboost.charge.customer.common.BaseServiceImpl;
import cn.com.cdboost.charge.customer.dao.DeviceUsePayRelationMapper;
import cn.com.cdboost.charge.customer.model.DeviceUsePayRelation;
import cn.com.cdboost.charge.customer.service.IDeviceUsePayRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 开电记录与开电充值订单关联表单表接口实现类
 */
@Service
public class IDeviceUsePayRelationServiceImpl extends BaseServiceImpl<DeviceUsePayRelation> implements IDeviceUsePayRelationService {

    @Resource
    private DeviceUsePayRelationMapper deviceUsePayRelationMapper;

    @Override
    public DeviceUsePayRelation queryByPayFlag(String payFlag) {
        DeviceUsePayRelation param = new DeviceUsePayRelation();
        param.setPayFlag(payFlag);
        return deviceUsePayRelationMapper.selectOne(param);
    }

    @Override
    public List<DeviceUsePayRelation> queryByParams(String chargingGuid, String signGuid) {
        DeviceUsePayRelation param = new DeviceUsePayRelation();
        param.setChargingGuid(chargingGuid);
        param.setSignGuid(signGuid);
        return deviceUsePayRelationMapper.select(param);
    }

    @Override
    public List<DeviceUsePayRelation> queryCanRefundList(String chargingGuid) {
        List<DeviceUsePayRelation> usePayRelations = deviceUsePayRelationMapper.queryCanRefundList(chargingGuid);
        return usePayRelations;
    }

    @Override
    public List<DeviceUsePayRelation> queryBySignGuid(String signGuid) {
        DeviceUsePayRelation param = new DeviceUsePayRelation();
        param.setSignGuid(signGuid);
        return deviceUsePayRelationMapper.select(param);
    }
}
