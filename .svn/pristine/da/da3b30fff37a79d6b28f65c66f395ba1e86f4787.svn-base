package cn.com.cdboost.charge.customer.dto.info;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wt
 * @desc
 * @create in  2018/8/16
 **/
@Setter
@Getter
public class ChargeRecordListDaoInfo {

    private String date;
    /**
     * '充值方式 1-微信 2-支付宝 3-现金 4-余额'
     */
    private String payMethod;

    private String payMoney;

    private String remainAmount;

    private String deviceNo;

    private String userId;
    /**
     *  '支付状态 0-待支付 1-支付成功',
     */
    private String payState;

    public void setPayState(String payState) {
        if("0".equals(payState)){
            payState="待支付";
        }else if("1".equals(payState)){
            payState="支付成功";
        }
        this.payState = payState;
    }


    public void setPayMethod(String payMethod) {
        if("1".equals(payMethod)){
            payMethod="微信";
        }else if("2".equals(payMethod)){
            payMethod="支付宝";
        }else if("3".equals(payMethod)){
            payMethod="现金";
        }else if("4".equals(payMethod)){
            payMethod="余额";
        }
        this.payMethod = payMethod;
    }


}
