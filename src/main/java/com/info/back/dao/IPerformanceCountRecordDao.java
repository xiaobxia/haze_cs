package com.info.back.dao;

import com.info.back.vo.PerformanceTotalResult;
import com.info.back.vo.PerformanceCountRecord;

import java.util.HashMap;
import java.util.List;

public interface IPerformanceCountRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PerformanceCountRecord record);

    int insertSelective(PerformanceCountRecord record);

    PerformanceCountRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PerformanceCountRecord record);

    int updateByPrimaryKey(PerformanceCountRecord record);

    List<PerformanceCountRecord> selectByCountDateAndName(HashMap<String, Object> params);

    int findAllCount(HashMap<String, Object> params);

    PerformanceTotalResult getTotal(HashMap<String, Object> params);

    List<PerformanceCountRecord> findAll(HashMap<String, Object> params);
}