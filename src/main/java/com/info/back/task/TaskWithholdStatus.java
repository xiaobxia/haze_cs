package com.info.back.task;

import com.info.back.service.ICollectionWithholdingRecordService;
import com.info.web.pojo.cspojo.CollectionWithholdingRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class TaskWithholdStatus {
    @Resource
    private ICollectionWithholdingRecordService collectionWithholdingRecordService;

    public void updateStatus() {
        try {
            //查询两小时前申请中的数据   what???????
            List<CollectionWithholdingRecord> recordList = collectionWithholdingRecordService.findTowHoursList();
            if (recordList != null && recordList.size() > 0) {
                collectionWithholdingRecordService.updateStatusFail();
            } else {
                log.error("暂时不没有需要处理的代扣记录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("TaskWithholdStatus updateStatus error:", e);
        }
    }
}
