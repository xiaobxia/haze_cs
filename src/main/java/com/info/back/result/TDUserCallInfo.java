package com.info.back.result;

/**
 * Author :${cqry_2017}
 * Date   :2017-11-07 09:54.
 */
public class TDUserCallInfo {

    public TDUserCallInfo() {
    }

    public TDUserCallInfo(String callTime, String startTime, String callOtherNumber, String callTypeName) {
        this.callTime = callTime;
        this.startTime = startTime;
        this.callOtherNumber = callOtherNumber;
        this.callTypeName = callTypeName;
    }
    private String callOtherNumber;
    private String callTime;
    private String startTime;
    private String callTypeName;

    public TDUserCallInfo(String callTypeName) {
        this.callTypeName = callTypeName;
    }

    public String getCallOtherNumber() {
        return callOtherNumber;
    }

    public void setCallOtherNumber(String callOtherNumber) {
        this.callOtherNumber = callOtherNumber;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
