package cn.com.cdboost.charge.customer.vo.info;



import cn.com.cdboost.charge.customer.vo.param.QueryListParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author wt
 * @desc
 * @create in  2018/8/15
 **/
@Setter
@Getter
public class CustomerInfoListDto extends QueryListParam {
    @NotBlank(message = "customerState不能为null")
    private String customerState;

    private String webcharNo;

    private String alipayUserId;

    private String customerName;

    private String alipayNickName;

    private String customerContact;

    private String nodeId;

    private Integer nodeType;

    private List<String> proGuids;



    @Override
    protected String defaultSortName() {
        return null;
    }

    @Override
    protected String defaultSortOrder() {
        return null;
    }


}
