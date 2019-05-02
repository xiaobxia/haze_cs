package com.info.back.service;

import com.info.back.dao.*;
import com.info.back.result.JsonResult;
import com.info.back.vo.CollectionSucCount;
import com.info.back.vo.CollectionSucRecord;
import com.info.back.vo.PerformanceCountRecord;
import com.info.back.vo.PerformanceTotalResult;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackRole;
import com.info.web.pojo.cspojo.Collection;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class CollectionServiceImpl implements ICollectionService {

    @Resource
    private IPaginationDao paginationDao;
    @Resource
    private ICollectionDao collectionDao;
    @Resource
    private IMmanLoanCollectionCompanyDao mmanLoanCollectionCompanyDao;
    @Resource
    private IBackUserService backUserService;
    @Resource
    private IPerformanceCountRecordDao performanceCountRecordDao;
    @Resource
    private IBackRoleDao backRoleDao;
    @Resource
    private ISysDictDao sysDictDao;

    @Override
    public PageConfig<Collection> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "Collection");
        PageConfig<Collection> pageConfig = new PageConfig<Collection>();
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, null);
        return pageConfig;
    }

    @Override
    public Collection findOneCollection(Integer id) {
        return collectionDao.findOneCollection(id);
    }

    @Override
    public JsonResult updateById(Collection collection) {
        JsonResult result = new JsonResult("-1", "修改催收员失败");
        try {
            Collection oldCollection = collectionDao.findOneCollection(collection.getId());
            int count = 0;
            if (oldCollection.getGroupLevel() == null || oldCollection.getGroupLevel().equals(collection.getGroupLevel())) {
                count = collectionDao.updateById(collection);
            } else {
                int orderCount = collectionDao.findOrderCollection(oldCollection.getUuid());//统计催员手上未完成的订单
                if (orderCount <= 0) {
                    count = collectionDao.updateById(collection);
                } else {
                    result.setMsg("该催收员还有" + orderCount + "条订单未完成不能转组修改。等完成订单或转派给他人后再修改");
                }
            }
            if (count > 0) {
                result.setCode("0");
                result.setMsg("修改催收员成功");
            }
        } catch (Exception e) {
            log.error("CollectionService updateById", e);
        }
        return result;
    }

    @Override
    public JsonResult insert(Collection collection) {
        JsonResult result = new JsonResult("-1", "添加催员失败");
        Integer id = collectionDao.insert(collection);
        if (id > 0) {//赋予催收员角色
            HashMap<String, Object> params = new HashMap<>();
            //总催收员角色名
            String csRoleName = sysDictDao.findDictByType("csRoleName").get(0).getValue();
            BackRole role = backRoleDao.findByName(csRoleName);
            params.put("roleIds", role.getId().toString().split(","));//催收员角色ID
            params.put("id", collection.getId().toString());
            backUserService.addUserRole(params);
            result.setCode("0");
            result.setMsg("添加催收员成功");
        }
        return result;
    }

    @Override
    public JsonResult deleteCollection(Integer id) {
        JsonResult result = new JsonResult("-1", "删除催员失败");
        try {
            Collection collection = collectionDao.findOneCollection(id);
            if (collection != null) {
                int orderCount = collectionDao.findOrderCollection(collection.getUuid());//统计催员手上未完成的订单
                if (orderCount <= 0) {
                    //标记用户为删除
                    orderCount = collectionDao.updateDeleteCoection(collection.getUuid());
                    if (orderCount > 0) {
                        List<String> uuidlist = new ArrayList<>();
                        uuidlist.add(collection.getUuid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("UUIdlist", uuidlist);
                        //标记订单为需要删除 统计过后删除该数据
                        mmanLoanCollectionCompanyDao.updateOrderStatus(map);
                    }
                    result.setCode("0");
                    result.setMsg("删除催收员成功");
                } else {
                    result.setMsg("该催收员还有" + orderCount + "条订单未完成不能删除。等完成订单或转派给他人后再删除");
                }
            } else {
                result.setMsg("该催收员不存在");
            }
        } catch (Exception e) {
            log.error("CollectionService insert", e);
        }
        return result;
    }

    @Override
    public PageConfig<PerformanceCountRecord> findPerformancePage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "PerformanceCountRecord");
        return paginationDao.findPage("findAll", "findAllCount", params, null);
    }

    @Override
    public void deleteTagDelete() {
        log.error("删除标记为删除的订单 CollectionService deleteTagDelete");
        try {
            int deleteCoun = collectionDao.findTagDelete();
            if (deleteCoun > 0) {
                deleteCoun = collectionDao.deleteTagDelete();
                log.error("删除标记为删除的订单 总条数" + deleteCoun);
            } else {
                log.error("CollectionService deleteTagDelete 暂时没有需要删除的订单");
            }
        } catch (Exception e) {
            log.error("CollectionService deleteTagDelete", e);
        }
    }

    @Override
    public PageConfig<CollectionSucRecord> findRepayRenewRecordPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "CollectionSucRecord");
        return paginationDao.findPage("findAll", "findAllCount", params, null);
    }

    @Override
    public int findAllCount(HashMap<String, Object> params) {
        return performanceCountRecordDao.findAllCount(params);
    }

    @Override
    public PerformanceTotalResult getPerformanceTotalResult(HashMap<String, Object> params) {
        return performanceCountRecordDao.getTotal(params);
    }

    @Override
    public PageConfig<CollectionSucCount> findCollecSucCountPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "CollectionSucCount");
        return paginationDao.findPage("findAll", "findAllCount", params, null);
    }
}
