package com.info.back.service;

import com.info.back.dao.ICountCollectionAssessmentDao;
import com.info.back.dao.ICountCollectionManageDao;
import com.info.back.dao.IPaginationDao;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.CountCashBusiness;
import com.info.web.pojo.cspojo.CountCollectionAssessment;
import com.info.web.pojo.cspojo.CountCollectionManage;
import com.info.web.synchronization.dao.IPaginationXjxDao;
import com.info.web.util.DateUtil;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 管理跟踪统计
 *
 * @author Administrator
 */

@Slf4j
@Service
public class CountCollectionManageServiceImpl implements ICountCollectionManageService {

    @Resource
    private ICountCollectionManageDao countCollectionManageDao;
    @Resource
    private IPaginationDao paginationDao;
    @Resource
    private IPaginationXjxDao paginationDaoXjx;
    @Resource
    private ICountCollectionAssessmentDao countCollectionAssessmentDao;

    @Override
    public PageConfig<CountCollectionManage> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "CountCollectionManage");
        PageConfig<CountCollectionManage> pageConfig = new PageConfig<CountCollectionManage>();
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, null);
        this.handleData(pageConfig.getItems());
        return pageConfig;
    }

    @Override
    public PageConfig<CountCashBusiness> getCountCashBusinessPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "CountCashBusiness");
        PageConfig<CountCashBusiness> pageConfig = new PageConfig<CountCashBusiness>();
        pageConfig = paginationDaoXjx.findPage("findAll", "findAllCount", params, null);
        if (pageConfig != null && CollectionUtils.isNotEmpty(pageConfig.getItems())) {
            params.put("numPerPage", pageConfig.getTotalResultSize());
            PageConfig<CountCashBusiness> pageConfigAll = paginationDaoXjx.findPage("findAll", "findAllCount", params, null);
            CountCashBusiness ccb = this.handleCashData(pageConfigAll.getItems());
            if (ccb != null) {
                pageConfig.getItems().add(0, ccb);
            }
        }
        return pageConfig;
    }

    @Override
    public List<CountCollectionManage> findAll(HashMap<String, Object> params) {
        return countCollectionManageDao.findAll(params);
    }

    @Override
    public Integer findAllCount(HashMap<String, Object> params) {
        return countCollectionManageDao.findAllCount(params);
    }

    @Override
    public CountCollectionManage getOne(HashMap<String, Object> params) {
        List<CountCollectionManage> list = this.findAll(params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 数据处理
     *
     * @param list
     */
    private void handleData(List<CountCollectionManage> list) {
        if (list != null && list.size() > 0) {
            CountCollectionManage cc = new CountCollectionManage();
            for (CountCollectionManage ca : list) {
                cc.setLoanMoney(cc.getLoanMoney().add(ca.getLoanMoney()));   //本金
                cc.setRepaymentMoney(cc.getRepaymentMoney().add(ca.getRepaymentMoney()));  //已还本金
                cc.setNotYetRepaymentMoney(cc.getNotYetRepaymentMoney().add(ca.getNotYetRepaymentMoney()));   //未还本金
                cc.setPenalty(cc.getPenalty().add(ca.getPenalty()));   //滞纳金
                cc.setRepaymentPenalty(cc.getRepaymentPenalty().add(ca.getRepaymentPenalty()));   //已还滞纳金
                cc.setNotRepaymentPenalty(cc.getNotRepaymentPenalty().add(ca.getNotRepaymentPenalty()));   //未还滞纳金
                cc.setOrderTotal(cc.getOrderTotal() + ca.getOrderTotal());   // 订单量
                cc.setRepaymentOrderNum(cc.getRepaymentOrderNum() + ca.getRepaymentOrderNum());  //已还订单量
                cc.setDisposeOrderNum(cc.getDisposeOrderNum() + ca.getDisposeOrderNum());  //已操作订单量
            }
            BigDecimal rr = new BigDecimal(0);     //本金还款率
            BigDecimal rpr = new BigDecimal(0);    //滞纳金还款率
            BigDecimal ror = new BigDecimal(0);    //订单还款率
            if (cc.getLoanMoney().compareTo(new BigDecimal(0)) == 1) {
                rr = cc.getRepaymentMoney().divide(cc.getLoanMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

            }
            if (cc.getPenalty().compareTo(new BigDecimal(0)) == 1) {  //滞纳金还款率
                rpr = cc.getRepaymentPenalty().divide(cc.getPenalty(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (cc.getOrderTotal() > 0) {   //订单还款率
                double rate = cc.getRepaymentOrderNum() / cc.getOrderTotal() * 100;
                ror = new BigDecimal(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            cc.setRepaymentReta(rr);
            cc.setMigrateRate(new BigDecimal(-1.00).setScale(2, BigDecimal.ROUND_HALF_UP));
            cc.setPenaltyRepaymentReta(rpr);
            cc.setRepaymentOrderRate(ror);
            list.add(0, cc);
        }
    }

    /**
     * 小鱼儿业务量总计
     *
     * @param list
     */
    private CountCashBusiness handleCashData(List<CountCashBusiness> list) {
        CountCashBusiness ccb = new CountCashBusiness();
        if (CollectionUtils.isNotEmpty(list)) {
            for (CountCashBusiness countCashBusiness : list) {
                ccb.setExpireAmount((ccb.getExpireAmount() == null ? 0 : ccb.getExpireAmount()) + countCashBusiness.getExpireAmount());
                ccb.setMoneyAmountCount((ccb.getMoneyAmountCount() == null ? 0 : ccb.getMoneyAmountCount()) + countCashBusiness.getMoneyAmountCount());
                ccb.setSevendayMoenyCount((ccb.getSevendayMoenyCount() == null ? 0 : ccb.getSevendayMoenyCount()) + countCashBusiness.getSevendayMoenyCount());
                ccb.setFourteendayMoneyCount((ccb.getFourteendayMoneyCount() == null ? 0 : ccb.getFourteendayMoneyCount()) + countCashBusiness.getFourteendayMoneyCount());
                ccb.setOverdueAmount((ccb.getOverdueAmount() == null ? 0 : ccb.getOverdueAmount()) + countCashBusiness.getOverdueAmount());
                ccb.setOverdueRateSevenAmount((ccb.getOverdueRateSevenAmount() == null ? 0 : ccb.getOverdueRateSevenAmount()) + countCashBusiness.getOverdueRateSevenAmount());
                ccb.setOverdueRateFourteenAmount((ccb.getOverdueRateFourteenAmount() == null ? 0 : ccb.getOverdueRateFourteenAmount()) + countCashBusiness.getOverdueRateFourteenAmount());
            }
        }
        return ccb;
    }

    @Override
    public void countCallManage(HashMap<String, Object> params) {
//		countCollectionManageDao.callManage(params);
        params.put("begDate", DateUtil.getDayFirst());
        params.put("endDate", new Date());
        countCollectionAssessmentDao.deleteManageList(params);
        Date date = new Date();
        int count = 0;
        try {
            count = DateUtil.daysBetween(DateUtil.getDayFirst(), new Date());
        } catch (ParseException e) {
            log.error("计算时间天数异常", e);
        }
        log.info("天数：" + count);
        for (int i = count; i >= 0; i--) {
            params.put("currDate", DateUtil.getBeforeOrAfter(date, -i));
            List<CountCollectionAssessment> manageList = countCollectionAssessmentDao.queryManageList(params);
            if (CollectionUtils.isNotEmpty(manageList)) {
                countCollectionAssessmentDao.insertManageList(manageList);
            }
        }
    }
}
