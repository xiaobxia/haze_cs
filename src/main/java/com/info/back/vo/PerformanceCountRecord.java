package com.info.back.vo;

import com.info.web.util.DateUtil;

import java.util.Date;

public class PerformanceCountRecord {
    private Integer id;

    private String userName;

    private String groupLevel;

    private String tel;

    private Integer sysOrder;

    private Integer outOrder;

    private Integer handOrder;

    private Integer returnPrincipal;

    private Integer renewalCount;

    private Integer fee;

    private Date countDate;

    private Integer totalIntoOrder;

    private Integer totalOutOrder;

    private Double sucRate;

    private Date createDate;

    private Date updateDate;

    private String mapK;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public Integer getSysOrder() {
        return sysOrder;
    }

    public void setSysOrder(Integer sysOrder) {
        this.sysOrder = sysOrder;
    }

    public Integer getOutOrder() {
        return outOrder;
    }

    public void setOutOrder(Integer outOrder) {
        this.outOrder = outOrder;
    }

    public Integer getReturnPrincipal() {
        return returnPrincipal;
    }

    public void setReturnPrincipal(Integer returnPrincipal) {
        this.returnPrincipal = returnPrincipal;
    }

    public Integer getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(Integer renewalCount) {
        this.renewalCount = renewalCount;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Date getCountDate() {
        return countDate;
    }

    public void setCountDate(Date countDate) {
        this.countDate = countDate;
    }

    public Integer getTotalIntoOrder() {
        return totalIntoOrder;
    }

    public void setTotalIntoOrder(Integer totalIntoOrder) {
        this.totalIntoOrder = totalIntoOrder;
    }

    public Integer getTotalOutOrder() {
        return totalOutOrder;
    }

    public void setTotalOutOrder(Integer totalOutOrder) {
        this.totalOutOrder = totalOutOrder;
    }

    public Double getSucRate() {
        return sucRate;
    }

    public void setSucRate(Double sucRate) {
        this.sucRate = sucRate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel;
    }

    public Integer getHandOrder() {
        return handOrder;
    }

    public void setHandOrder(Integer handOrder) {
        this.handOrder = handOrder;
    }

    public String getMapK() {
        return String.join("", this.userName, DateUtil.getDateFormat(this.countDate, "yyyy-MM-dd"));
    }

    public void setMapK(String mapK) {
        this.mapK = mapK;
    }
}