package com.info.web.synchronization.dao;

import com.info.back.vo.LoanRecord;
import com.info.back.vo.RenewOrPayRecord;
import com.info.risk.pojo.RiskOrders;
import com.info.web.copys.pojo.User;
import com.info.web.copys.pojo.UserContacts;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDataDao {
	/**
	 * 获取逾期
	 * @param map
	 * @return
	 */
	public HashMap<String,Object> getAssetRepayment(HashMap<String,String> map);
	/**
	 * 获取还款详情
	 * @param map
	 * @return
	 */
	public List<HashMap<String,Object>> getAssetRepaymentDetail(HashMap<String,String> map);
	/**
	 * 获取手机通讯录
	 * @param map
	 * @return
	 */
	public List<HashMap<String,Object>> getUserContacts(HashMap<String,String> map);
	/**
	 * 获取银行卡
	 * @param map
	 * @return
	 */
	public HashMap<String,Object> getUserCardInfo(HashMap<String,String> map);
	/**
	 * 获取用户信息
	 * @param map
	 * @return
	 */
	public HashMap<String,Object> getUserInfo(HashMap<String,String> map);
	/**
	 * 获取借款详情
	 * @param map
	 * @return
	 */
	public HashMap<String,Object> getAssetBorrowOrder(HashMap<String,String> map);

	public User selectByUserId(Integer id);

	/**
	 * 查询联系人信息
	 * @param params
	 * @return
	 */
	public List<UserContacts> selectUserContacts(Map<String, Object> params);

	/**
	 * 根据订单id查询 creditReport信息
	 * @param parms
	 * @return
	 */
	public RiskOrders selectCreditReportByBorrowId(HashMap<String,Object> parms);

	/**
	 * 查询用户最后一次登录日志
	 *
	 * @param userId
	 * @return
	 */
	public HashMap<String, Object> selectUserLastLoginLog(String userId);

	List<HashMap<Integer, Object>> findUserFromData(List<Integer> ids);

	List<LoanRecord> findUserLoanRecord(Integer userId);

	List<RenewOrPayRecord> findUserRenewOrPayRecord(Integer assetOrderId);

	HashMap<String, Object> selectRenewalRecord(Integer repayId);

	List<HashMap<Integer, Long>> findOrderRenewalCount(List<Integer> ids);

	List<Date> getOrderRenewTime(String repayId);

	Date getOrderRepayTime(String repayId);
}
