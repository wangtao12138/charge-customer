package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 支付宝充电参数
 */
@Getter
@Setter
public class AlipayChargeVo implements Serializable{
    private static final long serialVersionUID = -1541826630526145412L;

    @NotBlank(message = "customerGuid不能为空")
    private String customerGuid;

    /**
     * 支付宝用户id
     */
    @NotBlank(message = "alipayUserId不能为空")
    private String alipayUserId;

    /**
     * 充电方案唯一标识
     */
    @NotBlank(message = "schemeGuid不能为空")
    private String schemeGuid;

    /**
     * 充电桩设备编号
     */
    @NotBlank(message = "deviceNo不能为空")
    private String deviceNo;
}
