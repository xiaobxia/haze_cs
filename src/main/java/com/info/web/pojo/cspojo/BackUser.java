package com.info.web.pojo.cspojo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述：后台用户类 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 上午11:13:16 <br>
 */
@Data
public class BackUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String userAccount;
    private String userPassword;
    private String userName;
    private String userSex;
    private String userAddress;
    private String userTelephone;
    private String userMobile;
    private String userEmail;
    private String userQq;
    private Date createDate;
    private Date updateDate;
    private String addIp;
    private String remark;
    private Integer userStatus;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyTitle;
    /**
     * 催收组
     */
    private String groupLevel;
    private String uuid;
    private String roleId;
    private Integer viewdataStatus;
    /**
     * 需转派订单的催收人
     */
    private String notMineId;
    private Integer seatExt;

    /**
     * 与数据库无关（对应back_user_company_permissions）该用户可查看到的信息
     */
    private String[] dataComapnyIDs;

    private List<Integer> realIds;

    /**
     * 用户状态
     */
    public static final HashMap<Integer, String> ALL_STATUS = new HashMap<>();
    /**
     * 启用
     */
    public static final Integer STATUS_USE = 1;
    /**
     * 删除
     */
    public static final Integer STATUS_DELETE = 2;
    /**
     * 催收员停用
     */
    public static final Integer STATUS_STOP = 0;

    static {
        ALL_STATUS.put(STATUS_USE, "启用");
        ALL_STATUS.put(STATUS_DELETE, "删除");
    }

}
