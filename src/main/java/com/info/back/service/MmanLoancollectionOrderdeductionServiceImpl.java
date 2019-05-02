package com.info.back.service;

import com.info.back.dao.*;
import com.info.back.result.JsonResult;
import com.info.back.utils.IdGen;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrder;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrderdeduction;
import com.info.web.pojo.cspojo.MmanUserInfo;
import com.info.web.pojo.cspojo.MmanUserLoan;
import com.info.web.util.CompareUtils;
import com.info.web.util.PageConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MmanLoancollectionOrderdeductionServiceImpl implements IMmanLoanCollectionOrderdeductionService {

    @Resource
    private ImmanLoanCollectionOrderdeductiondao collectionOrderdeductiondao;
    @Resource
    private IMmanLoanCollectionOrderDao mmanLoanCollectionOrderDao;
    @Resource
    private IMmanUserLoanDao iMmanUserLoanDao;
    @Resource
    private IMmanUserInfoDao mmanUserInfoDao;
    @Resource
    private IPaginationDao paginationDao;

    @Override
    public JsonResult saveorderdeduction(HashMap<String, Object> params) {
        JsonResult result = new JsonResult("-1", "申请减免失败！");
        Date now = new Date();
        BigDecimal bigDecimal = new BigDecimal(0);
        MmanLoanCollectionOrderdeduction collectionOrderdeduction = new MmanLoanCollectionOrderdeduction();
        MmanLoanCollectionOrder collectionOrder = mmanLoanCollectionOrderDao.getOrderById(params.get("id").toString());
        MmanUserLoan loan;
        MmanUserInfo userInfo;
        loan = iMmanUserLoanDao.get(collectionOrder.getLoanId());
        System.out.println(loan.getLoanPenalty());
        userInfo = mmanUserInfoDao.get(collectionOrder.getUserId());

        if (userInfo != null) {

            if (CompareUtils.greaterEquals(loan.getLoanPenalty(), bigDecimal)) {
                //减免金额
                collectionOrderdeduction.setDeductionmoney(new BigDecimal(Integer.valueOf((String) params.get("deductionmoney"))));
                //备注
                collectionOrderdeduction.setDeductionremark((String) params.get("deductionremark"));
                //创建时间
                collectionOrderdeduction.setCreatetime(now);
                collectionOrderdeduction.setId(IdGen.uuid());
                collectionOrderdeduction.setLoanrealname(userInfo.getRealname());
                collectionOrderdeduction.setLoanuserphone(userInfo.getUserPhone());
                collectionOrderdeduction.setReturnmoney(loan.getLoanMoney());
                int count = collectionOrderdeductiondao.insertSelective(collectionOrderdeduction);

                if (count > 0) {
                    result.setMsg("申请减免成功");
                    result.setCode("0");
                }
            } else {
                result.setData("减免金额不能大于" + loan.getLoanPenalty());
            }
        } else {
            result.setMsg("用户信息不能为空！");
        }
        return result;

    }


    @Override
    @SuppressWarnings("unchecked")
    public PageConfig<MmanLoanCollectionOrderdeduction> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "MmanLoanCollectionOrderdeduction");
        PageConfig<MmanLoanCollectionOrderdeduction> pageConfig;
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, null);
        return pageConfig;
    }


    @Override
    public List<MmanLoanCollectionOrderdeduction> findAllList(String id) {
        return collectionOrderdeductiondao.findAllList(id);
    }
}
