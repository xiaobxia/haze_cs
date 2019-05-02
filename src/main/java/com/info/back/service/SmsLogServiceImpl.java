package com.info.back.service;

import java.util.HashMap;

import com.info.back.dao.ISmsLogDao;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.SmsLog;
import com.info.web.util.PageConfig;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.back.dao.IPaginationDao;

@Service
public class SmsLogServiceImpl implements ISmsLogService {
	@Resource
	private IPaginationDao paginationDao;
	@Resource
	private ISmsLogDao smsLogDao;

	@Override
	public PageConfig<SmsLog> findPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "SmsLog");
		return paginationDao.getMyPage("findAll", "findAllCount", params, null);
	}

	@Override
	public int insert(SmsLog smsLog) {
		return smsLogDao.insert(smsLog);
	}

}
