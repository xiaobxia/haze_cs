package com.info.back.service;

import com.info.back.dao.ICreditLoanPayDao;
import com.info.back.utils.IdGen;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.web.pojo.cspojo.CreditLoanPay;

@Service
public class CreditLoanPayServiceImpl implements ICreditLoanPayService{

	@Resource
    ICreditLoanPayDao creditLoanPayDao;
	
	@Override
    public CreditLoanPay findByLoanId(String loanId){
		return creditLoanPayDao.findByLoanId(loanId);
	}

	@Override
    public void save(CreditLoanPay creditLoanPay) {
		if(StringUtils.isBlank(creditLoanPay.getId())){
			creditLoanPay.setId(IdGen.uuid());
			creditLoanPayDao.insertCreditLoanPay(creditLoanPay);
		}else{
			creditLoanPayDao.updateCreditLoanPay(creditLoanPay);
		}
	}

	@Override
	public int saveNotNull(CreditLoanPay creditLoanPay) {
		return creditLoanPayDao.saveNotNull(creditLoanPay);
	}

	@Override
	public int findcount(String id) {
		return creditLoanPayDao.findcount(id);
	}

	@Override
	public int updateNotNull(CreditLoanPay creditLoanPay) {
		return creditLoanPayDao.updateNotNull(creditLoanPay);
	}

	@Override
	public CreditLoanPay get(String payId) {
		return creditLoanPayDao.get(payId);
	}

	@Override
	public void updateCreditLoanPay(CreditLoanPay creditLoanPay) {
		// TODO Auto-generated method stub
		creditLoanPayDao.updateCreditLoanPay(creditLoanPay);
	}
}
