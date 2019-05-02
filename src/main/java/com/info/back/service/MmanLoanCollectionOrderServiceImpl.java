package com.info.back.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.info.back.dao.IMmanUserInfoDao;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrder;
import com.info.back.dao.IMmanLoanCollectionOrderDao;
import com.info.back.dao.IMmanUserLoanDao;
import com.info.back.dao.IPaginationDao;
import com.info.back.result.JsonResult;
import com.info.web.pojo.cspojo.OrderBaseResult;
import com.info.back.utils.IdGen;
import com.info.web.util.PageConfig;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.info.web.pojo.cspojo.MmanUserInfo;

@Service
public class MmanLoanCollectionOrderServiceImpl implements IMmanLoanCollectionOrderService{
	
	@Resource
	private IMmanLoanCollectionOrderDao mmanLoanCollectionOrderDao;
	
	@Resource
	private IMmanLoanCollectionOrderDao manLoanCollectionOrderDao;
	
	@Resource
	private IPaginationDao paginationDao;
	
	@Resource
	private IMmanUserInfoDao mmanUserInfoDao;
	
    @Resource
    private IMmanLoanCollectionOrderDao collectionOrderDao;
    @Resource
    private IMmanUserLoanService iMmanUserLoanService;
    @Resource
    private IMmanUserLoanDao iMmanUserLoanDao;
    
	@Override
    public List<MmanLoanCollectionOrder> getOrderList(MmanLoanCollectionOrder mmanLoanCollectionOrder) {
		return mmanLoanCollectionOrderDao.getOrderList(mmanLoanCollectionOrder);
	}

	@Override
	public PageConfig<MmanLoanCollectionOrder> findPage(HashMap<String, Object> params) {
		PageConfig<MmanLoanCollectionOrder> page = new PageConfig<MmanLoanCollectionOrder>();
		page = paginationDao.findPage("getOrderPage", "getOrderPageCount", params, null);
		return page;
	}
	@Override
    public List<MmanLoanCollectionOrder> findList(MmanLoanCollectionOrder queryManLoanCollectionOrder) {
		return manLoanCollectionOrderDao.findList(queryManLoanCollectionOrder);
	}

	
	@Override
    public void saveMmanLoanCollectionOrder(MmanLoanCollectionOrder order) {
		
		if(order!=null && StringUtils.isNotBlank(order.getUserId())){
			MmanUserInfo mmanUserInfo = mmanUserInfoDao.get(order.getUserId());
			if(null!=mmanUserInfo){
				order.setLoanUserName(mmanUserInfo.getRealname());
				order.setLoanUserPhone(mmanUserInfo.getUserPhone());
			}
		}
		
		
		if(StringUtils.isBlank(order.getId())){
			order.setId(IdGen.uuid());
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			manLoanCollectionOrderDao.insertCollectionOrder(order);
		}else{
			order.setUpdateDate(new Date());
			manLoanCollectionOrderDao.updateCollectionOrder(order);
		}
		
	}
	@Override
	public PageConfig<OrderBaseResult> getPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "MmanLoanCollectionOrder");
		PageConfig<OrderBaseResult> page;
		page = paginationDao.findPage("getCollectionOrderList", "getCollectionOrderCount", params, null);
		return page == null ? new PageConfig<>() : page;
	}

	@Override
	public int findAllCount(HashMap<String, Object> params) {
		return manLoanCollectionOrderDao.getOrderPageCount(params);
	}

	@Override
	public void updateRecord(MmanLoanCollectionOrder mmanLoanCollectionOrder) {
		manLoanCollectionOrderDao.updateCollectionOrder(mmanLoanCollectionOrder);
	}
	@Override
	public MmanLoanCollectionOrder getOrderById(String id) {
		return manLoanCollectionOrderDao.getOrderById(id);
	}

	@Override
	public MmanLoanCollectionOrder getOrderByUserId(String userId) {
		return manLoanCollectionOrderDao.getOrderByUserId(userId);
	}

	@Override
	public OrderBaseResult getBaseOrderById(String orderId) {
		return manLoanCollectionOrderDao.getBaseOrderById(orderId);
	}

	@Override
	public JsonResult saveTopOrder(Map<String, Object> params) {
		JsonResult result=new JsonResult("-1","标记订单重要程度失败");
		if(StringUtils.isNotBlank(params.get("id")+"")&&StringUtils.isNotBlank(params.get("topLevel")+"")){
			int count=manLoanCollectionOrderDao.updateTopOrder(params);
			if(count>0){
				result.setCode("0");
				result.setMsg("");
			}
		}
		return result;
	}

	@Override
	public MmanLoanCollectionOrder getOrderWithId(String orderId) {
		return manLoanCollectionOrderDao.getOrderWithId(orderId);
	}

	@Override
	public PageConfig<OrderBaseResult> getMyPage(HashMap<String, Object> params) {
		params.put(Constant.NAME_SPACE, "MmanLoanCollectionOrder");
		PageConfig<OrderBaseResult> page = new PageConfig<OrderBaseResult>();
		page = paginationDao.findPage("getCollectionMyOrderList", "getCollectionMyOrderCount", params, null);
		return page;
	}

	@Override
	public int updateJmStatus(MmanLoanCollectionOrder collectionOrder) {
		return manLoanCollectionOrderDao.updateJmStatus(collectionOrder);
	}

	@Override
	public List<HashMap<String,Object>> selectZzcBlackList(){
		List<HashMap<String,Object>> hashMapList = manLoanCollectionOrderDao.selectZzcBlackList();
		return hashMapList;
	}
	
}
