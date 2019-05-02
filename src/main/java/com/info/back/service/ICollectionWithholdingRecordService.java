package com.info.back.service;

import java.util.List;

import com.info.web.pojo.cspojo.CollectionWithholdingRecord;

public interface ICollectionWithholdingRecordService {
	/**
	 * 修改催收代扣状态
	 * @param withholdRecord
	 * @return
	 */
	public boolean updateStatusFail();

	public List<CollectionWithholdingRecord> findTowHoursList();
}
