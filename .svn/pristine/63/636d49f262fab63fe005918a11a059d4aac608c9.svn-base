package cn.com.cdboost.charge.customer.dto.info;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wt
 * @desc
 * @create in  2018/8/11
 **/
@Getter
@Setter
public class DayLineLossInfoListDao {
    private String  deviceNo;
    private String  port;
    private String  deviceElect;
    private String  userTime;
    private String  startDate;
    private String  endDate="";
    private String  mostPower;
    private String  mostCurrent;
    private String  state;
    private String  chargingPlieGuid;


    public void setState(String state) {
        if("1".equals(state)){
            state="充电完成";
        }else{
            state="充电中";
        }
        this.state = state;
    }


}
