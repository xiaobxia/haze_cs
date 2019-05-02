package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;

/**
  催收机构（公司、本公司不可删除）
*/
@Data
public class MmanLoanCollectionCompany {
	// 
	private String id;
	/** 催单公司名称 */
	private String title;
	/** 创建时间 */
	private Date createDate;
	/** 优先级，优先级越高，优先分配订单（暂时不提供） */
	private String priority;
	/** 状态，1启用，0禁用 */
	private String status;
	/** 是否是自营团队，1是，0不是 */
	private String selfBusiness;
	/** 修改时间 */
	private Date updateDate;
	/** 地区 */
	private String region;
	/** 人数 （与数据库无关） */
	private Integer peopleCount;

}