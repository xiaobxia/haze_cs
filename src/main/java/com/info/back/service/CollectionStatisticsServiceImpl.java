package com.info.back.service;

import com.info.back.dao.ICollectionStatisticsDao;
import com.info.back.dao.ICollectionSucCountDao;
import com.info.back.dao.ICollectionSucRecordDao;
import com.info.back.dao.IMmanLoanCollectionOrderDao;
import com.info.back.echarts.Echarts;
import com.info.back.echarts.entity.EchartsConstant;
import com.info.back.echarts.entity.EchartsEntity;
import com.info.back.echarts.entity.EchartsSeriesEntity;
import com.info.back.utils.BackConstant;
import com.info.back.vo.CollectionSucCount;
import com.info.web.pojo.cspojo.CollectionStatistics;
import com.info.web.pojo.cspojo.CollectionStatisticsOrder;
import com.info.web.pojo.cspojo.StatisticsDistribute;
import com.info.web.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CollectionStatisticsServiceImpl implements ICollectionStatisticsService {
    @Resource
    private ICollectionStatisticsDao collectionStatisticsDao;
    @Resource
    private ICollectionSucCountDao collectionSucCountDao;
    @Resource
    private IMmanLoanCollectionOrderDao mmanLoanCollectionOrderDao;
    @Resource
    private ICollectionSucRecordDao collectionSucRecordDao;

    @Override
    public CollectionStatistics countPrincipal(Map<String, Object> map) {
        map.put("yesterday", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
        List<CollectionStatistics> list = collectionStatisticsDao.countPrincipal(map);
        if (list != null && list.size() > 0) {
            CollectionStatistics ls = list.get(0);
            BigDecimal rate = new BigDecimal(0);
            if (ls.getLoanMoney() != null && ls.getLoanMoney().compareTo(new BigDecimal(0)) > 0) {
                BigDecimal aa = ls.getLoanMoney().subtract(ls.getNotRepayment());
                rate = aa.divide(ls.getLoanMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                ls.setYetRepayment(ls.getLoanMoney().subtract(ls.getNotRepayment()));
                ls.setLoanMoneyRate(rate);
            } else {
                ls.setLoanMoney(new BigDecimal(0));
                ls.setNotRepayment(new BigDecimal(0));
                ls.setYesterdayMoney(new BigDecimal(0));
                ls.setYetRepayment(new BigDecimal(0));
                ls.setLoanMoneyRate(rate);
            }

            return ls;
        }
        return new CollectionStatistics();
    }

    @Override
    public CollectionStatistics countPrincipalNew(Map<String, Object> map) {
        List<CollectionStatistics> list = collectionStatisticsDao.countPrincipal(map);
        CollectionStatistics collectionStatistics = new CollectionStatistics();
        if (CollectionUtils.isNotEmpty(list)) {
            collectionStatistics = list.get(0);
        }
        return collectionStatistics;
    }


    @Override
    public CollectionStatisticsOrder countPrincipalOrder(Map<String, Object> map) {
        map.put("yesterday", DateUtil.getDateForDayBefor(1, "yyyy-MM-dd"));
        List<CollectionStatisticsOrder> list = collectionStatisticsDao.countPrincipalOrder(map);
        if (list != null && list.size() > 0) {
            CollectionStatisticsOrder ls = list.get(0);
            double rate = 0;
            if (ls.getLoanNum() != 0) {
                Integer yet = ls.getLoanNum() - ls.getNotRepayment();
                rate = ((double) yet) / ls.getLoanNum() * 100;
                BigDecimal temp = new BigDecimal(rate);
                rate = temp.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                ls.setYetRepayment(yet);
                ls.setLoanRate(rate);
            } else {
                ls.setLoanRate(rate);
            }
            return ls;
        }
        return new CollectionStatisticsOrder();
    }

    @Override
    public List<Object> countBySevenDay(Map<String, Object> map) {
        List<Map<String, Object>> obj = collectionStatisticsDao.countBySevenDay(map);
        List<EchartsEntity> entity = this.packPrams(obj, "create_date", "");
        return new Echarts().echart(entity, EchartsConstant.ECHART_LINE, "业务量折线图_本金");
    }

    @Override
    public List<Object> countOrderBySevenDay(Map<String, Object> map) {
        List<Map<String, Object>> obj = collectionStatisticsDao.countOrderBySevenDay(map);
        List<EchartsEntity> entity = this.packPrams(obj, "create_date", "order");
        return new Echarts().echart(entity, EchartsConstant.ECHART_LINE, "业务量折线图_订单");
    }


    /**
     * 本金总计_本金分布
     */
    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public StatisticsDistribute countByDistribute(Map<String, Object> map) {
        StatisticsDistribute res = new StatisticsDistribute();
        List<StatisticsDistribute> list = collectionStatisticsDao.countByDistribute(map);

        if (list != null && list.size() > 0) {
            BigDecimal money = new BigDecimal(0);
            BigDecimal penalty = new BigDecimal(0);
            for (StatisticsDistribute ls : list) {
                if (BackConstant.XJX_COLLECTION_ORDER_STATE_WAIT.equals(ls.getType()) || BackConstant.XJX_COLLECTION_ORDER_STATE_OUTSIDE.equals(ls.getType())) {
                    res.setWaitMoney(res.getWaitMoney().add(ls.getLoanMoney()));
                    res.setWaitPenalty(res.getWaitPenalty().add(ls.getLoanPenalty()));
                } else if (BackConstant.XJX_COLLECTION_ORDER_STATE_ING.equals(ls.getType())) {
                    res.setInMoney(res.getInMoney().add(ls.getLoanMoney()));
                    res.setInPenalty(res.getInPenalty().add(ls.getLoanPenalty()));
                } else if (BackConstant.XJX_COLLECTION_ORDER_STATE_PROMISE.equals(ls.getType())) {
                    res.setPromiseMoney(res.getPromiseMoney().add(ls.getLoanMoney()));
                    res.setPromisePenalty(res.getPromisePenalty().add(ls.getLoanPenalty()));
                } else if (BackConstant.XJX_COLLECTION_ORDER_STATE_SUCCESS.equals(ls.getType())) {
                    res.setFinishMoney(res.getFinishMoney().add(ls.getLoanMoney()));
                    res.setFinishPenalty(res.getFinishPenalty().add(ls.getLoanPenalty()));
                }
                money.add(ls.getLoanMoney());
                penalty.add(ls.getLoanPenalty());
            }
            if (money.compareTo(new BigDecimal(0)) > 0) {
                res.setWaitRate(res.getWaitMoney().divide(money, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                res.setInRate(res.getInMoney().divide(money, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                res.setPromiseRate(res.getPromiseMoney().divide(money, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                res.setFinishRate(res.getFinishMoney().divide(money, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            }
            if (penalty.compareTo(new BigDecimal(0)) > 0) {
                res.setWaitPenaltyRate(res.getWaitPenalty().divide(penalty, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                res.setInPenaltyRate(res.getInPenalty().divide(penalty, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                res.setPromisePenaltyRate(res.getPromisePenalty().divide(penalty, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
                res.setFinishPenaltyRate(res.getFinishPenalty().divide(penalty, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            }

        }
        return res;
    }

    @Override
    public List<StatisticsDistribute> countByDistributeNew(Map<String, Object> map) {
        return collectionStatisticsDao.countByDistribute(map);
    }

    /**
     * 组装echarts需要的参数格式
     *
     * @param lable 作为图表lable的字段名
     */
    private List<EchartsEntity> packPrams(List<Map<String, Object>> list, String lable, String type) {
        List<EchartsEntity> entity = new ArrayList<>();
        String lab = "";
        for (Map<String, Object> m : list) {
            EchartsEntity ee = new EchartsEntity();
            if (m.get(lable) instanceof Date) {
                Date date = (Date) m.get(lable);
                lab = DateUtil.getDateFormat(date, "yyyy-MM-dd");
            }
            double loanMoney;
            double notRepayment;
            String title = "本金总额";
            String title1 = "未还本金";
            if ("order".equals(type)) {
                loanMoney = m.get("loan_num") != null ? Double.parseDouble(m.get("loan_num").toString()) : 0;
                notRepayment = m.get("not_repayment") != null ? Double.parseDouble(m.get("not_repayment").toString()) : 0;
                title = "已还订单数";
                title1 = "未还订单数";
            } else {
                loanMoney = m.get("loan_money") != null ? ((BigDecimal) m.get("loan_money")).doubleValue() : 0;
                notRepayment = m.get("not_repayment") != null ? ((BigDecimal) m.get("not_repayment")).doubleValue() : 0;
            }
            ee.setLabels(lab);
            List<EchartsSeriesEntity> seriesList = new ArrayList<>();
            seriesList.add(new EchartsSeriesEntity(title, loanMoney, "line", null));
            seriesList.add(new EchartsSeriesEntity(title1, notRepayment, "line", null));
            ee.setValues(seriesList);
            entity.add(ee);
        }
        return entity;
    }

    @SuppressWarnings({"BigDecimalMethodWithoutRoundingCalled"})
    @Override
    public void countCollectionResult(String startTime, String endTime) {
        if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
            String format = "yyyy-MM-dd";
            startTime = DateUtil.getDateFormat(format);
            endTime = DateUtil.getDateFormat(DateUtil.addDay(new Date(), 1), format);
        }
        List<CollectionSucCount> dispatchResults = mmanLoanCollectionOrderDao.countIntoOrderNum(startTime, endTime);
        List<CollectionSucCount> sucCountList = collectionSucRecordDao.findSucCount(startTime, endTime);
        List<CollectionSucCount> endResult = new ArrayList<>();
        boolean dispatchNotEmp = CollectionUtils.isNotEmpty(dispatchResults);
        boolean sucNotEmp = CollectionUtils.isNotEmpty(sucCountList);
        if (dispatchNotEmp) {
            if (sucNotEmp) {
                Map<String, CollectionSucCount> sucMap = sucCountList.stream().collect(Collectors.toMap(CollectionSucCount::getMapK, Function.identity()));
                dispatchResults.forEach(one -> {
                    CollectionSucCount suc = sucMap.get(one.getMapK());
                    if (suc != null) {
                        one.setRepayNum(suc.getRepayNum());
                        one.setRenewNum(suc.getRenewNum());
                        one.setSucNum(one.getRenewNum() + one.getRepayNum());
                        BigDecimal sucRate = null;
                        if (one.getSucNum() > 0 && one.getIntoNum() > 0) {
                            sucRate = (new BigDecimal(one.getSucNum()).divide(new BigDecimal(one.getIntoNum()), 4, BigDecimal.ROUND_HALF_UP));
                        }
                        one.setSucRate(sucRate == null ? new BigDecimal(0) : sucRate);
                        sucCountList.remove(suc);
                    }
                    endResult.add(one);
                });
                if (sucCountList.size() > 0) {
                    endResult.addAll(sucCountList);
                }
            }
        }

        if (!(dispatchNotEmp && sucNotEmp) && (dispatchNotEmp || sucNotEmp)) {
            endResult.addAll(dispatchNotEmp ? dispatchResults : sucCountList);
        }
        if (CollectionUtils.isNotEmpty(endResult)) {
            HashMap<String, Object> params = new HashMap<>(3);
            String dateStr = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
            endResult.forEach(one -> {
                one.setSucNum(one.getRenewNum() + one.getRepayNum());
                if (one.getIntoNum().equals(0) || one.getSucNum().equals(0)) {
                    one.setSucRate(new BigDecimal(0));
                }
                params.put("group", one.getGroupLevel());
                params.put("name", one.getUserName());
                params.put("createTime", dateStr);
                CollectionSucCount exist = collectionSucCountDao.selectByGroupAndNameAndCreateDate(params);
                if (exist != null) {
                    one.setId(exist.getId());
                    collectionSucCountDao.updateByPrimaryKeySelective(one);
                } else {
                    one.setCreateTime(new Date());
                    collectionSucCountDao.insertSelective(one);
                }
                params.clear();
            });
        }
    }


}
