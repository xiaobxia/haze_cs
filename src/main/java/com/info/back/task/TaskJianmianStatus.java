package com.info.back.task;

import com.info.back.dao.IAuditCenterDao;
import com.info.back.dao.IMmanLoanCollectionOrderDao;
import com.info.web.pojo.cspojo.AuditCenter;
import com.info.web.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TaskJianmianStatus {
    @Resource
    private IMmanLoanCollectionOrderDao iMmanLoanCollectionOrderDao;
    @Resource
    private IAuditCenterDao auditCenterDao;

    public void updateStatus() {
        try {
            log.error(" 减免定时............." + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
            List<AuditCenter> auditCenterlist = auditCenterDao.findgetList();
            log.info(" select list===========" + auditCenterlist.size());
            for (int i = 0; i < auditCenterlist.size(); i++) {
                AuditCenter auditCenter = auditCenterlist.get(i);
                log.info("order  auditCenter===========" + auditCenter);
                Map<String, String> map1 = new HashMap<>();
                map1.put("loanId", auditCenter.getLoanId());
                map1.put("status", "4");
                auditCenterDao.updateSysStatus(map1);
                HashMap<String, String> ordermap = new HashMap<>();
                ordermap.put("loanId", auditCenter.getLoanId());
                ordermap.put("status", auditCenter.getOrderStatus());
                ordermap.put("reductionMoney", "0");
                iMmanLoanCollectionOrderDao.sveUpdateNotNull(ordermap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("TaskWithholdStatus updateStatus error:", e);
        }
/**
 * 减免特殊处理
 */
        try {
            log.error(" 减免特殊订单处理............." + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
            List<AuditCenter> list = auditCenterDao.selectGetList();
            log.info(" AuditCenter  list===========" + list.size());
            if (!CollectionUtils.isEmpty(list)) {
                for (int j = 0; j < list.size(); j++) {
                    AuditCenter auditCenter = list.get(j);
                    log.info("order  auditCenter===========" + auditCenter);
                    if (auditCenter != null) {
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("loanId", auditCenter.getLoanId());
                        map1.put("status", "4");
                        auditCenterDao.updateSysStatus(map1);
                    } else {
                        log.info("order  auditCenter===========" + auditCenter);
                    }
                }
            }
        } catch (Exception e) {
            log.info("order  auditCenter===========" + e);
        }
    }
}

