package com.info.back.dao;

import com.info.back.vo.OdvRate;

import java.util.List;

public interface IOdvRateDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OdvRate record);

    int insertSelective(OdvRate record);

    OdvRate selectByPrimaryKey(Integer id);

    List<OdvRate> selectByGroupLevel(Integer level);

    int updateByPrimaryKeySelective(OdvRate record);

    int updateByPrimaryKey(OdvRate record);

    int selectByOdvId(Integer odvId);
}