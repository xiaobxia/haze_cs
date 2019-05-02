package com.info.web.copys.pojo;

import com.info.risk.pojo.Advice;
import com.info.risk.pojo.newrisk.NumberCallInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phi on 2017/12/14.
 */
@Data
public class ShuJuMoHeVo {
    private String name;
    /**姓名*/
    private String phoneNumber;
    /**手机号码*/
    private String local;
    /**联系地址*/
    private String callNumber;
    /**联系电话*/
    private String eMail;
    /**邮箱地址*/
    private String netAge;
    /**入网时间*/
    private String accountType;
    /**账户状态*/
    private String accountStart;
    /**账户星级*/
    private String accountBalance;
    /**账户余额*/
    private String sameRate;
    /**撞库率*/
    private String channel;
    /**运营商*/
    private List<String> monthList;
    /**月份列表*/
    private Map<String, String> lastCallTimeFirst;
    /**与第一紧急联系人最后一次通话时间*/
    private Map<String, String> lastCallTimeSecond;
    /**与第二紧急联系人最后一次通话时间*/
    private Map<String, String> frequencyFirst;
    /**与第一紧急联系人通话次数*/
    private Map<String, String> frequencySecond;
    /**与第二紧急联系人通话次数*/
    private Map<String, String> frequencyRankFirst;
    /**与第一联系人通话次数排名*/
    private Map<String, String> frequencyRankSecond;
    /**与第二联系人通话次数排名*/
    private Map<String, Integer> silenceDays;
    /**手机静默天数*/
    private Map<String, Integer> maxSilenceDays;
    /**手机连续静默最大天数*/
    private Map<String, Integer> monthNumberSize;
    /**通话号码数量*/
    private Map<String, String> monthNightFrequency;
    /**晚间通话活跃度*/
    private Map<String, String> billInfoMap;
    /**总话费*/
    private Map<String, Integer> monthDayDuration;
    /**通话总时长*/
    private List<TdTopTen> tdTopTenList = new ArrayList<>();
    /**运营商数据top10*/
    private String tdTopTenSame;
    /**运营商数据top10,碰撞率*/
    private List<String> autoList = new ArrayList<>();
    /**命中规则的集合*/
    private List<Advice> adviceList = new ArrayList<>();
    /**建议集合*/
    private Map<String, NumberCallInfo> numberCallInfoMap;
    /**通话详单*/

}
