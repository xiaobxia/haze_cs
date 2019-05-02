package com.info.back.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.info.web.pojo.cspojo.ContactInfo;
import com.info.web.pojo.cspojo.MmanUserInfo;

@Repository
public interface IMmanUserInfoDao {
	
	MmanUserInfo get(String id);
	
	
	MmanUserInfo  getxjxuser(Long id);
	
	
	int saveNotNull(MmanUserInfo mmanUserInfo);
	
	MmanUserInfo findJxlDetail(Map<String,Object> map);


	List<ContactInfo> getContactInfo(String phoneNum);

	int getUserCount(Map<String,Object> map);

	List<Integer> getUserIds(Map<String,Object> map);

	void updateUserFrom(@Param("dataMap") Map<Integer,String> dataMap);

}
