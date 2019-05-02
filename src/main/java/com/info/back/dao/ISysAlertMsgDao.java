package com.info.back.dao;

import com.info.web.pojo.cspojo.SysAlertMsg;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysAlertMsgDao {
	
	public void insert(SysAlertMsg sysAlertMsg);

	public int updateTogAlertMsg(String alertId);

}
