package com.info.web.pojo.cspojo;


import lombok.Data;

/**
  用户关系表
*/
@Data
public class MmanUserRela {
	/** */
	private String id;
	/** 借款用户id*/
	private String userId;
	/** 联系人类型（1 -直系亲属联系人、2-其他联系人，若为通讯录备份联系人这里不填）*/
	private String contactsKey;
	/** 联系人关系（直系：1 -父亲 、2-母亲、3-儿子、4-女儿、5-配偶；其他：1 -同学、2-亲戚、3-同事、4-朋友、5-其他，若为通讯录备份联系人这里不填）*/
	private String relaKey;
	/** 联系人姓名*/
	private String infoName;
	/** 联系人电话*/
	private String infoValue;
	/** 关联紧急联系人标记，1是0否（为通讯录备份联系人这里不填）*/
	private String contactsFlag;
	/** 删除标记（'1'-已删除，'0'正常）*/
	private String delFlag;
	/** 联系人类型字典值（这里不用）*/
	private String contactsValue;
	/** 联系人关系字典值（这里不用）*/
	private String relaValue;
	/** 这里不用*/
	private String kvVars;
	/** 归属地*/
	private String phoneNumLoc;
	/** 联系次数*/
	private Integer callCnt;
	/** 主叫次数*/
	private Integer callOutCnt;
	/** 被叫次数*/
	private Integer callInCnt;
	/** 联系时间（分）*/
	private String callLen;
	/** 主叫时间（分）*/
	private String callOutLen;
	/** 被叫时间（分）*/
	private String callInLen;
	
	private String realName;

}