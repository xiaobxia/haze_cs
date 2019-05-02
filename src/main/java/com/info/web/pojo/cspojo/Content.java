package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

@Data
public class Content {
	private Integer id;
	private String channelType;
	private String contentTitle;
	private String contentSummary;
	private Date addTime;
	private Integer addUserId;
	private User addUser;
	private String addIp;
	private Integer updateUserId;
	private User updateUser;
	private String contentTxt;
	private Date updateTime;
	private Integer viewCount;
	private Integer isDeleted;
	private String remark;
	private Integer orderNum;
	private String fromUrl;
	
}
