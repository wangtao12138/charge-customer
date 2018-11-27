package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;

/**
 * @author wt
 * @desc
 * @create in  2018/8/16
 **/
@Getter
@Setter
public class ChargeCustomerInfoDetailInfo implements Serializable{
    private String customerContact;
    private String customerState;
    private String remainAmount;
    private String chargeCount;
    private String remainCnt;
    private String expireTime;
    private String alipayUserId;
    private String webcharNo;
    private String cardId;
    private String communityName;
    private String remark;


}