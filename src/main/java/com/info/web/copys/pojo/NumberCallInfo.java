package com.info.web.copys.pojo;

import lombok.Data;

/**
 * Created by Phi on 2017/12/6.
 */
@Data
public class NumberCallInfo {
    private String callNumber;

    private int duration;

    private int durationIn;

    private int durationOut;

    private int frequency;

    private int frequencyIn;

    private int frequencyOut;

    private int nightFrequency;

    private String netMark;

    private String location;

}
