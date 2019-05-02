package com.info.back.dao;

import com.info.web.pojo.cspojo.SmsLog;
import org.springframework.stereotype.Repository;

@Repository
public interface ISmsLogDao {
	public int insert(SmsLog smsLog);
}
