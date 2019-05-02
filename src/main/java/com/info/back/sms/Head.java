package com.info.back.sms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"appKey", "timeStamp", "nonceStr","sign"})
@XmlRootElement(name="xml")
public class Head {
	private String appKey;
	private String timeStamp;
	private String nonceStr;
	private String sign;
	public String getAppKey() {
		return appKey;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
