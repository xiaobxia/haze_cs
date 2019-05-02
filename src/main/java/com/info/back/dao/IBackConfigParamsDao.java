package com.info.back.dao;

import java.util.HashMap;
import java.util.List;

import com.info.web.pojo.cspojo.BackConfigParams;
import org.springframework.stereotype.Repository;

@Repository
public interface IBackConfigParamsDao {
	/**
	 * 
	 * @param params
	 *            sysType参数分类
	 * @return
	 */
	public List<BackConfigParams> findParams(HashMap<String, Object> params);

	/**
	 * 更新
	 * 
	 * @param list
	 * @return
	 */
	public int updateValue(List<BackConfigParams> list);
}
