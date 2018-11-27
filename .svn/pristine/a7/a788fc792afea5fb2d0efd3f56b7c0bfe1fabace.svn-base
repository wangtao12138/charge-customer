package cn.com.cdboost.charge.merchant.vo.param;

import cn.com.cdboost.charge.base.vo.PageQueryParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * ic卡查询参数
 */
@Setter
@Getter
public class IcCardParam extends PageQueryParam implements Serializable{

    private static final long serialVersionUID = 3452804315308928050L;
    @ApiModelProperty(value = "商户guid")
    private String merchantGuid;

    @ApiModelProperty(value = "站点guid")
    private String projectGuid;

    @ApiModelProperty(value = "站点guid集合")
    private List<String> projectGuids;

    @ApiModelProperty(value = "卡号")
    private String cardId;

    @ApiModelProperty(value = "卡状态：0初始、1 -启用、2-停用、3-挂失")
    private Integer cardState;

    @ApiModelProperty(value = "IC 卡状态 0 欠费 1正常")
    private Integer cardOwe;

    @ApiModelProperty(value = "客户唯一标识")
    private String customerGuid;


    private String deviceNo;

    private String commNo;

    private Integer sendFlag;
}
