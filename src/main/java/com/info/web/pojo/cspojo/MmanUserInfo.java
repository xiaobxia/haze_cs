package com.info.web.pojo.cspojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
  借款人信息表
*/
@Data
public class MmanUserInfo {
	/** ID*/
	private String id;
	/** 用户名称(手机号码) */
	private String userName;
	/** 密码*/
	private String password;
	/** 交易密码*/
	private String payPassword;
	/** 真实姓名*/
	private String realname;
	/** 实名认证状态（0、未认证，1、已认证）*/
	private Integer realnameStatus;
	/** 实名认证时间*/
	private Date realnameTime;
	/** 身份证号码*/
	private String idNumber;
	/** 性别*/
	private String userSex;
	/** 年龄*/
	private Integer userAge;
	/** qq*/
	private String qq;
	/** 手机号码*/
	private String userPhone;
	/** 淘宝账号*/
	private String taobaoAccount;
	/** 邮箱*/
	private String email;
	/** 微信账号*/
	private String wechatAccount;
	/** 学历（1博士、2硕士、3本科、4大专、5中专、6高中、7初中及以下）*/
	private Integer education;
	/** 婚姻状况:1未婚,2已婚未育,3,未婚已育,4离异,5其他*/
	private Integer maritalStatus;
	/** 现居地*/
	private String presentAddress;
	/** 现居地详细信息*/
	private String presentAddressDistinct;
	/** 现居地地图的纬度*/
	private String presentLatitude;
	/** 现居地地图的经度*/
	private String presentLongitude;
	/** 居住时长*/
	private Integer presentPeriod;
	/** 公司名称*/
	private String companyName;
	/** 公司地址*/
	private String companyAddress;
	/** 公司详细地址*/
	private String companyAddressDistinct;
	/** 工作的地图经度*/
	private String companyLongitude;
	/** 公司的地图的纬度*/
	private String companyLatitude;
	/** 公司电话*/
	private String companyPhone;
	/** 工作时长*/
	private Integer companyPeriod;
	/** 第一联系人姓名*/
	private String firstContactName;
	/** 第一联系人电话*/
	private String firstContactPhone;
	/** 与第一联系人的关系(1父亲、2母亲、3儿子、4女儿、5配偶)*/
	private Integer fristContactRelation;
	/** 第二联系人姓名*/
	private String secondContactName;
	/** 第二联系人电话*/
	private String secondContactPhone;
	/** 与第二联系人的关系(1.同学2.亲戚3.同事4.朋友5.其他)*/
	private Integer secondContactRelation;
	/** 注册的时间*/
	private Date createTime;
	/** 注册的IP*/
	private String createIp;
	/** 修改的时间*/
	private Date updateTime;
	/** 用户状态(1,正常 2,黑名单)*/
	private Integer status;
	/** 邀请好友*/
	private Integer inviteUserid;
	/** 判断信息是否全部填写完成，如果为1，表示不能修改*/
	private Integer isSave;
	/** 头像地址*/
	private String headPortrait;
	/** 身份证正面*/
	private String idcardImgZ;
	/** 身份证反面*/
	private String idcardImgF;
	/** 是否是老用户：0、新用户；1；老用户*/
	private Integer customerType;
	/** 最小额度(单位分)*/
	private Integer amountMin;
	/** 最大额度(单位分)*/
	private Integer amountMax;
	/** 用户剩余额度*/
	private Integer amountAvailable;
	/** 用户注册的手机设备号*/
	private String equipmentNumber;
	/** 芝麻分*/
	private BigDecimal zmScore;
	/** 芝麻分上次更新时间*/
	private Date zmScoreTime;
	/** 芝麻行业关注度黑名单1.是；2否*/
	private Integer zmIndustyBlack;
	/** 行业关注度接口中返回的借贷逾期记录数AA001借贷逾期的记录数*/
	private Integer zmOver;
	/** 行业关注度接口中返回的逾期未支付记录数，包括AD001 逾期未支付、AE001 逾期未支付的记录总数*/
	private Integer zmNoPayOver;
	/** 行业关注度上次更新时间*/
	private Date zmIndustyTime;
	/** 芝麻信用认证状态1.未认证；2已认证*/
	private Integer zmStatus;
	/** 蚂蚁花呗额度*/
	private BigDecimal myHb;
	/** 蚂蚁花呗额度更新时间*/
	private Date myHbTime;
	/** 聚信立开始采集数据时存入token*/
	private String jxlToken;
	/** 聚信立token入库时间*/
	private Date jxlTokenTime;
	/** 聚信立认证状态,有token就认为认证通过1.未认证；2已认证*/
	private Integer jxlStatus;
	/** 聚信立成功采集后返回的数据*/
	private String jxlDetail;
	/** 聚信立成功采集详情数据的时间*/
	private Date jxlDetailTime;
	/** 聚信立贷款类号码主叫个数*/
	private Integer jxlZjDkNum;
	/** 聚信立贷款类号码被叫个数*/
	private Integer jxlBjDkNum;
	/** 聚信立月均话费*/
	private BigDecimal jxlYjHf;
	/** 聚信立通话详单和用户第二联系人最晚联系日期到目前的天数*/
	private Integer jxlLink2Days;
	/** 聚信立通话详单和用户第一联系人最晚联系日期到目前的天数*/
	private Integer jxlLink1Days;
	/** 聚信立通话详单和用户第二联系人的通话次数*/
	private Integer jxlLink2Num;
	/** 聚信立通话详单和用户第一联系人的通话次数*/
	private Integer jxlLink1Num;
	/** 聚信立第二联系人通话次数排名*/
	private Integer jxlLink2Order;
	/** 聚信立第一联系人通话次数排名*/
	private Integer jxlLink1Order;
	/** 聚信立关机天数，手机静默情况*/
	private Integer jxlGjTs;
	/** 聚信立互通电话数量*/
	private Integer jxlHtPhoneNum;
	/** 聚信立澳门通话次数*/
	private Integer jxlAmthNum;
	/** 聚信立手机开户时间距离当前的天数*/
	private Integer jxlPhoneRegDays;
	/** 聚信立分析数据更新时间*/
	private Date jxlTime;
	/** 用户通讯录的联系人数量*/
	private Integer userContactSize;
	/** 历史逾期总记录数，逾期并还款也纳为逾期记录*/
	private Integer historyOverNum;
	/** 最近一次逾期总天数，逾期并还款也算*/
	private Integer lastOverDays;
	/** 1通过；2拒绝；3无建议*/
	private Integer csjy;
	/** 用户来源0:小鱼儿,1:360,.......（扩展其它渠道）*/
	private Integer userFrom;
}