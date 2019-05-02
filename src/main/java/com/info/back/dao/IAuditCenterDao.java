package com.info.back.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.info.web.pojo.cspojo.AuditCenter;

@Repository
public interface IAuditCenterDao {

	int saveNotNull(AuditCenter auditCenter);

	AuditCenter findAuditId(String string);

	void updateStatus(Map<String, String> params);

	void updateSysStatus(Map<String, String> params);
	
	int saveUpdate(AuditCenter auditcenter);

	List<AuditCenter> findgetList();

	List<AuditCenter> selectGetList();

	Integer findAllCount(HashMap<String, Object> params);
}
