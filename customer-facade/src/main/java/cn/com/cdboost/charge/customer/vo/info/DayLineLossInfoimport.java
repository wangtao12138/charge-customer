package cn.com.cdboost.charge.customer.vo.info;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wt
 * @desc
 * @create in  2018/8/11
 **/
@Getter
@Setter
public class DayLineLossInfoimport implements Serializable{
    private String date;
    private String voltageA;
    private String currentA;


}
