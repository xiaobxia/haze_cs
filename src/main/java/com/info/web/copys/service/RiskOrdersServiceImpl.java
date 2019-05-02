package com.info.web.copys.service;

import com.info.back.utils.RequestUtils;
import com.info.back.dao.IRiskOrdersDao;
import com.info.risk.pojo.RiskOrders;
import com.info.risk.service.IRiskOrdersService;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RiskOrdersServiceImpl implements IRiskOrdersService {
    @Resource
    private IRiskOrdersDao ordersDao;


    @Override
    public int insert(RiskOrders orders) {
        if (StringUtils.isBlank(orders.getAddIp())) {
            orders.setAddIp(RequestUtils.getIpAddr());
        }
        return ordersDao.insert(orders);
    }


    @Override
    public int update(RiskOrders orders) {
        return ordersDao.update(orders);
    }


    @Override
    public RiskOrders findById(Integer id) {
        return ordersDao.findById(id);
    }

    @Override
    public RiskOrders selectCreditReportByBorrowId(Integer borrowId) {
        return ordersDao.selectCreditReportByBorrowId(borrowId);
    }
}
