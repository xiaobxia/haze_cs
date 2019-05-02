package com.info.risk.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * 调用第三方接口记录
 * Created by tl on 2018/3/13.
 */
@Document(collection = "CallRecord")
public class CallRecord implements Serializable {
    @Id
    private String id;
    @CreatedDate
    private Date createTime = new Date();
    private Date startTime ;
    private Date endTime ;
    private Integer status;
    private String supplier;

    //接口名称
    public static final ArrayList<String> ALL_INTERFACE = new ArrayList<>();
    //白骑士
    public static final String BQS = "白骑士";
    //同盾
    public static final String TD = "同盾";
    //中智诚反欺诈
    public static final String ZZCFQZ = "中智诚反欺诈";
    //中智诚黑名单
    public static final String ZZCHMD = "中智诚黑名单";
    //致诚阿福
    public static final String ZCAF = "致诚阿福";
    //公信宝
    public static final String GXB = "公信宝";
    //状态
    public static final HashMap<Integer, String> ALL_STATUS = new HashMap<Integer, String>();
    public static final Integer SUCCESS = 1;
    public static final Integer DEFEAT = 0;

    static {
        ALL_INTERFACE.add(BQS);
        ALL_INTERFACE.add(TD);
        ALL_INTERFACE.add(ZZCFQZ);
        ALL_INTERFACE.add(ZZCHMD);
        ALL_INTERFACE.add(ZCAF);
        ALL_INTERFACE.add(GXB);
    }

    static {
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
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

}