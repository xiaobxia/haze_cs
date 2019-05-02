package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

/**
 * 
 * 类描述： <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 上午10:04:55 <br>
 * 
 * @version
 * 
 */
@Data
public class BackRole {
	private Integer id;

	private String roleName;

	private String roleSummary;

	private Integer roleSuper;

	private Date roleAddtime;

	private String roleAddip;


}