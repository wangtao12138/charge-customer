package cn.com.cdboost.charge.customer.vo.info;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 客户IC卡使用记录，充值记录详情
 */
@Getter
@Setter
public class IcCardInfo implements Serializable{
    private static final long serialVersionUID = -7230584523662160477L;

    /**
     * IC卡当前剩余金额
     */
    private BigDecimal remainAmount;

    /**
     * IC卡最后一次更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date updateTime;

    /**
     * IC卡最近3条使用记录
     */
    private List<IcCardUseInfo> useInfoList;

    /**
     * IC卡最近3条支付记录
     */
    private List<IcCardPayInfo> payInfoList;
}
