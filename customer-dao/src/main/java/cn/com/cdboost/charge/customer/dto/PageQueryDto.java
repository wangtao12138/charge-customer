package cn.com.cdboost.charge.customer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询参数
 */
@Getter
@Setter
public class PageQueryDto {
    /**
     * 分页开始索引值
     */
    private Integer pageIndex;

    /**
     * 每页显示数量
     */
    private Integer pageSize;
}
