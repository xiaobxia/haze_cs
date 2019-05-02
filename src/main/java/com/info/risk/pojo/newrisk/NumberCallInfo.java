package com.info.risk.pojo.newrisk;

import java.util.Map;

/**
 * Created by Phi on 2017/12/6.
 */
public class NumberCallInfo {
    private String callNumber;

    private int duration;//通话时长

    private int durationIn;//呼入时长

    private int durationOut;//呼出时长

    private int frequency;//通话次数

    private int frequencyIn;//呼入次数

    private int frequencyOut;//呼出次数

    private int nightFrequency;//夜间通话时长

    private String netMark;//运营商

    private String location;//归属地

    private String callName;

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDurationIn() {
        return durationIn;
    }

    public void setDurationIn(int durationIn) {
        this.durationIn = durationIn;
    }

    public int getDurationOut() {
        return durationOut;
    }

    public void setDurationOut(int durationOut) {
        this.durationOut = durationOut;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequencyIn() {
        return frequencyIn;
    }

    public void setFrequencyIn(int frequencyIn) {
        this.frequencyIn = frequencyIn;
    }

    public int getFrequencyOut() {
        return frequencyOut;
    }

    public void setFrequencyOut(int frequencyOut) {
        this.frequencyOut = frequencyOut;
    }

    public String getNetMark() {
        return netMark;
    }

    public void setNetMark(String netMark) {
        this.netMark = netMark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNightFrequency() {
        return nightFrequency;
    }

    public void setNightFrequency(int nightFrequency) {
        this.nightFrequency = nightFrequency;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }
}
