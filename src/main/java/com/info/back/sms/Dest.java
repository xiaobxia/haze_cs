package com.info.back.sms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"missionNum", "destId"})
@XmlRootElement(name="xml")
public class Dest {
	private String missionNum;
	private String destId;
	public String getMissionNum() {
		return missionNum;
	}
	public String getDestId() {
		return destId;
	}
	public void setMissionNum(String missionNum) {
		this.missionNum = missionNum;
	}
	public void setDestId(String destId) {
		this.destId = destId;
	}
	

}
