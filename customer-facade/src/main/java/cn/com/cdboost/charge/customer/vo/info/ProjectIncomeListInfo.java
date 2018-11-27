package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 站点列表收入
 */
@Setter
@Getter
public class ProjectIncomeListInfo extends PropertyIncomeInfo{
    private static final long serialVersionUID = -5210671179994244603L;

    /**
     * 站点guid
     */
    private String projectGuid;
    /**
     * 分润比例
     */
    private BigDecimal profitScale;
}
