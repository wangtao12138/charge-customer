package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wt
 * @desc
 * @create in  2018/8/11
 **/
@Getter
@Setter
public class DayLineLossInfoList implements Serializable{
    private String  deviceNo;
    private String  port;
    private String  deviceElect;
    private String  userTime;
    private String  startDate;
    private String  endDate="";
    private String  mostPower;
    private String  mostCurrent;
    private String  state;



    public void setState(String state) {
        if("1".equals(state)){
            state="充电完成";
        }else{
            state="充电中";
        }
        this.state = state;
    }

}
