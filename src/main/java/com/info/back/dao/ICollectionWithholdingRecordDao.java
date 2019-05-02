package com.info.back.dao;

import java.util.HashMap;
import java.util.List;

import com.info.web.pojo.cspojo.CollectionWithholdingRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface ICollectionWithholdingRecordDao {

	public void insert(CollectionWithholdingRecord withholdingRecord);

	public List<CollectionWithholdingRecord> findOrderList(String id);

	public int updateStatusFail();

	public List<CollectionWithholdingRecord> findTowHoursList();

	public int findCurrDayWithhold(HashMap<String, String> dayMap);

}
