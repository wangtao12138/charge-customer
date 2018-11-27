package cn.com.cdboost.charge.customer.vo.param;

import cn.com.cdboost.charge.base.vo.PageQueryParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 充电订单查询参数
 */
@Getter
@Setter
public class ChargeOrderQueryParam extends PageQueryParam{
    /**
     * 商户标识
     */
    private String merchantGuid;

    /**
     * 站点标识
     */
    private String projectGuid;

    /**
     * 客户唯一标识
     */
    private String customerGuid;

    /**
     * 充电桩唯一标识
     */
    private String chargingPlieGuid;

    /**
     * 充电方案标识
     */
    private String schemeGuid;

    /**
     * 开启方式 1-微信 2-支付宝 3-IC卡
     */
    private Integer openMeans;

    /**
     * 充电记录唯一标识
     */
    private String chargingGuid;

    /**
     * 开电状态
     */
    private Integer state;
}
