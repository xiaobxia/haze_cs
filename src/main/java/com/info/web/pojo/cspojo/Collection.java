package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;


/**
 * 催收员实体用户 没有对应的表 信息保存在back_user 中
 * @author xieyaling
 * @date 2017-02-15
 */
@Data
public class Collection {
	private Integer id;
	private String userAccount;
	private String userPassword;
	private String userName;
	private String userSex;
	private String userAddress;
	private String userTelephone;
	private String userMobile;
	private String userEmail;
	private String userQq;
	private Date createDate;
	private Date updateDate;
	private String addIp;
	private String remark;
	private String userStatus;
	/**
	 * 公司编号
	 */
	private String companyId;
	/**
	 * 催收组
	 */
	private String groupLevel;
	private String uuid;
	private String companyName;
	private String roleId;
}
