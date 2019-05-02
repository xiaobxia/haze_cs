package com.info.back.service;

import com.info.back.dao.ISysUserBankCardDao;
import com.info.web.pojo.cspojo.SysUserBankCard;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SysUserBankCardServiceImpl implements ISysUserBankCardService{
	
	@Resource
	private ISysUserBankCardDao sysUserBankCardDao;

	@Override
	public int saveNotNull(SysUserBankCard sysUserBankCard) {
		return sysUserBankCardDao.saveNotNull(sysUserBankCard);
	}

	@Override
	public SysUserBankCard findUserId(String userId) {
		return sysUserBankCardDao.findUserId(userId);
	}

}
