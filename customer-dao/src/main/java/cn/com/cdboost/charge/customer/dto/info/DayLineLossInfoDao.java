package cn.com.cdboost.charge.customer.dto.info;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wt
 * @desc
 * @create in  2018/8/11
 **/
@Getter
@Setter
public class DayLineLossInfoDao {

    private List<DayLineLossInfoListDao> list;
    @JSONField(name = "import")
    private List<DayLineLossInfoimportDao> importlist;

}
