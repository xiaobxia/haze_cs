package com.info.back.dao;

import com.info.back.vo.CollectionSucCount;
import com.info.back.vo.MmmanOrderId;
import com.info.back.vo.PerformanceResult;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrder;
import com.info.web.pojo.cspojo.OrderBaseResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface IMmanLoanCollectionOrderDao {
	
	
	public List<MmanLoanCollectionOrder> getOrderList(MmanLoanCollectionOrder mmanLoanCollectionOrder);

	
	
	public List<MmanLoanCollectionOrder> findList(MmanLoanCollectionOrder queryParam);
	
	public void insertCollectionOrder(MmanLoanCollectionOrder queryParam);
	
	
	public void updateCollectionOrder(MmanLoanCollectionOrder queryParam);



	public int getOrderPageCount(HashMap<String, Object> params);



	public MmanLoanCollectionOrder getOrderById(String id);

	/**
	 * 标记订单
	 * @param params
	 * @return
	 */
	public int updateTopOrder(Map<String, Object> params);


	public void updateAuditStatus(HashMap<String, String> params);

	/**
	 * 根据订单id查询一条订单记录
	 * @param orderId
	 * @return
	 */
	public MmanLoanCollectionOrder getOrderWithId(String orderId);


	/**
	 * 
	 * 减免状态更新
	 * @return
	 */
	public int updateJmStatus(MmanLoanCollectionOrder collectionOrder);

	/**
	 *  审核更新状态
	 * @param params
	 */
	public void sveUpdateJmStatus(HashMap<String, String> params);


	/**
	 * 查询催收成功订单
	 */

	public  List<MmanLoanCollectionOrder> getSelectList();


	void updateReductionMoney( HashMap<String, Object> order);
	/**
	 * 根据userId获取订单
	 */
    MmanLoanCollectionOrder getOrderByUserId(String userId);

	void sveUpdateNotNull(HashMap<String, String> ordermap);
	/**
	 * 根据orderId获取baseOrder
	 */
    OrderBaseResult getBaseOrderById(String orderId);


	public List<HashMap<String,Object>> selectZzcBlackList();

	List<PerformanceResult> getPerformanceResults(HashMap<String, Object> params);

	List<MmmanOrderId> getIdByLoanId(List<Integer> loanIds);

	/**
	 * 按条件查找订单数量
	 * @param map
	 * @return
	 */
	int getOrderCount(Map<String,Object> map);

	/**
	 * 按条件查找借款id
	 * @param map
	 * @return
	 */
	List<Integer> getLoanIds(Map<String,Object> map);

	void updateOrderRenewCount(@Param("dataMap") Map<String,Integer> dataMap);

	List<CollectionSucCount> countIntoOrderNum(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
