package cn.com.cdboost.charge.customer.vo.info;

import cn.com.cdboost.charge.customer.vo.param.QueryListParamDate;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author wt
 * @desc
 * @create in  2018/8/16
 **/
public class ChargeRecordListDto extends QueryListParamDate {
    private Integer userId;
    @NotBlank(message ="customerGuid 不能为空")
    private String customerGuid;
    @NotBlank(message ="payMethod 不能为空")
    private String payMethod;
    @NotBlank(message ="payState 不能为空")
    private String payState;

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCustomerGuid() {
        return customerGuid;
    }

    public void setCustomerGuid(String customerGuid) {
        this.customerGuid = customerGuid;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
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
