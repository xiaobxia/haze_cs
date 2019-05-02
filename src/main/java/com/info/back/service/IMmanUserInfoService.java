package com.info.back.service;

import com.info.web.pojo.cspojo.ContactInfo;
import com.info.web.pojo.cspojo.MmanUserInfo;

import java.util.List;
import java.util.Map;

public interface IMmanUserInfoService {

    /**
     * 根据主键查询用户信息
     *
     * @param mmanUserInfo
     * @return
     */
    public MmanUserInfo getUserInfoById(String id);


    public MmanUserInfo getxjxuser(Long id);


    public int saveNotNull(MmanUserInfo mmanUserInfo);

    /**
     * 根据电话号码(借款人对应联系人的)查询对应借款人信息
     *
     * @param phoneNum
     * @return
     */
    public List<ContactInfo> getContactInfo(String phoneNum);

    /**
     * 获取同盾运营商报告
     *
     * @param tdData
     * @param userInfo
     * @return
     */
    Map<String, Object> getTdYysInfo(String tdData, MmanUserInfo userInfo);

    /**
     * 根据条件查询用户数量
     *
     * @param map
     * @return
     */
    int getUserCount(Map<String, Object> map);

    /**
     * 分页查询用户id集合
     *
     * @return
     */
    List<Integer> getUserIdList(Map<String, Object> map);

    void addUserFrom(Map<Integer, String> dataMap);

}
