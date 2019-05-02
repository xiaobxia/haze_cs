package com.info.web.synchronization;

import com.info.web.synchronization.service.ILocalDataService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 处理续期数据
 *
 * @author gaoyuhai
 */
@Slf4j
public class OperaRenewalDataThread implements Runnable {
    private String repaymentId;
    private Map<String, String> callBackParams;
    private ILocalDataService localDataService;

    public OperaRenewalDataThread(Map<String, String> callBackParams, String repaymentId, ILocalDataService localDataService) {
        this.repaymentId = repaymentId;
        this.callBackParams = callBackParams;
        this.localDataService = localDataService;
    }

    @Override
    public void run() {
        localDataService.dealRenewal(callBackParams, repaymentId);
    }


}
