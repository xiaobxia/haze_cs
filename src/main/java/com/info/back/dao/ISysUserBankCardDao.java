package com.info.back.dao;

import com.info.web.pojo.cspojo.SysUserBankCard;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysUserBankCardDao {
	
	public int saveNotNull(SysUserBankCard sysUserBankCard);

	public SysUserBankCard findUserId(String userId);

}
