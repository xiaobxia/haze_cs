package com.info.back.sms;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"errorCode", "errorMsg","outString"})
@XmlRootElement(name="xml")
public class Result implements Serializable {
	
	private String errorCode;
	private String errorMsg;

	/**
	 * 需要去除的标签
	 */
	private String outString[] = new String[]{"<head>","</head>"};
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
	public String[] getOutString() {
		return outString;
	}
	@Override
	public String toString() {
		return "Result [errorCode=" + errorCode + ", errorMsg=" + errorMsg
				+ ", outString=" + Arrays.toString(outString) + "]";
	}
	
}
