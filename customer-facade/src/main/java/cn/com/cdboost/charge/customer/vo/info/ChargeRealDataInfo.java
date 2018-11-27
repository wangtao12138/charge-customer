package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 提供给充电中实时数据页面的数据
 */
@Getter
@Setter
public class ChargeRealDataInfo extends ChargeOrderInfo{
    /**
     * 充电百分比
     */
    private BigDecimal chargingPercent;

    /**
     * 功率
     */
    private BigDecimal power;
}
