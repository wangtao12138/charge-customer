package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@ApiModel(value = "IncomeCurveInfo",description = "商户收入曲线返回对象")
public class IncomeCurveInfo implements Serializable{
    private static final long serialVersionUID = 1785605979021688392L;

    @ApiModelProperty(value = "x轴")
    private List<String> xData;

    @ApiModelProperty(value = "y轴")
    private List<BigDecimal> yData;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal remainAmount;

    @ApiModelProperty(value = "充电收入")
    private BigDecimal chargingIncome;

    @ApiModelProperty(value = "ic卡充值收入")
    private BigDecimal icCardIncome;
}
