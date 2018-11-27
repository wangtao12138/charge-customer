package cn.com.cdboost.charge.customer.vo.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 站点每日收入统计
 */
@Getter
@Setter
public class ProjectIncomeInfo implements Serializable{
    private static final long serialVersionUID = -7586035833915432778L;

    @ApiModelProperty(value = "站点每日收入集合")
    private List<ProjectIncomeDayInfo> projectIncomeDayInfo;

    @ApiModelProperty(value = "站点一个月收入汇总")
    private ProjectIncomeMonthInfo projectIncomeMonthInfo;
}
