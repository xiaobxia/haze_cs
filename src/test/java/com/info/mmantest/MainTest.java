package com.info.mmantest;

import com.info.back.dao.ICollectionSucCountDao;
import com.info.back.dao.ILocalDataDao;
import com.info.back.dao.IMmanLoanCollectionOrderDao;
import com.info.back.service.ICollectionStatisticsService;
import com.info.back.vo.CollectionSucCount;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrder;
import com.info.web.synchronization.dao.IDataDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:applicationContext.xml"})
public class MainTest {

    @Resource
    private ILocalDataDao localDataDao;
    @Resource
    private IMmanLoanCollectionOrderDao mmanLoanCollectionOrderDao;
    @Resource
    private IDataDao dataDao;
    @Resource
    private ICollectionStatisticsService collectionStatisticsService;
    @Resource
    private ICollectionSucCountDao collectionSucCountDao;

    //    @Test
    public void test1() {
        HashMap<String, Object> map = new HashMap<>();
        String loanId = "290107";
        map.put("repayRenewalMark", 1);
        map.put("ORDER_ID", loanId);
        map.put("USER_ID", 24371);
        map.put("REAL_MONEY", 0);
        this.localDataDao.updateOrderStatus(map);
        MmanLoanCollectionOrder one = mmanLoanCollectionOrderDao.getOrderById("00006a4aa5054ea7b6d924fd4133479a");
        assert one.getRepayRenewalMark() == 1;
    }

//    @Test
    public void test2() {
        HashMap<String, Object> lateFeeMap = dataDao.selectRenewalRecord(95);
        assert  Integer.valueOf(lateFeeMap.get("lateFee").toString()) > 0;
    }

//    @Test
    public void testCollectSucCount() {
        String startTime = "2018-08-010";
        String endTime = "2018-08-11";
        collectionStatisticsService.countCollectionResult(startTime, endTime);
        CollectionSucCount one = collectionSucCountDao.selectByLevelAndName("3", "章慧琴");
        Assert.assertNotNull(one);
        Assert.assertTrue(one.getIntoNum() == 44 && one.getSucNum() == 15 &&
                one.getRenewNum() == 10 && one.getRepayNum() == 5 && one.getSucRate().doubleValue() == 0.34);
    }

}
