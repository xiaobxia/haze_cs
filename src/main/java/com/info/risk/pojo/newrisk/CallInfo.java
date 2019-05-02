package com.info.risk.pojo.newrisk;

import lombok.Data;

/**
 * Created by Phi on 2017/12/6.
 */
@Data
public class CallInfo {
    private String callNumber;

    private String inOrOut;

    private String time;

    private int duration;

    /**夜间活跃为true*/
    private Boolean nightFlag;


}
