package com.info.risk.pojo.newrisk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phi on 2017/12/14.
 */
public class ShuJuMoHeVo {
    private String name;//姓名
    private String age;//年龄
    private String phoneNumber;//手机号码
    private String local;//联系地址
    private String callNumber;//联系电话
    private String netAge;//入网时间
    private String accountType;//账户状态
    private String accountStart;//账户星级
    private String accountBalance;//账户余额
    private String sameRate;//撞库率
    private String channel;//运营商

    private List<String> monthList;//月份列表

    private Map<String, String> lastCallTimeFirst;  //与第一紧急联系人最后一次通话时间
    private Map<String, String> lastCallTimeSecond;//与第二紧急联系人最后一次通话时间
    private Map<String, String> frequencyFirst; //与第一紧急联系人通话次数
    private Map<String, String> frequencySecond;//与第二紧急联系人通话次数
    private Map<String, String> frequencyRankFirst; //与第一联系人通话次数排名
    private Map<String, String> frequencyRankSecond;//与第二联系人通话次数排名

    private Map<String, Integer> silenceDays;//手机静默天数
    private Map<String, Integer> maxSilenceDays;//手机连续静默最大天数
    private Map<String, Integer> monthNumberSize;//通话号码数量
    private Map<String, String> monthNightFrequency;//晚间通话活跃度

    private Map<String, String> billInfoMap;//总话费

    private Map<String, Integer> monthDayDuration;//通话总时长

    private List<TdTopTen> tdTopTenList = new ArrayList<TdTopTen>();//运营商数据top10

    private String tdTopTenSame;//运营商数据top10,碰撞率

    private Map<String, NumberCallInfo> numberCallInfoMap;//通话详单

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getNetAge() {
        return netAge;
    }

    public void setNetAge(String netAge) {
        this.netAge = netAge;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountStart() {
        return accountStart;
    }

    public void setAccountStart(String accountStart) {
        this.accountStart = accountStart;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getSameRate() {
        return sameRate;
    }

    public void setSameRate(String sameRate) {
        this.sameRate = sameRate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<String> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<String> monthList) {
        this.monthList = monthList;
    }

    public Map<String, String> getLastCallTimeFirst() {
        return lastCallTimeFirst;
    }

    public void setLastCallTimeFirst(Map<String, String> lastCallTimeFirst) {
        this.lastCallTimeFirst = lastCallTimeFirst;
    }

    public Map<String, String> getLastCallTimeSecond() {
        return lastCallTimeSecond;
    }

    public void setLastCallTimeSecond(Map<String, String> lastCallTimeSecond) {
        this.lastCallTimeSecond = lastCallTimeSecond;
    }

    public Map<String, String> getFrequencyFirst() {
        return frequencyFirst;
    }

    public void setFrequencyFirst(Map<String, String> frequencyFirst) {
        this.frequencyFirst = frequencyFirst;
    }

    public Map<String, String> getFrequencySecond() {
        return frequencySecond;
    }

    public void setFrequencySecond(Map<String, String> frequencySecond) {
        this.frequencySecond = frequencySecond;
    }

    public Map<String, String> getFrequencyRankFirst() {
        return frequencyRankFirst;
    }

    public void setFrequencyRankFirst(Map<String, String> frequencyRankFirst) {
        this.frequencyRankFirst = frequencyRankFirst;
    }

    public Map<String, String> getFrequencyRankSecond() {
        return frequencyRankSecond;
    }

    public void setFrequencyRankSecond(Map<String, String> frequencyRankSecond) {
        this.frequencyRankSecond = frequencyRankSecond;
    }

    public Map<String, Integer> getSilenceDays() {
        return silenceDays;
    }

    public void setSilenceDays(Map<String, Integer> silenceDays) {
        this.silenceDays = silenceDays;
    }

    public Map<String, Integer> getMaxSilenceDays() {
        return maxSilenceDays;
    }

    public void setMaxSilenceDays(Map<String, Integer> maxSilenceDays) {
        this.maxSilenceDays = maxSilenceDays;
    }

    public Map<String, Integer> getMonthNumberSize() {
        return monthNumberSize;
    }

    public void setMonthNumberSize(Map<String, Integer> monthNumberSize) {
        this.monthNumberSize = monthNumberSize;
    }

    public Map<String, String> getMonthNightFrequency() {
        return monthNightFrequency;
    }

    public void setMonthNightFrequency(Map<String, String> monthNightFrequency) {
        this.monthNightFrequency = monthNightFrequency;
    }

    public Map<String, String> getBillInfoMap() {
        return billInfoMap;
    }

    public void setBillInfoMap(Map<String, String> billInfoMap) {
        this.billInfoMap = billInfoMap;
    }

    public Map<String, Integer> getMonthDayDuration() {
        return monthDayDuration;
    }

    public void setMonthDayDuration(Map<String, Integer> monthDayDuration) {
        this.monthDayDuration = monthDayDuration;
    }

    public List<TdTopTen> getTdTopTenList() {
        return tdTopTenList;
    }

    public void setTdTopTenList(List<TdTopTen> tdTopTenList) {
        this.tdTopTenList = tdTopTenList;
    }

    public String getTdTopTenSame() {
        return tdTopTenSame;
    }

    public void setTdTopTenSame(String tdTopTenSame) {
        this.tdTopTenSame = tdTopTenSame;
    }

    public Map<String, NumberCallInfo> getNumberCallInfoMap() {
        return numberCallInfoMap;
    }

    public void setNumberCallInfoMap(Map<String, NumberCallInfo> numberCallInfoMap) {
        this.numberCallInfoMap = numberCallInfoMap;
    }
}
