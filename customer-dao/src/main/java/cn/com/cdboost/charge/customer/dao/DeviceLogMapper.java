package cn.com.cdboost.charge.customer.dao;

import cn.com.cdboost.charge.customer.common.CommonMapper;
import cn.com.cdboost.charge.customer.dto.info.MessageListDto;
import cn.com.cdboost.charge.customer.dto.param.MessageListQueryDto;
import cn.com.cdboost.charge.customer.model.DeviceLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceLogMapper extends CommonMapper<DeviceLog> {
    // 分页查询充电日志消息
    List<MessageListDto> queryLogMessageList(MessageListQueryDto queryDto);

    // 查询充电日志消息总数
    Long queryLogMessageListCount(MessageListQueryDto queryDto);

    List<DeviceLog> queryCurve(@Param("chargingPlieGuid") String chargingPlieGuid, @Param("chargingGuid") String chargingGuid);
}