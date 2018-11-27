package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物业收入统计列表
 */
@Setter
@Getter
public class PropertyIncomeInfo implements Serializable{
    private static final long serialVersionUID = -3986299135888966507L;

    /**
     * 物业guid
     */
    private String propertyGuid;

    /**
     * 物业名称
     */
    private String propertyName;

    /**
     * 总收入
     */
    private BigDecimal inComeMoney;

    /**
     * 充电收入
     */
    private BigDecimal chargingIncome;

    /**
     * ic卡充值收入
     */
    private BigDecimal icCardIncome;

    /**
     * 支出
     */
    private BigDecimal cost;
}
