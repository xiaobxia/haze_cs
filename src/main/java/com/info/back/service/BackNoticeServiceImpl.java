package com.info.back.service;

import java.util.HashMap;
import java.util.List;

import com.info.back.dao.IBackNoticeDao;
import com.info.back.dao.IPaginationDao;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackNotice;
import com.info.web.util.PageConfig;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BackNoticeServiceImpl implements IBackNoticeService {
	@Resource
	private IBackNoticeDao backNoticeDao;
	@Resource
	private IPaginationDao paginationDao;

	@Override
	public int deleteById(Integer id) {
		return backNoticeDao.deleteById(id);
	}

	@Override
	public BackNotice findById(Integer id) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("id", id);
		List<BackNotice> list = backNoticeDao.findParams(params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public BackNotice findByCode(String noticeCode) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("noticeCode", noticeCode);
		List<BackNotice> list = backNoticeDao.findParams(params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public PageConfig<BackNotice> findPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "BackNotice");
		return paginationDao.findPage("findParams", "findParamsCount", params,
				null);
	}

	@Override
	public int insert(BackNotice backNotice) {
		return backNoticeDao.insert(backNotice);
	}

	@Override
	public int updateById(BackNotice backNotice) {
		return backNoticeDao.update(backNotice);
	}

}
