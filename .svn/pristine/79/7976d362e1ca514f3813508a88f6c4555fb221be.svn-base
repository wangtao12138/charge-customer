package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@ApiModel(value = "AlipayMonthRechargeInfo", description = "支付宝月卡购买返回信息")
public class AlipayMonthRechargeInfo implements Serializable{
    private static final long serialVersionUID = 2816019425432276034L;

    @ApiModelProperty(value = "是否需要调起支付（1-需要，0-不需要）")
    private Integer isPay;

    @ApiModelProperty(value = "支付宝订单号")
    private String tradeNo;
}
