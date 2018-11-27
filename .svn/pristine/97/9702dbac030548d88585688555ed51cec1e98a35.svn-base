package cn.com.cdboost.charge.customer.common;

import java.io.Serializable;
import java.util.List;

/**
 * Service接口基类
 */
public interface BaseService<T extends Serializable> {
    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Object id);

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     * @param record
     * @return
     */
    int insert(T record);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     * @param record
     * @return
     */
    int insertSelective(T record);

    int insertList(List<T> list);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性
     * @param id
     * @return
     */
    T selectByPrimaryKey(Object id);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     * @param record
     * @return
     */
    T selectOne(T record);

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     * @param record
     * @return
     */
    List<T> select(T record);

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     * @param record
     * @return
     */
    int selectCount(T record);

    /**
     * 根据主键更新属性不为null的值
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 根据主键更新实体全部字段，null值会被更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(T record);
}
