package com.info.back.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.info.back.result.JsonResult;
import com.info.web.pojo.cspojo.AuditCenter;
import com.info.web.util.PageConfig;

public interface IAuditCenterService {
	/**
	 * 添加审核申请
	 * @param params
	 * @return
	 */
	public JsonResult svueAuditCenter(Map<String, String> params);
	/**
	 * 查询审核列表
	 * @param params
	 * @return
	 */
	public PageConfig<AuditCenter> findPage(HashMap<String, Object> params);
	
	public JsonResult updateAuditCenter(Map<String, String> params);
	/**
	 * 
	 * 减免申请
	 * @param params
	 * @return
	 */
	public  JsonResult saveorderdeduction(Map<String, Object> params) throws ParseException;
	
	public PageConfig<AuditCenter> findAllPage(HashMap<String, Object> params);

	public void updateSysStatus(Map<String, String> params);
}
