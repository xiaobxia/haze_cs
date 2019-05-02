package com.info.web.copys.service;

import com.info.risk.pojo.CreditReport;
import com.info.risk.pojo.RiskOrders;
import com.info.risk.pojo.Supplier;
import com.info.risk.pojo.newrisk.ShowRisk;
import com.info.risk.pojo.newrisk.ShuJuMoHeVo;


import java.util.List;
import java.util.Map;

public interface IAutoRiskService {
    //public void autoRisk();

    /**
     *
     * @param creditReport
     * @param supplier
     * @return 返回页面报文的数据
     */
    Map<String,Object> getRiskData(CreditReport creditReport, String supplier);


    ShuJuMoHeVo getShuJuMoHe(RiskOrders riskOrders);

    ShowRisk getShowRiskFromSupplier(List<Supplier> supplierList, CreditReport creditReport);

    Map<String, String> getConcatsRemarkMap(String userId);
}
