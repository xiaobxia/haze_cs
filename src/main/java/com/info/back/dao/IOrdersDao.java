package com.info.back.dao;

import com.info.web.pojo.cspojo.Orders;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrdersDao {
	/**
	 * 发出请求
	 * 
	 * @param zbNews
	 * @return
	 */
	public int insert(Orders orders);

	/**
	 * 回调更新
	 * 
	 * @param Integer
	 * @return
	 */
	public int updateByOrderNo(Orders orders);

	/**
	 * 根据主键查询
	 * 
	 * @param id
	 * @return
	 */
	public Orders findById(Integer id);
}
