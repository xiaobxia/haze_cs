package com.info.back.service;

import java.util.HashMap;
import java.util.List;

import com.info.constant.Constant;
import com.info.web.pojo.cspojo.MmanUserRela;
import com.info.back.dao.IPaginationDao;
import com.info.web.util.PageConfig;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.back.dao.IMmanUserRelaDao;

@Service
public class MmanUserRelaServiceImpl implements IMmanUserRelaService{
	@Resource
	private IMmanUserRelaDao mmanUserRelaDao;
	
	@Resource
	private IPaginationDao<MmanUserRela> paginationDao;
	
	@Override
	public int saveNotNull(MmanUserRela mmanUserRela) {
		return mmanUserRelaDao.saveNotNull(mmanUserRela);
	}

	@Override
	public PageConfig<MmanUserRela> findPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "MmanUserRela");
		return paginationDao.findPage("findAll", "findAllCount", params,null);
	}

	@Override
	public List<MmanUserRela> getList(HashMap<String, Object> params) {
		
		return mmanUserRelaDao.getList(params);
	}

	@Override
	public List<MmanUserRela> getContactPhones(String userId) {
		return mmanUserRelaDao.getContactPhones(userId);
	}

	@Override
	public PageConfig<MmanUserRela> findAllPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "MmanUserRela");
		return paginationDao.findPage("findAllList", "findAllPageCount", params,null);
	}

	@Override
	public MmanUserRela getUserRealByUserId(String  userRelaId) {
		return mmanUserRelaDao.getUserRealByUserId(userRelaId);
	}


}

