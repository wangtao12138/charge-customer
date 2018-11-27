package cn.com.cdboost.charge.customer.service;

import cn.com.cdboost.charge.base.vo.PageData;
import cn.com.cdboost.charge.customer.common.BaseService;
import cn.com.cdboost.charge.customer.model.CustomerPayCard;
import cn.com.cdboost.charge.customer.vo.param.IncomePageQueryParam;
import cn.com.cdboost.charge.customer.vo.param.IncomeQueryParam;

import java.util.List;

/**
 * 客户充值ic卡单表接口
 */
public interface ICustomerPayCardService extends BaseService<CustomerPayCard>{
    //查询站点某时间段的所有ic卡充值收入
    List<CustomerPayCard> queryCustomerPayCard(IncomeQueryParam queryParam);

    //分页查询站点ic卡某天充值收入
    PageData<CustomerPayCard> queryCustomerPayCardPage(IncomePageQueryParam queryParam);

    // 查询最近N条IC卡充值记录
    List<CustomerPayCard> queryRecentNList(String cardId,Integer number);
}
