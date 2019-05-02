package com.info.back.result;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Author :${cqry_2017}
 * Date   :2017-11-07 11:55.
 */
public class MonthUseDetail {
//    月份
    private String month;
//    与第一紧急联系人最后一次通话时间
    private String firstConcatLastCallTime;
//    与第二紧急联系人最后一次通话时间
    private String secondConcatLastCallTime;
//    与第一紧急联系人通话次数
    private String firstConcatCallCount;
//    与第二紧急联系人通话次数
    private String secondConcatCallCount;
//    与第一联系人通话次数排名
    private String firstConcatCallRanking;
//    与第二联系人通话次数排名
    private String secondConcatCallRanking;
//    手机关机静默天数
    private String telCloseDays;
//    手机连续关机最大天数
    private String severalCloseMaxDays;
//    通话号码数量
    private String callNumCount;
//    手机通话晚间活跃度
    private String callNightActiveness;
//    每月话费
    private String totalFee;
//    通话总时长
    private String totalTime;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getFirstConcatLastCallTime() {
        return firstConcatLastCallTime;
    }

    public void setFirstConcatLastCallTime(String firstConcatLastCallTime) {
        this.firstConcatLastCallTime = firstConcatLastCallTime;
    }

    public String getSecondConcatLastCallTime() {
        return secondConcatLastCallTime;
    }

    public void setSecondConcatLastCallTime(String secondConcatLastCallTime) {
        this.secondConcatLastCallTime = secondConcatLastCallTime;
    }

    public String getFirstConcatCallCount() {
        return firstConcatCallCount;
    }

    public void setFirstConcatCallCount(String firstConcatCallCount) {
        this.firstConcatCallCount = firstConcatCallCount;
    }

    public String getSecondConcatCallCount() {
        return secondConcatCallCount;
    }

    public void setSecondConcatCallCount(String secondConcatCallCount) {
        this.secondConcatCallCount = secondConcatCallCount;
    }

    public String getFirstConcatCallRanking() {
        return firstConcatCallRanking;
    }

    public void setFirstConcatCallRanking(String firstConcatCallRanking) {
        this.firstConcatCallRanking = firstConcatCallRanking;
    }

    public String getSecondConcatCallRanking() {
        return secondConcatCallRanking;
    }

    public void setSecondConcatCallRanking(String secondConcatCallRanking) {
        this.secondConcatCallRanking = secondConcatCallRanking;
    }

    public String getTelCloseDays() {
        return telCloseDays;
    }

    public void setTelCloseDays(String telCloseDays) {
        this.telCloseDays = telCloseDays;
    }

    public String getSeveralCloseMaxDays() {
        return severalCloseMaxDays;
    }

    public void setSeveralCloseMaxDays(String severalCloseMaxDays) {
        this.severalCloseMaxDays = severalCloseMaxDays;
    }

    public String getCallNumCount() {
        return callNumCount;
    }

    public void setCallNumCount(String callNumCount) {
        this.callNumCount = callNumCount;
    }

    public String getCallNightActiveness() {
        return callNightActiveness;
    }

    public void setCallNightActiveness(String callNightActiveness) {
        this.callNightActiveness = callNightActiveness;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String toStirng(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
