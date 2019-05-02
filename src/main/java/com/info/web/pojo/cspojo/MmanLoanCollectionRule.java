package com.info.web.pojo.cspojo;


import lombok.Data;

/**
 * 催收规则分配表，和催收员表中数据一并显示，关联催收公司表
 */
@Data
public class MmanLoanCollectionRule {
    //
    private String id;
    /** 催收公司id */
    private String companyId;
    /** 催收组 */
    private String collectionGroup;
    /** 每人每天单数上限(单数平均分配，0代表无上限) */
    private Integer everyLimit;
    /** 催收公司名称 companyName */
    private String companyName;
    /** 当前人数 */
    private String personCount;
    /** 分单类型 */
    private String assignType;

}