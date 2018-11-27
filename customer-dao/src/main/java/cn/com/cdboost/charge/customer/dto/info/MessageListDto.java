package cn.com.cdboost.charge.customer.dto.info;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 充电消息日志列表返回
 */
@Getter
@Setter
public class MessageListDto {
    private String chargingPlieGuid;
    private Integer eventCode;
    private String smsContent;
    private Date createTime;
    private String chargingGuid;
}
