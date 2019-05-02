package com.info.back.service;

import java.util.HashMap;
import java.util.List;

import com.info.back.dao.IBackRoleModuleDao;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.web.pojo.cspojo.BackRoleModule;

@Service
public class BackRoleModuleServiceImpl implements IBackRoleModuleService {
	@Resource
	private IBackRoleModuleDao backRoleModuleDao;

	@Override
	public List<BackRoleModule> findAll(HashMap<String, Object> params) {
		return backRoleModuleDao.findAll(params);
	}

	@Override
	public void insertModuleRole(HashMap<String, Object> params) {
		backRoleModuleDao.insertModuleRole(params);
	}

}
