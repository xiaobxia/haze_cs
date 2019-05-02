package com.info.back.vo.jxl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Remark {
    private Integer id;
    private Integer remarkFlag; //备注标志
    private String remarkContent; //备注内容
    private Date createTime;
    private Date updateTime;
    private Integer assignId; //订单分配id
    private String jobPhone; //客服电话
    private String jobName;//客服名称

    public Integer getAssignId() {
        return assignId;
    }

    public void setAssignId(Integer assignId) {
        this.assignId = assignId;
    }

    public String getJobPhone() {
        return jobPhone;
    }

    public void setJobPhone(String jobPhone) {
        this.jobPhone = jobPhone;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public static final Integer ON_PHONE = 1; //在通话中
    public static final Integer PHONE_SHUT = -1; //已关机
    public static final Integer USER_REJECT = -2; //用户不接电话
    public static final Integer DEAL_BORROW_NOW = 2; //立即处理还款
    public static final Integer TOMORROW_PAY = 3; //明天还款
    public static final Integer BEFORE_NIGHT_TWELVE = 4; //晚上12点前处理还款
    public static final Map<Integer, String> BORROW_REMARK_MAP = new HashMap<>();
    static {
        BORROW_REMARK_MAP.put(ON_PHONE,"通话中");
        BORROW_REMARK_MAP.put(PHONE_SHUT,"已关机");
        BORROW_REMARK_MAP.put(USER_REJECT,"用户按掉");
        BORROW_REMARK_MAP.put(DEAL_BORROW_NOW,"立即处理还款");
        BORROW_REMARK_MAP.put(TOMORROW_PAY,"明天还款");
        BORROW_REMARK_MAP.put(BEFORE_NIGHT_TWELVE,"晚上12点钱处理还款");
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRemarkFlag() {
        return remarkFlag;
    }

    public void setRemarkFlag(Integer remarkFlag) {
        this.remarkFlag = remarkFlag;
    }

    public String getRemarkContent() {
        return remarkContent;
    }

    public void setRemarkContent(String remarkContent) {
        this.remarkContent = remarkContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
