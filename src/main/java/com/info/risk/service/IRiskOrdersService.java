package com.info.risk.service;

import com.info.risk.pojo.RiskOrders;


public interface IRiskOrdersService {
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

    /**
     * 根据订单id查询 creditReport信息
     *
     * @param borrowId
     * @return
     */
    RiskOrders selectCreditReportByBorrowId(Integer borrowId);
}
