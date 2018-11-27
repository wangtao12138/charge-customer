package cn.com.cdboost.charge.customer.vo.param;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author wt
 * @desc 分页查询参数实体类
 * @create 2017-10-31 09:30
 **/
public abstract class QueryListParamDate extends QueryListParam {

    /**
     * 起始时间
     */
    @NotBlank(message = "startDate不能为null")
    private String startDate;

    /**
     *结束时间
     */
    @NotBlank(message = "endDate不能为null")
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
