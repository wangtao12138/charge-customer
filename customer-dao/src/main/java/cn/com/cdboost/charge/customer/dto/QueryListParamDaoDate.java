package cn.com.cdboost.charge.customer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wt
 * @desc 分页查询参数实体类
 * @create 2017-10-31 09:30
 **/
@Getter
@Setter
public abstract class QueryListParamDaoDate extends QueryListDaoParam {

    /**
     * 起始时间
     */
    private String startDate;

    /**
     *结束时间
     */
    private String endDate;

}
