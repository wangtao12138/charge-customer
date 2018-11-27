package cn.com.cdboost.charge.customer.vo.param;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付宝回调通知参数
 */
@Getter
@Setter
public class ZfbPayNoticeParam implements Serializable{
    private static final long serialVersionUID = -4590550315173478887L;

    /**
     * 回调通知的返回的数据，这个用于验证签名用，签名不正确的，签名通过才进行后续逻辑处理
     */
    private Map<String,String> params;

    /**
     * 系统订单号
     */
    private String outTradeNo;

    /**
     * 支付总金额
     */
    private BigDecimal totalAmount;

    /**
     * appId
     */
    private String appId;

    /**
     * 交易状态
     */
    private String tradeStatus;
}
