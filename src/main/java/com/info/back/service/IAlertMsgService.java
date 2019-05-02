package com.info.back.service;

import java.util.HashMap;

import com.info.web.pojo.cspojo.SysAlertMsg;
import com.info.back.result.JsonResult;
import com.info.web.util.PageConfig;

public interface IAlertMsgService {

	public void insert(SysAlertMsg alertMsg);

	public PageConfig<SysAlertMsg> findPage(HashMap<String, Object> params);

	public JsonResult updateTogAlertMsg(String alertId);
}
