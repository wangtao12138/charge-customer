package cn.com.cdboost.charge.customer.dao;

import cn.com.cdboost.charge.customer.common.CommonMapper;
import cn.com.cdboost.charge.customer.dto.CustomerInfoListDaoDto;
import cn.com.cdboost.charge.customer.dto.DayLineLossDtoDao;
import cn.com.cdboost.charge.customer.dto.UseRecordListDaoDto;
import cn.com.cdboost.charge.customer.dto.info.*;
import cn.com.cdboost.charge.customer.model.DeviceUseDetailed;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceUseDetailedMapper extends CommonMapper<DeviceUseDetailed> {

    List<CustomerInfoListDaoInfo> customerInfoList(@Param("customerInfoListDto") CustomerInfoListDaoDto customerInfoListDto);

    ChargeCustomerInfoDetailDaoInfo customerInfoDetail(@Param("customerGuid") String customerGuid);

    List<UseRecordListDaoInfo> useRecordList(@Param("useRecordListDto") UseRecordListDaoDto useRecordListDto);

    List<DayLineLossInfoDao> dayLineLoss(@Param("dayLineLossDto") DayLineLossDtoDao dayLineLossDto);

    List<SchemeUseListDto> querySchemeUseList(String projectGuid, String beginTime, String endTime);
}