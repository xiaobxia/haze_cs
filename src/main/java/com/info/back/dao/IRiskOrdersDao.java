package com.info.back.dao;

import com.info.risk.pojo.RiskOrders;
import org.springframework.stereotype.Repository;


@Repository
public interface IRiskOrdersDao {
    /**
     * 发出请求
     *
     * @param zbNews
     * @return
     */
    public int insert(RiskOrders orders);

    /**
     * 更新
     *
     * @param Integer
     * @return
     */
    public int update(RiskOrders orders);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    public RiskOrders findById(Integer id);


    public int insertCreditReport(RiskOrders riskOrders);

    public RiskOrders selectCreditReportByBorrowId(Integer assetBorrowId);

}
