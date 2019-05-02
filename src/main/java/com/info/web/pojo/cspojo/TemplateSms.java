package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class TemplateSms {
	/** */
	private String id;
	/** 名称*/
	private String name;
	/** 短信内容*/
	private String contenttext;
	/**短信类型  3为S1组，4为 S2组，5为 M组*/
	private String msgType;
	/** 状态 1 可用 ，2 禁用*/
	private String status;
	/** */
	private Date createtime;
	/** */
	private Date updatetime;

}