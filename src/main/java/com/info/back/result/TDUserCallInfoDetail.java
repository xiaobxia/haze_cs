package com.info.back.result;

import java.util.Formatter;

/**
 * Author :${cqry_2017}
 * Date   :2017-11-07 11:09.
 */
public class TDUserCallInfoDetail {
    /**
     * 号码
     */
    private String tel;
    /**
     * 互联网标示
     */
    private String netIdentifier;
    /**
     * 归属地
     */
    private String callFrom;
//
    /**
     * 联系次数
     */
    private String concatCount;
//
    /**
     * 主叫次数
     */
    private String originatingCount;
    /**
     * 被叫次数
     */
    private String terminatingCount;
    /**
     *联系时间（分）
     */
    private String concatTime;
    /**
     *主叫时间（分）
     */
    private String originatingTime;
    /**
     *被叫时间（分）
     */
    private String terminatingTime;
    /**
     * 通讯录备注
     */
    private String telRemark;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNetIdentifier() {
        return netIdentifier;
    }

    public void setNetIdentifier(String netIdentifier) {
        this.netIdentifier = netIdentifier;
    }

    public String getCallFrom() {
        return callFrom;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public String getConcatCount() {
        return Integer.parseInt(this.getTerminatingCount())+Integer.parseInt(this.getOriginatingCount())+"";
    }

    public void setConcatCount(String concatCount) {
        this.concatCount = concatCount;
    }

    public String getOriginatingCount() {
        return originatingCount;
    }

    public void setOriginatingCount(String originatingCount) {
        this.originatingCount = originatingCount;
    }

    public String getTerminatingCount() {
        return terminatingCount;
    }

    public void setTerminatingCount(String terminatingCount) {
        this.terminatingCount = terminatingCount;
    }

    public String getConcatTime() {
        return (new Formatter().format("%.2f", Double.parseDouble(this.getTerminatingTime())+Double.parseDouble(this.getOriginatingTime())).toString());
    }

    public void setConcatTime(String concatTime) {
        this.concatTime = concatTime;
    }

    public String getOriginatingTime() {
        return originatingTime;
    }

    public void setOriginatingTime(String originatingTime) {
        this.originatingTime = originatingTime;
    }

    public String getTerminatingTime() {
        return terminatingTime;
    }

    public void setTerminatingTime(String terminatingTime) {
        this.terminatingTime = terminatingTime;
    }

    public String getTelRemark() {
        return telRemark;
    }

    public void setTelRemark(String telRemark) {
        this.telRemark = telRemark;
    }
}
