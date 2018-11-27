package cn.com.cdboost.charge.customer.common;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 自己个性化的Mapper
 */
public interface CommonMapper<T> extends
        Mapper<T>,
        MySqlMapper<T>,
        IdsMapper<T>,
        IdListMapper<T,Object>,
        ConditionMapper<T> {

    default int deleteWithVersion(T t){
        int result = delete(t);
        if(result == 0){
            throw new RuntimeException("删除失败!");
        }
        return result;
    }

    default int deleteByPrimaryKeyWithVersion(T t) {
        int result = deleteByPrimaryKey(t);
        if(result == 0){
            throw new RuntimeException("删除失败!");
        }
        return result;
    }

    default int updateByPrimaryKeyWithVersion(T t){
        int result = updateByPrimaryKey(t);
        if(result == 0){
            throw new RuntimeException("更新失败!");
        }
        return result;
    }

    default int updateByPrimaryKeySelectiveWithVersion(T t){
        int result = updateByPrimaryKeySelective(t);
        if(result == 0){
            throw new RuntimeException("更新失败!");
        }
        return result;
    }

}
