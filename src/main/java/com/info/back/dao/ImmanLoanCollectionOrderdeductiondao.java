package com.info.back.dao;

import java.util.List;

import com.info.web.pojo.cspojo.MmanLoanCollectionOrderdeduction;
import org.springframework.stereotype.Repository;

@Repository
public interface ImmanLoanCollectionOrderdeductiondao {
/**
 *    减免
 * @param record
 * @return
 */
    public  int insertSelective(MmanLoanCollectionOrderdeduction record);
    
    /**
     * 根据借款ID 查询单条
     * 
     */
    public List<MmanLoanCollectionOrderdeduction> findAllList(String id);
   
}
