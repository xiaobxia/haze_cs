package com.info.mqlistener;

import com.info.back.exception.BusinessException;
import com.info.back.utils.SpringUtils;
import com.info.constant.Constant;
import com.info.web.synchronization.OperaRenewalDataThread;
import com.info.web.synchronization.ThreadPoolManager;
import com.info.web.synchronization.service.ILocalDataService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 逾期消费
 *
 * @author cqry_2016
 * @create 2018-09-10 10:48
 **/

@Slf4j
public class RenewalConsumer implements MessageConsumerStrategy {

    @Override
    public void process(String tag, List<String> repaymentIdList, Map<String, String> callBackParams) throws BusinessException {
        if(!Constant.TAG_RENEWAL.equals(tag)) {
            return;
        }
        String repayId = repaymentIdList.get(0);
        log.info("consume renewal id: {}", repayId);
        ILocalDataService dataService = SpringUtils.getBean("localDataServiceImpl");
        dataService.renewalOver(repayId);
        ThreadPoolManager pool = ThreadPoolManager.getInstance();
        OperaRenewalDataThread renewalDataThread = new OperaRenewalDataThread(callBackParams, repaymentIdList.get(0), dataService);
        pool.execute(renewalDataThread);
        log.info("consume renewal id: {} end", repaymentIdList.get(0));
    }
}
