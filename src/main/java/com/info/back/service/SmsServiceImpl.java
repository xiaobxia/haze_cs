package com.info.back.service;

import java.util.HashMap;

import com.info.constant.Constant;
import com.info.back.dao.IPaginationDao;
import com.info.back.dao.ISmsDao;
import com.info.web.util.PageConfig;


import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.web.pojo.cspojo.InfoSms;


@Service
public class SmsServiceImpl implements ISmsService {

	
	@Resource
	private IPaginationDao<InfoSms> paginationDao;
	
	@Resource
	private ISmsDao smsDao;
	
	@Override
	public PageConfig<InfoSms> findPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "Sms");
		return paginationDao.findPage("findAll", "findAllCount", params,null);
	}

}
