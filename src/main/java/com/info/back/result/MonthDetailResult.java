package com.info.back.result;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Author :${cqry_2017}
 * Date   :2017-11-13 09:46.
 */
public class MonthDetailResult {
    private String type;
    private String month1;
    private String month2;
    private String month3;
    private String month4;
    private String month5;
    private String month6;
    private String month7;
    private String month8;
    private String month9;
    private String month10;
    private String month11;
    private String month12;
    private String allCount;

    public String getAllCount() {
        return allCount;
    }

    public void setAllCount(String allCount) {
        this.allCount = allCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth1() {
        return month1;
    }

    public void setMonth1(String month1) {
        this.month1 = month1;
    }

    public String getMonth2() {
        return month2;
    }

    public void setMonth2(String month2) {
        this.month2 = month2;
    }

    public String getMonth3() {
        return month3;
    }

    public void setMonth3(String month3) {
        this.month3 = month3;
    }

    public String getMonth4() {
        return month4;
    }

    public void setMonth4(String month4) {
        this.month4 = month4;
    }

    public String getMonth5() {
        return month5;
    }

    public void setMonth5(String month5) {
        this.month5 = month5;
    }

    public String getMonth6() {
        return month6;
    }

    public void setMonth6(String month6) {
        this.month6 = month6;
    }

    public String getMonth7() {
        return month7;
    }

    public void setMonth7(String month7) {
        this.month7 = month7;
    }

    public String getMonth8() {
        return month8;
    }

    public void setMonth8(String month8) {
        this.month8 = month8;
    }

    public String getMonth9() {
        return month9;
    }

    public void setMonth9(String month9) {
        this.month9 = month9;
    }

    public String getMonth10() {
        return month10;
    }

    public void setMonth10(String month10) {
        this.month10 = month10;
    }

    public String getMonth11() {
        return month11;
    }

    public void setMonth11(String month11) {
        this.month11 = month11;
    }

    public String getMonth12() {
        return month12;
    }

    public void setMonth12(String month12) {
        this.month12 = month12;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
