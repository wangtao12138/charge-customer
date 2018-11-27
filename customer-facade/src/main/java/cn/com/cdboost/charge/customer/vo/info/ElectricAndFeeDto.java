package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 电量电费统计列表
 */
@Getter
@Setter
public class ElectricAndFeeDto implements Serializable{
    private String chargingPlieGuid;
    private String deviceNo;
    private String commNo;
    private String deviceName;
    private String installAddr;
    private String installDate;
    private String projectAddr;
    private Integer useNumber;
    private BigDecimal electricityFees;
    private BigDecimal electricQuantity;
    private BigDecimal profitable;


}
