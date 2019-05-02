package com.info.back.service;

import java.util.HashMap;
import java.util.List;

import com.info.back.dao.*;
import com.info.back.utils.RequestUtils;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackRole;
import com.info.web.pojo.cspojo.BackTree;
import com.info.web.pojo.cspojo.BackUserRole;
import com.info.web.util.PageConfig;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BackRoleServiceImpl implements IBackRoleService {
	@Resource
	private IBackRoleDao backRoleDao;
	@Resource
	private IPaginationDao paginationDao;
	@Resource
	private IBackUserRoleDao backUserRoleDao;
	@Resource
	private IBackRoleModuleDao backRoleModuleDao;
	@Resource
	private ISysDictDao sysDictDao;

	@Override
	public List<BackTree> findRoleTree(HashMap<String, Object> params) {
		if (params.containsKey("userId")
				&& Constant.ADMINISTRATOR_ID.intValue() == Integer.valueOf(
						String.valueOf(params.get("userId"))).intValue()) {
			return backRoleDao.findAdminRoleTree(params);
		} else {
			// showUserListByRoleId(id);
			return backRoleDao.findUserRoleTree(params);
		}
	}

	@Override
	public PageConfig<BackRole> findPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "BackRole");
		PageConfig<BackRole> pageConfig = new PageConfig<BackRole>();
		if (params.containsKey("userId")
				&& Constant.ADMINISTRATOR_ID.intValue() == Integer.valueOf(
						String.valueOf(params.get("userId"))).intValue()) {
			pageConfig = paginationDao.findPage("findAdminAll",
					"findAdminCount", params, null);
		} else {
			pageConfig = paginationDao.findPage("findUserAll", "findUserCount",
					params, null);
		}
		return pageConfig;

	}

	@Override
	public void deleteById(Integer id) {
		backRoleDao.deleteById(id);
		backUserRoleDao.deleteByRoleId(id);
		backRoleModuleDao.deleteByRoleId(id);
	}

	@Override
	public BackRole findById(Integer id) {
		return backRoleDao.findById(id);
	}

	@Override
	public void insert(BackRole backRole, Integer userId) {
		if (StringUtils.isBlank(backRole.getRoleAddip())) {
			backRole.setRoleAddip(RequestUtils.getIpAddr());
		}
		backRoleDao.insert(backRole);
		if (userId.intValue() != Constant.ADMINISTRATOR_ID.intValue()) {
			BackUserRole backUserRole = new BackUserRole();
			backUserRole.setRoleId(backRole.getId());
			backUserRole.setUserId(userId);
			backUserRoleDao.inserUserRole(backUserRole);
		}
	}

	@Override
	public void updateById(BackRole backRole) {
		backRoleDao.updateById(backRole);
	}

	@Override
	public List<BackTree> showUserListByRoleId(Integer id) {
		List<BackTree> treeList = null;
		List<Integer> roleChildIds = backRoleDao.showChildRoleListByRoleId(id);
		HashMap<String, Object> params = new HashMap<>();
		params.put("roleChildIds", roleChildIds);
		treeList = backRoleDao.showUserListByRoleId(params);
		return treeList;
	}

	@Override
	public List<BackTree> getTreeByRoleId(Integer roleId) {
		return backRoleDao.getTreeByRoleId(roleId);
	}

	@Override
	public void addRoleModule(HashMap<String, Object> params) {
		backRoleModuleDao.deleteByRoleId(Integer.valueOf(String.valueOf(params
				.get("id"))));
		String[] rightIds = (String[]) params.get("rightIds");
		if (null != rightIds && rightIds.length > 0) {
			backRoleModuleDao.insertModuleRole(params);
		}
	}

	@Override
	public List<Integer> showChildRoleListByRoleId(Integer id) {
		return backRoleDao.showChildRoleListByRoleId(id);
	}

	@Override
	public BackRole findByName(String roleName) {
		return backRoleDao.findByName(roleName);
	}

	@Override
	public BackRole getTopCsRole() {
		//总催收员角色名
		String csRoleName = sysDictDao.findDictByType("csRoleName").get(0).getValue();
		BackRole role = backRoleDao.findByName(csRoleName);
		return role;
	}
}
