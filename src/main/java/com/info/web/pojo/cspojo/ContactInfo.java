package com.info.web.pojo.cspojo;

import lombok.Data;

@Data
public class ContactInfo {
	
	// 借款人姓名
	private String loanUserName;
	// 借款人电话
	private String loanUserPhone;
	// 联系人姓名
	private String contactUserName;
	// 联系人电话
	private String contactUserPhone;
	// 联系人类型
	private String contactUserType;
	
}
