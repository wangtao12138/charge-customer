package cn.com.cdboost.charge.customer.vo.param;

import cn.com.cdboost.charge.base.vo.PageQueryParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户充电历史数据查询VO
 */
@Getter
@Setter
public class ChargeHistoryVo extends PageQueryParam{
    /**
     * 客户唯一标识
     */
    private String customerGuid;
}
