package com.info.back.dao;

import com.info.back.vo.CollectionSucCount;
import com.info.back.vo.CollectionSucRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICollectionSucRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CollectionSucRecord record);

    int insertSelective(CollectionSucRecord record);

    CollectionSucRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CollectionSucRecord record);

    int updateByPrimaryKey(CollectionSucRecord record);

    List<CollectionSucCount> findSucCount(@Param("startTime") String startTime, @Param("endTime") String endTime);
}