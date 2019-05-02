package com.info.back.service;

import java.util.List;

import com.info.back.dao.IMmanLoanCollectionCompanyDao;
import com.info.web.pojo.cspojo.BackUser;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.web.pojo.cspojo.MmanLoanCollectionCompany;

@Service
public class MmanLoanCollectionCompanyServiceImpl implements IMmanLoanCollectionCompanyService{
	
	@Resource
	private IMmanLoanCollectionCompanyDao mmanLoanCollectionCompanyDao;

	@Override
	public List<MmanLoanCollectionCompany> getList(MmanLoanCollectionCompany mmanLoanCollectionCompany) {
		return mmanLoanCollectionCompanyDao.getList(mmanLoanCollectionCompany);
	}
	@Override
	public List<MmanLoanCollectionCompany> selectCompanyList() {
		return mmanLoanCollectionCompanyDao.selectCompanyList();
	}
	@Override
	public List<MmanLoanCollectionCompany> findCompanyByUserId(BackUser user) {
		//当前用户既不是催收员又无法查询所有公司数据
		Integer userId=null;
		if(!"10021".equals(user.getRoleId()) && "1".equals(user.getViewdataStatus())){
			userId = user.getId();
		}
		return mmanLoanCollectionCompanyDao.findCompanyByUserId(userId);
	}

}
