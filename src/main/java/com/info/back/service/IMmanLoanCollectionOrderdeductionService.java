package com.info.back.service;

import java.util.HashMap;
import java.util.List;

import com.info.back.result.JsonResult;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrderdeduction;
import com.info.web.util.PageConfig;

public interface IMmanLoanCollectionOrderdeductionService {
	/**
	 * 
	 * 减免申请
	 * @param params
	 * @return
	 */
	public  JsonResult saveorderdeduction(HashMap<String, Object> params);
	/**
	 *  减免记录列表
	 * @param params
	 * @return
	 */
	public PageConfig<MmanLoanCollectionOrderdeduction> findPage(HashMap<String, Object> params);

	/**
	 * 单条查询
	 * 
	 */
    public List<MmanLoanCollectionOrderdeduction> findAllList(String id);
}
