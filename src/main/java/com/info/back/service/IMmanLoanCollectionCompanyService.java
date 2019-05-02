package com.info.back.service;

import java.util.List;

import com.info.web.pojo.cspojo.BackUser;
import com.info.web.pojo.cspojo.MmanLoanCollectionCompany;

public interface IMmanLoanCollectionCompanyService {
	
	/**
	 * 根据条件查询公司list
	 * @param mmanLoanCollectionCompany
	 * @return
	 */
	public List<MmanLoanCollectionCompany> getList(MmanLoanCollectionCompany mmanLoanCollectionCompany);
	/**
	 * 查询公司列表
	 * @return
	 */
	public List<MmanLoanCollectionCompany> selectCompanyList();
	
	/**
	 * 根据用户查询用户绑定的公司ID
	 */
	public List<MmanLoanCollectionCompany> findCompanyByUserId(BackUser user);
	
}
