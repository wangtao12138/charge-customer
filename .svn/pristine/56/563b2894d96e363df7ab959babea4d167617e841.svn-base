package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wt
 * @desc
 * @create in  2018/8/16
 **/
@Getter
@Setter
public class UseRecordListInfo implements Serializable{

    private String date;

    private String deviceState;
    /**
     * 1-按时充电 2-按电量充电 3-充满断电,
     */
    private String payCategory;
    /**
     * '开启方式 1-微信 2-支付宝 3-IC卡',
     */
    private String payMethod;

    private String deviceNo;

    private String port;

    private String deviceElect;

    private String useTime;

    private String startDate;

    private String endDate;

    private String installAddress;

    private String price;

    private String chargingGuid;

    private String chargingPlieGuid;

    private String schemeGuid;


    public void setDeviceState(String deviceState) {
        if("1".equals(deviceState)){
            deviceState="手动停止";
        }else if("0".equals(deviceState)){
            deviceState="自动停止";
        }else{
            deviceState="异常停止";
        }
        this.deviceState = deviceState;
    }

    public void setPayMethod(String payMethod) {
        if("1".equals(payMethod)){
            payMethod="微信";
        }else if("2".equals(payMethod)){
            payMethod="支付宝";
        }else if("3".equals(payMethod)){
            payMethod="IC卡";
        }
        this.payMethod = payMethod;
    }

}
