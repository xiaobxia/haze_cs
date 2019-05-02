package com.info.back.service;

import java.util.List;

import com.info.web.pojo.cspojo.CollectionWithholdingRecord;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.info.back.dao.ICollectionWithholdingRecordDao;

@Service
public class CollectionWithholdingRecordServiceImpl implements ICollectionWithholdingRecordService {
    @Resource
    private ICollectionWithholdingRecordDao collectionWithholdingRecordDao;

    @Override
    public boolean updateStatusFail() {
        return collectionWithholdingRecordDao.updateStatusFail() > 0;
    }

    @Override
    public List<CollectionWithholdingRecord> findTowHoursList() {
        return collectionWithholdingRecordDao.findTowHoursList();
    }

}
