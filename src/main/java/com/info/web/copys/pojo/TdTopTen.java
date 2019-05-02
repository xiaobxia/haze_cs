package com.info.web.copys.pojo;

import lombok.Data;

/**
 * Created by Phi on 2017/12/27.
 */
@Data
public class TdTopTen {
    private Integer rank;

    private String callInNumber;

    private String callInFrequency;

    private String callInPerson;

    private Boolean callInSameFlag;

    private String callOutNumber;

    private String callOutFrequency;

    private String callOutPerson;

    private Boolean callOutSameFlag;

}
