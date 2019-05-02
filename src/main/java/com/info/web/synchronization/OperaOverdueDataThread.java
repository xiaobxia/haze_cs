package com.info.web.synchronization;

import com.info.web.synchronization.service.ILocalDataService;

import java.util.Map;


/**
 * 处理逾期数据
 *
 * @author gaoyuhai
 */
public class OperaOverdueDataThread implements Runnable {

    private Map<String, String> callBackParams;
    private String repaymentId;
    private ILocalDataService localDataService;

    public OperaOverdueDataThread(Map<String, String> callBackParams, String repaymentId, ILocalDataService localDataService) {
        this.repaymentId = repaymentId;
        this.callBackParams = callBackParams;
        this.localDataService = localDataService;
    }

    @Override
    public void run() {
        localDataService.dealOverdue(callBackParams, repaymentId);
    }
}
