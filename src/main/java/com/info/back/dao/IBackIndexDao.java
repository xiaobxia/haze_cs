package com.info.back.dao;

import com.info.web.pojo.cspojo.IndexPage;
import org.springframework.stereotype.Repository;

/**
 * 
 * 类描述：菜单dao层 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 下午01:53:41 <br>
 * 
 * @version 
 * 
 */
@Repository
public interface IBackIndexDao {
	public void update(IndexPage zbIndex);

	public IndexPage searchIndex();
}
