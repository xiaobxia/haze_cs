package com.info.mqlistener;

import com.info.back.exception.BusinessException;
import com.info.back.utils.SpringUtils;
import com.info.constant.Constant;
import com.info.web.synchronization.OperaOverdueDataThread;
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
public class OverdueConsumer implements MessageConsumerStrategy {

    @Override
    public void process(String tag, List<String> repaymentIdList, Map<String, String> callBackParams) throws BusinessException {
        if (!Constant.TAG_OVERDUE.equals(tag)) {
            return;
        }
        String logStr;
        ILocalDataService dataService = SpringUtils.getBean("localDataServiceImpl");
        if (callBackParams.containsKey("mqKey")) {
            String repayId = repaymentIdList.get(0);
            logStr = "consume part repay. id: " + repayId;
            dataService.getRepayedMoneyEqualTrueRepayed(repayId);
        } else {
            logStr = "consume overdue message, repaymentIdList size：" + repaymentIdList.size();
        }
        log.info(logStr);
        ThreadPoolManager pool = ThreadPoolManager.getInstance();
        for (String repaymentId : repaymentIdList) {
            Runnable operaDataThread = new OperaOverdueDataThread(callBackParams, repaymentId, dataService);
            pool.execute(operaDataThread);
        }
        log.info(logStr + " end");
    }
}
