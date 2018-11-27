package cn.com.cdboost.charge.customer.vo.info;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

public class HeartDto {
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date heartTime;
    private Integer prot;
    private Integer state;
    private String eventContent;
    private BigDecimal current;
    private BigDecimal voltage;
    private BigDecimal power;
    private BigDecimal activeTotal;
    private BigDecimal remainTime;

    public Date getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(Date heartTime) {
        this.heartTime = heartTime;
    }

    public Integer getProt() {
        return prot;
    }

    public void setProt(Integer prot) {
        this.prot = prot;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public void setCurrent(BigDecimal current) {
        this.current = current;
    }

    public BigDecimal getVoltage() {
        return voltage;
    }

    public void setVoltage(BigDecimal voltage) {
        this.voltage = voltage;
    }

    public BigDecimal getPower() {
        return power;
    }

    public void setPower(BigDecimal power) {
        this.power = power;
    }

    public BigDecimal getActiveTotal() {
        return activeTotal;
    }

    public void setActiveTotal(BigDecimal activeTotal) {
        this.activeTotal = activeTotal;
    }

    public BigDecimal getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(BigDecimal remainTime) {
        this.remainTime = remainTime;
    }
}
