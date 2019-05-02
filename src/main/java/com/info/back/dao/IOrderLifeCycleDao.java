package com.info.back.dao;

import com.info.back.vo.OrderLifeCycle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;

@Repository
public interface IOrderLifeCycleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderLifeCycle record);

    int insertSelective(OrderLifeCycle record);

    OrderLifeCycle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderLifeCycle record);

    int updateByPrimaryKey(OrderLifeCycle record);

    OrderLifeCycle findByLoanIdAndGroup(HashMap<String, Object> lifeMap);

    Integer findHigherCount(@Param("loanId") String loanId, @Param("s1Time") Date s1Time, @Param("level") String level);

}