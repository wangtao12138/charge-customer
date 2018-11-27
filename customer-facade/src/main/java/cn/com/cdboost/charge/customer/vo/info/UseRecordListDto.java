package cn.com.cdboost.charge.customer.vo.info;

import cn.com.cdboost.charge.customer.vo.param.QueryListParamDate;

import javax.validation.constraints.NotNull;

/**
 * @author wt
 * @desc
 * @create in  2018/8/16
 **/
public class UseRecordListDto extends QueryListParamDate {
    @NotNull(message = "customerGuid 不能为null")
    private String customerGuid;


    private String deviceNo;

    public String getCustomerGuid() {
        return customerGuid;
    }

    public void setCustomerGuid(String customerGuid) {
        this.customerGuid = customerGuid;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    @Override
    protected String defaultSortName() {
        return null;
    }

    @Override
    protected String defaultSortOrder() {
        return null;
    }
}
