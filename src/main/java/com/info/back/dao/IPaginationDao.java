package com.info.back.dao;

import java.util.HashMap;

import com.info.web.util.PageConfig;

/**
 * 分页
 * 
 * @author gaoyuhai 2016-6-22 下午03:53:18
 * @param <T>
 */
public interface IPaginationDao<T> {

	/**
	 * 分页方法
	 * 
	 *            在调用处需要手动传入nameSpace的只，且遵循com.info.back.dao.I+Constant.NAME_SPACE
	 *            +Dao.规则，不传入需要保证，listSql和countSql全局唯一<br>
	 *            type不为空则是 com.info.web.dao开头(前台)
	 */
	PageConfig<T> findPage(final String listSql, final String countSql,
						   final HashMap map, String type);

	/**
	 * 数据量较大时，自己拼装sql语句
	 * 
	 */
	PageConfig getMyPage(String listId, String countId, HashMap map, String type);
}
