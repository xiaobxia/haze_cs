package com.info.back.vo;

import java.util.Date;

public class OdvRate {

    public static final String HAND = "hand";

    public static final String AUTO = "auto";

    private Integer id;

    private Integer odvId;

    private String companyId;

    private String groupLevel;

    private Double assignRate;

    private Date createTime;

    private Date updateTime;

    /*催收员姓名*/
    private String odvName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOdvId() {
        return odvId;
    }

    public void setOdvId(Integer odvId) {
        this.odvId = odvId;
    }

    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel == null ? null : groupLevel.trim();
    }

    public Double getAssignRate() {
        return assignRate;
    }

    public void setAssignRate(Double assignRate) {
        this.assignRate = assignRate;
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

    public String getOdvName() {
        return odvName;
    }

    public void setOdvName(String odvName) {
        this.odvName = odvName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}