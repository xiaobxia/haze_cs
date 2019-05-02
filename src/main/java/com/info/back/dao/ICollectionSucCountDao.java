package com.info.back.dao;

import com.info.back.vo.CollectionSucCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface ICollectionSucCountDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CollectionSucCount record);

    int insertSelective(CollectionSucCount record);

    CollectionSucCount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollectionSucCount record);

    int updateByPrimaryKey(CollectionSucCount record);

    CollectionSucCount selectByLevelAndName(@Param("level") String level, @Param("name") String name);

    CollectionSucCount selectByGroupAndNameAndCreateDate(HashMap<String, Object> params);
}