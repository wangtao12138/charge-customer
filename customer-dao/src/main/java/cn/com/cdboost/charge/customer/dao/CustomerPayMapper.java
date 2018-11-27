package cn.com.cdboost.charge.customer.dao;

import cn.com.cdboost.charge.customer.common.CommonMapper;
import cn.com.cdboost.charge.customer.dto.ChargeRecordListDaoDto;
import cn.com.cdboost.charge.customer.dto.info.ChargeRecordListDaoInfo;
import cn.com.cdboost.charge.customer.model.CustomerPay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerPayMapper extends CommonMapper<CustomerPay> {

    List<ChargeRecordListDaoInfo> chargeRecordList(@Param("chargeRecordListDto") ChargeRecordListDaoDto chargeRecordListDto);

    // 支付成功批量更新，状态和时间
    int batchUpdate(List<CustomerPay> list);
}