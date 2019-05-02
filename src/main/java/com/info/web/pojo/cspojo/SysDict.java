package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
  字典表
*/
@Data
public class SysDict {
	/** 编号*/
	private String id;
	/** 数据值*/
	private String value;
	/** 标签名*/
	private String label;
	/** 类型*/
	private String type;
	/** 描述*/
	private String description;
	/** 排序（升序）*/
	private BigDecimal sort;
	/** 父级编号*/
	private String parentId;
	/** 创建者*/
	private String createBy;
	/** 创建时间*/
	private Date createDate;
	/** 更新者*/
	private String updateBy;
	/** 更新时间*/
	private Date updateDate;
	/** 备注信息*/
	private String remarks;
	/** 删除标记*/
	private char delFlag;

}