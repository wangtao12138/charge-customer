package cn.com.cdboost.charge.customer.vo.param;

import cn.com.cdboost.charge.base.vo.PageQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel(value = "CardPayQueryVo", description = "IC卡支付记录查询vo")
public class CardPayQueryVo extends PageQueryParam {

    @ApiModelProperty(value = "IC卡卡号")
    private String cardId;

    @ApiModelProperty(value = "客户guid")
    private String customerGuid;
}
