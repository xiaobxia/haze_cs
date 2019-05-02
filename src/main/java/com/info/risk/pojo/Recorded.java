package com.info.risk.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * 被调用接口记录
 * Created by tl on 2018/3/13.
 */
@Document(collection = "Recorded")
public class Recorded implements Serializable {
    @Id
    private String id;
    @CreatedDate
    private Date createTime = new Date();
    private Date startTime ;
    private Date endTime ;
    private String callerId;
    private Integer status;
    private Integer bQS;//1为成功,0为失败
    private Integer tD;
    private Integer zZCFQZ;
    private Integer zZCHMD;
    private Integer zCAF;
    private Integer gXB;

    //状态
    public static final HashMap<Integer, String> ALL_STATUS = new HashMap<Integer, String>();
    public static final Integer UNCALL = 3;
    public static final Integer PART_SUCCESS = 2;
    public static final Integer SUCCESS = 1;
    public static final Integer DEFEAT = 0;

    static {
        ALL_STATUS.put(UNCALL, "未调用");
        ALL_STATUS.put(PART_SUCCESS, "部分成功");
        ALL_STATUS.put(SUCCESS, "成功");
        ALL_STATUS.put(DEFEAT, "失败");
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getbQS() {
        return bQS;
    }

    public void setbQS(Integer bQS) {
        this.bQS = bQS;
    }

    public Integer gettD() {
        return tD;
    }

    public void settD(Integer tD) {
        this.tD = tD;
    }

    public Integer getzZCFQZ() {
        return zZCFQZ;
    }

    public void setzZCFQZ(Integer zZCFQZ) {
        this.zZCFQZ = zZCFQZ;
    }

    public Integer getzZCHMD() {
        return zZCHMD;
    }

    public void setzZCHMD(Integer zZCHMD) {
        this.zZCHMD = zZCHMD;
    }

    public Integer getzCAF() {
        return zCAF;
    }

    public void setzCAF(Integer zCAF) {
        this.zCAF = zCAF;
    }

    public Integer getgXB() {
        return gXB;
    }

    public void setgXB(Integer gXB) {
        this.gXB = gXB;
    }

    @Override
    public String toString() {
        return "Recorded{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", callerId='" + callerId + '\'' +
                ", status=" + status +
                ", bQS=" + bQS +
                ", tD=" + tD +
                ", zZCFQZ=" + zZCFQZ +
                ", zZCHMD=" + zZCHMD +
                ", zCAF=" + zCAF +
                ", gXB=" + gXB +
                '}';
    }
}
