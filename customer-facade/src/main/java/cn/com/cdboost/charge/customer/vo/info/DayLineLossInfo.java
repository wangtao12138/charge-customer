package cn.com.cdboost.charge.customer.vo.info;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wt
 * @desc
 * @create in  2018/8/11
 **/
@Getter
@Setter
public class DayLineLossInfo implements Serializable{

    private List<DayLineLossInfoList> list;
    @JSONField(name = "import")
    private List<DayLineLossInfoimport> importlist;


}
