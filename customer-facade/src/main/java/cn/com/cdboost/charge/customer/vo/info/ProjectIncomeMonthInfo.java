package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
public class ProjectIncomeMonthInfo implements Serializable{
    private static final long serialVersionUID = -4895858442271117606L;
    //总收入
    private BigDecimal incomeMoneyTotal;
    //总电量
    private BigDecimal usePowerTotal;
}
