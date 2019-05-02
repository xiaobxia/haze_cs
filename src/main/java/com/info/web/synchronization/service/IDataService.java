package com.info.web.synchronization.service;

import com.info.back.vo.LoanRecord;
import com.info.back.vo.RenewOrPayRecord;

import java.util.HashMap;
import java.util.List;

public interface IDataService {

    List<HashMap<Integer, Object>> getUserFromData(List<Integer> ids);

    /**
     * 获取用户借款记录
     *
     * @param userId
     * @return
     */
    List<LoanRecord> getUserLoanRecord(Integer userId);

    /**
     * 获取订单续期和还款记录
     *
     * @param assetOrderId
     * @return
     */
    List<RenewOrPayRecord> getUserRenewOrPayRecord(Integer assetOrderId);

    /**
     * 获取用户续期次数
     *
     * @param ids
     * @return
     */
    List<HashMap<Integer, Long>> getOrderRenewalCount(List<Integer> ids);

}
