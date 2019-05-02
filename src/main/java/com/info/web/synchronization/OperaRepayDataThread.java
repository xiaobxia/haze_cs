package com.info.web.synchronization;

import com.info.web.synchronization.service.ILocalDataService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * 还款
 *
 * @author cqry_2016
 * @create 2018-08-28 15:22
 **/
@Slf4j
public class OperaRepayDataThread implements Runnable {


    private String repayId;
    private Map<String, String> callBackParams;
    private ILocalDataService localDataService;

    public OperaRepayDataThread(Map<String, String> callBackParams, String repayId, ILocalDataService localDataService) {
        this.repayId = repayId;
        this.callBackParams = callBackParams;
        this.localDataService = localDataService;
    }

    @Override
    public void run() {
        localDataService.dealRepay(callBackParams, repayId);
    }

}
