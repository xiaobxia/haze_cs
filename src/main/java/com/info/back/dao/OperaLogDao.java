package com.info.back.dao;

import com.info.back.vo.OperaLog;

public interface OperaLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OperaLog record);

    int insertSelective(OperaLog record);

    OperaLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperaLog record);

    int updateByPrimaryKey(OperaLog record);
}