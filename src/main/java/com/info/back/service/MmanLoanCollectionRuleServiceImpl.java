package com.info.back.service;

import com.info.back.dao.IMmanLoanCollectionRuleDao;
import com.info.back.dao.IPaginationDao;
import com.info.back.result.JsonResult;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.MmanLoanCollectionRule;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Slf4j
@Service
public class MmanLoanCollectionRuleServiceImpl implements IMmanLoanCollectionRuleService {

    @Resource
    private IPaginationDao<MmanLoanCollectionRule> paginationDao;
    @Resource
    private IMmanLoanCollectionRuleDao mmanLoanCollectionRuleDao;


    @Override
    public PageConfig<MmanLoanCollectionRule> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "MmanLoanCollectionRule");
        return paginationDao.findPage("findAll", "findAllCount", params, null);
    }

    @Override
    public MmanLoanCollectionRule getCollectionRuleById(String id) {
        return mmanLoanCollectionRuleDao.getCollectionRuleById(id);
    }

    @Override
    public JsonResult updateCollectionRule(MmanLoanCollectionRule collectionRule) {
        JsonResult result = new JsonResult("-1", "修改规则失败");
        try {
            if (mmanLoanCollectionRuleDao.update(collectionRule) > 0) {
                result.setCode("0");
                result.setMsg("修改规则成功");
            }
        } catch (Exception e) {
            log.error("MmanLoanCollectionRuleService update", e);
        }
        return result;
    }

}
