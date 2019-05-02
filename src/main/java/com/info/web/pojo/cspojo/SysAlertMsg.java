package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统消息Entity
 * @author wayne
 * @version 2015-11-03
 */
@Data
public class SysAlertMsg{
	/** 普通通知*/
	public static final String TYPE_COMMON = "0";
	/** 待办任务*/
	public static final String TYPE_TODOTASK = "1";
	/** 授信中心通知*/
	public static final String TYPE_CREDIT = "2";
	/** 借款中心通知*/
	public static final String TYPE_LOAN = "3";
	/** 放款通知*/
	public static final String TYPE_LOAN_GRANT = "4";
	/** 逾期通知*/
	public static final String TYPE_LOAN_OVERDUE = "5";
	/** 还款通知*/
	public static final String TYPE_LOAN_RECEIVBLE = "6";
	/** 贷款申请通知*/
	public static final String TYPE_CREDIT_LOAN_APPLY = "7";
	/** 业务员报件通知*/
	public static final String TYPE_BUSINESS_DECLARATION = "8";
	private String id;
	/** 标题*/
	private String title;
	/** 内容*/
	private String content;
	/** 类型 0:普通消息,1:待办任务,2:授信中心通知,3:借款中心通知,4:放款通知 ,5:逾期通知,6:还款通知 ,7:贷款申请通知 ,8:业务员报件通知*/
	private String type;
	/** 状态 0--未读 1.已读*/
	private String status;
	/** 是否可处理 0--可处理 1--不可处理*/
	private String dealStatus;
	/** 是否已经发送 0--未发送， 1：已发送*/
	private String hasPush;
	/** 接收人*/
	private String userId;
	/** 处理或者查看时间*/
	private Date dueDate;
	/** 关联的业务ID*/
	private String businessId;
	/** 关联的业务的编号*/
	private String businessNum;
	/** 关联的流程任务Id*/
	private String taskId;
    private Date createDate;
    private Date updateDate;
	private Map<String, Object> variables = new HashMap<>();

}