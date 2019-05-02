package com.info.back.service;

import java.util.List;

import com.info.back.dao.ICreditLoanPayDetailDao;
import com.info.web.pojo.cspojo.CreditLoanPayDetail;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CreditLoanPayDetailServiceImpl implements ICreditLoanPayDetailService{
	
	@Resource
	private ICreditLoanPayDetailDao creditLoanPayDetailDao;

	@Override
	public int saveNotNull(CreditLoanPayDetail creditloanpaydetail) {
		return creditLoanPayDetailDao.saveNotNull(creditloanpaydetail);
	}

	@Override
	public int deleteid(String id) {
		return creditLoanPayDetailDao.deleteByPayId(id);
	}

	@Override
	public List<CreditLoanPayDetail> findPayDetail(String payId) {
		return creditLoanPayDetailDao.findPayDetail(payId);
	}

}
