package cn.com.cdboost.charge.customer.dto.param;

import cn.com.cdboost.charge.customer.dto.PageQueryDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 充电消息日志分页查询参数
 */
@Getter
@Setter
public class MessageListQueryDto extends PageQueryDto {
    /**
     * 客户唯一标识
     */
    private String customerGuid;
}
