package com.info.back.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.info.web.pojo.cspojo.CreditLoanPayDetail;

@Repository
public interface ICreditLoanPayDetailDao {
	
	public int saveNotNull(CreditLoanPayDetail creditloanpaydetail);
	
	
	public int deleteByPayId(String id);


	public List<CreditLoanPayDetail> findPayDetail(String payId);
	
}
