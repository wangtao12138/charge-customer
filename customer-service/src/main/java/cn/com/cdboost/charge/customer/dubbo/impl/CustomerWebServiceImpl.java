package cn.com.cdboost.charge.customer.dubbo.impl;

import cn.com.cdboost.charge.customer.dubbo.CustomerWebService;
import cn.com.cdboost.charge.customer.vo.info.ElectricAndFeeDto;
import cn.com.cdboost.charge.customer.vo.info.ElectricCountDto;
import cn.com.cdboost.charge.customer.vo.info.Statistics;
import cn.com.cdboost.charge.customer.vo.param.ChargerDeviceQueryVo;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author wt
 * @desc
 * @create in  2018/11/19
 **/
public class CustomerWebServiceImpl implements CustomerWebService{



    @Override
    public ElectricCountDto queryCountList(ChargerDeviceQueryVo queryVo) {

        return null;
    }
}
