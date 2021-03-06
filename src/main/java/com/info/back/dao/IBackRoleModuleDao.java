package com.info.back.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.info.web.pojo.cspojo.BackRoleModule;

/**
 * 
 * 类描述：角色菜单dao层 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 下午01:53:41 <br>
 * 
 * @version
 * 
 */
@Repository
public interface IBackRoleModuleDao {
	/**
	 * 根据条件查询角色菜单对象
	 * 
	 * @param map
	 * @return 角色list
	 */
	public List<BackRoleModule> findAll(HashMap<String, Object> params);

	/**
	 * 根据角色ID删除
	 * 
	 * @param roleId
	 */
	public void deleteByRoleId(Integer roleId);

	/**
	 * 插入角色菜单关联信息
	 * 
	 * @param params
	 *            rightIds要插入的菜单id集合<br>
	 *            id 要出的角色ID
	 */
	public void insertModuleRole(HashMap<String, Object> params);
}
