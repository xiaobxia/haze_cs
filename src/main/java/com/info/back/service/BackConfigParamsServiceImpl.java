package com.info.back.service;

import java.util.HashMap;
import java.util.List;

import com.info.web.pojo.cspojo.BackConfigParams;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.back.dao.IBackConfigParamsDao;

@Service
public class BackConfigParamsServiceImpl implements IBackConfigParamsService {

	@Resource
	IBackConfigParamsDao backConfigParamsDao;

	@Override
	public List<BackConfigParams> findParams(HashMap<String, Object> params) {

		return backConfigParamsDao.findParams(params);
	}

	@Override
	public int updateValue(List<BackConfigParams> list, String type) {
		int result = backConfigParamsDao.updateValue(list);
		if (result > 0) {
		}
		return result;
	}
}
