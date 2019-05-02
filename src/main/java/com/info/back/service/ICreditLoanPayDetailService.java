package com.info.back.service;


import java.util.List;

import com.info.web.pojo.cspojo.CreditLoanPayDetail;

public interface ICreditLoanPayDetailService {
	
	
	public int saveNotNull(CreditLoanPayDetail creditloanpaydetail);
	
	
	public int deleteid(String id);


	public List<CreditLoanPayDetail> findPayDetail(String payId);
	
	

}
