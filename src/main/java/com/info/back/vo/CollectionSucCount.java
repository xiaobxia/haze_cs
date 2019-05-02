package com.info.back.vo;

import java.math.BigDecimal;
import java.util.Date;

public class CollectionSucCount {
    private Integer id;

    private String groupLevel;

    private String userName;

    private Integer intoNum = 0;

    private Integer sucNum = 0;

    private Integer renewNum = 0;

    private Integer repayNum = 0;

    private BigDecimal sucRate;

    private Date createTime;

    private Date updateTime;

    private String mapK;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel == null ? null : groupLevel.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getIntoNum() {
        return intoNum;
    }

    public void setIntoNum(Integer intoNum) {
        this.intoNum = intoNum;
    }

    public Integer getSucNum() {
        return sucNum;
    }

    public void setSucNum(Integer sucNum) {
        this.sucNum = sucNum;
    }

    public Integer getRenewNum() {
        return renewNum;
    }

    public void setRenewNum(Integer renewNum) {
        this.renewNum = renewNum;
    }

    public Integer getRepayNum() {
        return repayNum;
    }

    public void setRepayNum(Integer repayNum) {
        this.repayNum = repayNum;
    }

    public BigDecimal getSucRate() {
        return sucRate;
    }

    public void setSucRate(BigDecimal sucRate) {
        this.sucRate = sucRate;
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

    public String getMapK() {
        return String.join("-", groupLevel, userName);
    }

    public void setMapK(String mapK) {
        this.mapK = mapK;
    }

}