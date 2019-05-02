package com.info.back.service;

import com.info.back.dao.IMmanLoanCollectionStatusChangeLogDao;
import com.info.back.dao.IPaginationDao;
import com.info.back.utils.IdGen;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.MmanLoanCollectionStatusChangeLog;
import com.info.web.util.PageConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MmanLoanCollectionStatusChangeLogServiceImpl implements IMmanLoanCollectionStatusChangeLogService {

    @Resource
    private IMmanLoanCollectionStatusChangeLogDao mmanLoanCollectionStatusChangeLogDao;

    @Resource
    private IPaginationDao paginationDao;


    @Override
    public PageConfig<MmanLoanCollectionStatusChangeLog> findPage(
            HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "MmanLoanCollectionStatusChangeLog");
        PageConfig<MmanLoanCollectionStatusChangeLog> pageConfig = new PageConfig<MmanLoanCollectionStatusChangeLog>();
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, null);
        return pageConfig;
    }


    @Override
    public void insert(MmanLoanCollectionStatusChangeLog mmanLoanCollectionStatusChangeLog) {
        mmanLoanCollectionStatusChangeLog.setId(IdGen.uuid());
        mmanLoanCollectionStatusChangeLog.setCreateDate(new Date());
        mmanLoanCollectionStatusChangeLogDao.insert(mmanLoanCollectionStatusChangeLog);

    }


    @Override
    public List<MmanLoanCollectionStatusChangeLog> findListLog(String orderId) {
        return mmanLoanCollectionStatusChangeLogDao.findListLog(orderId);
    }


}
