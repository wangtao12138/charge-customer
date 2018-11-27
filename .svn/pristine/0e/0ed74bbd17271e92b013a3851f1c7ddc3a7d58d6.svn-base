package cn.com.cdboost.charge.customer.common;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * Service接口基类实现类
 */
public class BaseServiceImpl<T extends Serializable> implements BaseService<T> {
    @Autowired
    private CommonMapper<T> commonMapper;

    @Override
    public int deleteByPrimaryKey(Object id) {
        return commonMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T record) {
        return commonMapper.insert(record);
    }

    @Override
    public int insertSelective(T record) {
        return commonMapper.insertSelective(record);
    }

    @Override
    public int insertList(List<T> list) {
        return commonMapper.insertList(list);
    }

    @Override
    public T selectByPrimaryKey(Object id) {
        return commonMapper.selectByPrimaryKey(id);
    }

    @Override
    public T selectOne(T record) {
        return commonMapper.selectOne(record);
    }

    @Override
    public List<T> select(T record) {
        return commonMapper.select(record);
    }

    @Override
    public int selectCount(T record) {
        return commonMapper.selectCount(record);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return commonMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(T record) {
        return commonMapper.updateByPrimaryKey(record);
    }
}
