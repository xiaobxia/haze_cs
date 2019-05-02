package com.info.risk.service;



import com.info.risk.pojo.CreditReport;
import com.info.risk.pojo.CreditUser;
import com.info.risk.pojo.Supplier;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangmiao on 2017-09-29.
 */
public interface IRiskService {

    CreditReport getUserCreditReport(CreditUser creditUser, Integer expiryDate, Boolean shortCircuitFlag);

    CreditReport getUserCreditReport(CreditUser creditUser, Integer expiryDate, List<Supplier> creditReportSupplier);

    /**
     * 查询数聚魔盒数据是否已经获取到了
     *
     * @return
     */
    boolean shujumoheCompleteFlag(String identityCard, String userPhone);


}
