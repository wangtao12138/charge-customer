package cn.com.cdboost.charge.customer.vo.info;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * IC卡使用记录返回
 */
@Getter
@Setter
public class IcCardUseInfo implements Serializable{
    private static final long serialVersionUID = 6535028940709992925L;

    /**
     * IC卡卡号
     */
    private String cardId;

    /**
     * 充电开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 充电结束时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 充电扣除费用
     */
    private BigDecimal deductMoney;

    /**
     * 本次充电后剩余金额
     */
    private BigDecimal afterRemainAmount;

}
