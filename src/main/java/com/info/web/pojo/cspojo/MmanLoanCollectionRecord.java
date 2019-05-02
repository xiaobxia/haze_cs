package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 催收记录表
 */
@Data
public class MmanLoanCollectionRecord {
    private String id;
    /** 催收订单ID（借款编号） */
    private String orderId;
    /** 催收员ID */
    private String collectionId;
    /** 借款人ID */
    private String userId;
    /** 联系人类型 1: 紧急联系人 2:通讯录联系人 */
    private String contactType;
    /** 联系人姓名 */
    private String contactName;
    /** 联系人关系 */
    private String relation;
    /** 联系人电话 */
    private String contactPhone;
    /** 施压等级(施压等级一、二、三 字典) */
    private String stressLevel;
    /** 当前催收状态 0待催收、1催收中、2承诺还款、3委外中、4委外成功、5催收成功 字典 */
    private String orderState;
    /** 催收类型( 1电话催收、2短信催收（可点击发短信）字典) */
    private String collectionType;
    /** 催收时间 */
    private Date collectionDate;
    /** 催收组 */
    private String collectionGroup;
    /** 催收员 */
    private String collectionPerson;
    /** 催收到的金额 */
    private BigDecimal collectionAmount;
    /** 催收内容 */
    private String content;
    /** 创建时间 */
    private Date createDate;
    /** 更新时间 */
    private Date updateDate;
    /** 备注（以备催收人员查阅） */
    private String remark;

    private String overdueDays;

    /** 还款情况 */
    private String repaymentDescript;

    /** 减免情况 */
    private String reductionDescript;

}