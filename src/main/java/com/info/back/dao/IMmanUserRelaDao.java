package com.info.back.dao;

import java.util.HashMap;
import java.util.List;

import com.info.web.pojo.cspojo.MmanUserRela;
import org.springframework.stereotype.Repository;

@Repository
public interface IMmanUserRelaDao {
	
	public int saveNotNull(MmanUserRela mmanUserRela);
	/**
	 * 鏌ヨ鎸囧畾閫炬湡澶╂暟鐨勫垪琛╨ist
	 * @param params
	 * @return
	 */
	public List<MmanUserRela> getList(HashMap<String, Object> params);

	/**
	 * 鏍规嵁鐢ㄦ埛id鏌ヨ鐢ㄦ埛瀵瑰簲鐨勮仈绯讳汉
	 * @param userId
	 * @return
	 */
	public List<MmanUserRela> getContactPhones(String userId);

    MmanUserRela getUserRealByUserId(String userRelaId);
}

