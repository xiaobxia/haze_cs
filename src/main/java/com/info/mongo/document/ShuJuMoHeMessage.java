package com.info.mongo.document;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 数聚魔盒存储对象
 * Created by Phi on 2018/1/24.
 */
@Document(collection = "ShuJuMoHeMessage")
public class ShuJuMoHeMessage implements Serializable {
    //身份证\电话 定位信息
    @Id
    private String id;
    @Indexed
    private String identityCard;//身份证
    @Indexed
    private String userPhone;//电话

    private String userName;//真实姓名

    private String taskId;//同盾运营商查询任务id

    private String message;//数据魔盒原始报文

    private String business;//调用者业务信息


    @CreatedDate
    private Date createTime = new Date();

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
