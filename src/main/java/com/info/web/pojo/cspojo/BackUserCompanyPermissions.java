package com.info.web.pojo.cspojo;

import lombok.Data;

/**
 * 用户所以查询公司信息的关联关系表
 * @author Administrator
 */
@Data
public class BackUserCompanyPermissions {
	private Integer id;
	private Integer userId;
	private String companyId;
}
