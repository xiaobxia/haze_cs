package com.info.back.vo;

import java.util.Date;

public class OrderLifeCycle {
    private Integer id;

    private Integer loanId;

    private Integer payId;

    private Date s1Time;

    private Byte currentLevel;

    private Byte currentStatus;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Date getS1Time() {
        return s1Time;
    }

    public void setS1Time(Date s1Time) {
        this.s1Time = s1Time;
    }

    public Byte getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Byte currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Byte getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Byte currentStatus) {
        this.currentStatus = currentStatus;
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

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }
}