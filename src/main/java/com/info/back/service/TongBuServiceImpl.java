package com.info.back.service;

import com.info.back.dao.ITongBuDao;
import com.info.web.pojo.cspojo.*;
import com.info.web.util.DataSourceContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TongBuServiceImpl implements ITongBuService{
	
	@Resource
	private ITongBuDao tongBuDao;
	
	@Resource
	private IMmanUserInfoService mmanUserInfoService;
	@Resource
	private ISysUserBankCardService sysUserBankCardService;
	@Resource
	private IMmanUserRelaService mmanUserRelaService;
	@Resource
	private ICreditLoanPayService creditLoanPayService;
	@Resource
	private ICreditLoanPayDetailService creditLoanPayDetailService;
	@Resource
	private IMmanUserLoanService mmanUserLoanService;
	
	
	/**
	 *  逾期推送
		repayId
		type:OVERDUE
		还款推送
		repayId
		type:REPAY
	 */
	

	@Override
	public void tongbu(String id, String cz) {
		DataSourceContextHolder.setDbType("dataSourcexjx");
		//还款信息
		Map<Object,Object> hkmap;
		hkmap = tongBuDao.getAssetRepayment(id);
		if(hkmap.isEmpty()){
			return;
		}
		
		//还款详情
		List<Map<Object,Object>> assetRepaymentDetailmap;
		assetRepaymentDetailmap = tongBuDao.getAssetRepaymentDetail((java.lang.Long)hkmap.get("id"));
		
		//用户信息信息
		Map<Object,Object> usermap = new HashMap<>();
		usermap =  tongBuDao.getUserInfo((java.lang.Long)hkmap.get("user_id"));
		//用户联系人
		List<Map<Object,Object>>  userContactsmap;
		userContactsmap =  tongBuDao.getUserContacts((java.lang.Long)hkmap.get("user_id"));
		//银行卡
		Map<Object,Object> userCardmap;
		userCardmap = tongBuDao.getuserCardInfo((java.lang.Long)hkmap.get("user_id"));
		
		
		//借款详情
		Map<Object,Object> assetborrowordermap;
		assetborrowordermap = tongBuDao.getassetborroworder((java.lang.Long)hkmap.get("asset_order_id"));
		
		
		
		
		
		//BorrowOrder bo = borrowOrderService.findOneBorrow(re.getAssetOrderId());  通过借款ID拿到借款详情 (select * FROM asset_borrow_order where id = ?)
		//==assetborrowordermap
		DataSourceContextHolder.setDbType("dataSourcecs");

		/*HashMap<String, Object> params = new HashMap<>();
		params.put("userId", usermap.get("id"));
		List<UserContacts> userContacts = userContactsService.selectUserContacts(params);  通过用户ID拿到联系人列表  (select * from user_contacts where user_id = ?)*/
		//==userContactsmap
		// 应还本金
		int repaymentAmount =  Integer.parseInt(String.valueOf(hkmap.get("repaymentAmount")));
		int planLateFee = Integer.parseInt(String.valueOf(hkmap.get("planLateFee")));
		//int receivablePrinciple = (int)hkmap.get("repaymentAmount") - (int)hkmap.get("planLateFee");
		int receivablePrinciple =  repaymentAmount- planLateFee;
		// 实收的利息 = 已还金额 - 应还本金
		int realPenlty = repaymentAmount - receivablePrinciple;
	
		int count = creditLoanPayService.findcount(String.valueOf(assetborrowordermap.get("id")));
		if("OVERDUE".equals(cz)){
			
			if(0==count){
				/**
				 * 插入紧急联系人   用户信息  用户银行卡  联系人列表
				 */
				MmanUserInfo mmanUserInfo = new MmanUserInfo();
				mmanUserInfo = mmanUserInfoService.getxjxuser((java.lang.Long)hkmap.get("user_id"));
				
				mmanUserInfoService.saveNotNull(mmanUserInfo);
				MmanUserRela mmanUserRela = null;
				for(Map<Object,Object> abc : userContactsmap){
					mmanUserRela = new MmanUserRela();
					mmanUserRela.setUserId(String.valueOf(hkmap.get("user_id")));
					mmanUserRela.setContactsKey("");
					mmanUserRela.setRelaKey("");
					mmanUserRela.setInfoName(String.valueOf(abc.get("contact_name")));
					mmanUserRela.setInfoValue(String.valueOf(abc.get("contact_phone")));
					mmanUserRela.setContactsFlag("");
					mmanUserRelaService.saveNotNull(mmanUserRela);
				}
				mmanUserRela = new MmanUserRela();
				mmanUserRela.setUserId(String.valueOf(hkmap.get("user_id")));
				mmanUserRela.setContactsKey("1");
				mmanUserRela.setRelaKey( String.valueOf(hkmap.get("frist_contact_relation")));
				mmanUserRela.setInfoName( String.valueOf(hkmap.get("first_contact_name")));
				mmanUserRela.setInfoValue((String) String.valueOf(hkmap.get("first_contact_phone")));
				mmanUserRela.setContactsFlag("1");
				mmanUserRelaService.saveNotNull(mmanUserRela);
				mmanUserRela = new MmanUserRela();
				mmanUserRela.setUserId(String.valueOf(hkmap.get("user_id")));
				mmanUserRela.setContactsKey("1");
				mmanUserRela.setRelaKey(String.valueOf(hkmap.get("second_contact_relation")));
				mmanUserRela.setInfoName(String.valueOf(hkmap.get("second_contact_name")));
				mmanUserRela.setInfoValue(String.valueOf(hkmap.get("second_contact_phone")));
				mmanUserRela.setContactsFlag("1");
				mmanUserRelaService.saveNotNull(mmanUserRela);
				
				SysUserBankCard sysUserBankCard = new SysUserBankCard();
				sysUserBankCard.setUserId(String.valueOf(userCardmap.get("user_id")));
				sysUserBankCard.setBankCard(String.valueOf(userCardmap.get("card_no")));
				sysUserBankCard.setDepositBank(String.valueOf(userCardmap.get("bank_name")));
				sysUserBankCard.setMobile(String.valueOf(userCardmap.get("phone")));
				sysUserBankCardService.saveNotNull(sysUserBankCard);
				
			}
		
		
		/**
		 * 插入 还款 信息 还款详情  借款信息
		 */
		CreditLoanPay creditLoanPay = new CreditLoanPay();
		
		// 还款信息
				creditLoanPay.setId((String)hkmap.get("id"));
				creditLoanPay.setLoanId(String.valueOf(hkmap.get("asset_order_id")));
				creditLoanPay.setReceivableStartdate((Date)hkmap.get("credit_repayment_time"));
				creditLoanPay.setReceivableDate((Date)hkmap.get("repayment_time"));
				creditLoanPay.setReceivableMoney((BigDecimal)hkmap.get("repaymentAmount"));
				creditLoanPay.setRealMoney((BigDecimal)hkmap.get("repaymented_amount"));
				
				if(realPenlty <= 0){
					creditLoanPay.setReceivablePrinciple(new BigDecimal((receivablePrinciple-Integer.parseInt(String.valueOf(hkmap.get("repaymented_amount")))/100.00)));
					creditLoanPay.setReceivableInterest(new BigDecimal(Integer.parseInt(String.valueOf(hkmap.get("planLateFee")))/100.00));
					creditLoanPay.setRealgetPrinciple(new BigDecimal(Integer.parseInt(String.valueOf(hkmap.get("repaymented_amount")))/100.00));
					creditLoanPay.setRealgetInterest(new BigDecimal(0));
					
				}
				else{
					creditLoanPay.setReceivablePrinciple(new BigDecimal(0));
					int receivableInterest = Integer.parseInt(String.valueOf(hkmap.get("repaymentAmount"))) - Integer.parseInt(String.valueOf(hkmap.get("repaymented_amount")));
					creditLoanPay.setReceivableInterest(new BigDecimal(receivableInterest/100.00));
					int realgetPrinciple =Integer.parseInt(String.valueOf(hkmap.get("repaymented_amount"))) - realPenlty;
					creditLoanPay.setRealgetPrinciple(new BigDecimal(realgetPrinciple/100.00));
					creditLoanPay.setRealgetInterest(new BigDecimal(0));
				}
				creditLoanPay.setStatus(Integer.parseInt(String.valueOf(hkmap.get("status")))); 
				creditLoanPay.setCreateDate(new Date());
				creditLoanPayService.saveNotNull(creditLoanPay);
		
		
		CreditLoanPayDetail creditLoanPayDetail;
		for(Map<Object,Object> bcds :assetRepaymentDetailmap){
			creditLoanPayDetail = new CreditLoanPayDetail();
			creditLoanPayDetail.setId(String.valueOf(bcds.get("id")));
			creditLoanPayDetail.setPayId(String.valueOf(hkmap.get("asset_order_id")));
			if(realPenlty <= 0){
				creditLoanPayDetail.setRealMoney(new BigDecimal(Integer.parseInt(String.valueOf(bcds.get("true_repayment_money")))/ 100.00));
				creditLoanPayDetail.setRealPenlty(new BigDecimal(0));
				int realprinciple = receivablePrinciple - Integer.parseInt(String.valueOf(hkmap.get("repaymented_amount")));
				creditLoanPayDetail.setRealPrinciple(new BigDecimal(realprinciple/ 100.00));
				creditLoanPayDetail.setRealInterest(new BigDecimal(Integer.parseInt(String.valueOf(hkmap.get("planLateFee")))));
			}
			else{
				int realMoney = Integer.parseInt(String.valueOf(bcds.get("true_repayment_money"))) - realPenlty;
				creditLoanPayDetail.setRealMoney(new BigDecimal(realMoney/100.00));
				creditLoanPayDetail.setRealPenlty(new BigDecimal(realPenlty/100.00));
				creditLoanPayDetail.setRealPrinciple(new BigDecimal(0));
				int realInterest = Integer.parseInt(String.valueOf(hkmap.get("repaymentAmount"))) - Integer.parseInt(String.valueOf(hkmap.get("repaymented_amount")));
				creditLoanPayDetail.setRealInterest(new BigDecimal(realInterest/100.00));
			} 
			creditLoanPayDetail.setCreateDate(new Date());
			creditLoanPayDetail.setReturnType(String.valueOf(assetborrowordermap.get("repayment_type")));
			creditLoanPayDetail.setRemark(String.valueOf(assetborrowordermap.get("remark")));
			
			creditLoanPayDetailService.saveNotNull(creditLoanPayDetail);
		}
		
		MmanUserLoan mmanUserLoan = new MmanUserLoan();
		
			mmanUserLoan.setUserId(String.valueOf(hkmap.get("user_id")));
			mmanUserLoan.setLoanPyId(String.valueOf(hkmap.get("asset_order_id")));
			mmanUserLoan.setLoanMoney(new BigDecimal(Integer.parseInt(String.valueOf(assetborrowordermap.get("money_amount")))/100));
			mmanUserLoan.setLoanRate(String.valueOf(assetborrowordermap.get("apr")));
			mmanUserLoan.setLoanPenalty(new BigDecimal(Integer.parseInt(String.valueOf(hkmap.get("money_amount")))/100));
			mmanUserLoan.setLoanPenaltyRate(String.valueOf(hkmap.get("late_fee_apr")));
			mmanUserLoan.setLoanStatus(String.valueOf(hkmap.get("status")));
			mmanUserLoan.setLoanEndTime((Date)hkmap.get("repayment_time"));
			mmanUserLoan.setLoanStartTime((Date)hkmap.get("credit_repayment_time"));
			mmanUserLoan.setCreateTime(new Date());
			mmanUserLoanService.saveNotNull(mmanUserLoan);
		//}
		}
		
		if("REPAY".equals(cz)){
			creditLoanPayDetailService.deleteid(String.valueOf(hkmap.get("id")));
			if("34".equals(String.valueOf(hkmap.get("status")))){
				MmanUserLoan mm = new MmanUserLoan();
				mm.setUpdateTime(new Date());
				mm.setLoanPyId(String.valueOf(hkmap.get("id")));
				mm.setLoanStatus("5");
				mmanUserLoanService.updateNotNull(mm);
				CreditLoanPay creditLoanPay = new CreditLoanPay();
				creditLoanPay.setUpdateDate(new Date());
				creditLoanPay.setStatus(5);
				creditLoanPay.setLoanId(String.valueOf(assetborrowordermap.get("id")));
				creditLoanPayService.updateNotNull(creditLoanPay);
			}
			CreditLoanPayDetail creditLoanPayDetail;
			for(Map<Object,Object> bcds :assetRepaymentDetailmap){
				creditLoanPayDetail = new CreditLoanPayDetail();
				creditLoanPayDetail.setId(String.valueOf((String)bcds.get("id")));
				creditLoanPayDetail.setPayId(String.valueOf(hkmap.get("asset_order_id")));
				if(realPenlty <= 0){
					creditLoanPayDetail.setRealMoney(new BigDecimal(Integer.parseInt(String.valueOf(bcds.get("true_repayment_money")))/ 100.00));
					creditLoanPayDetail.setRealPenlty(new BigDecimal(0));
					int realprinciple = receivablePrinciple - Integer.parseInt(String.valueOf(hkmap.get("repaymented_amount")));
					creditLoanPayDetail.setRealPrinciple(new BigDecimal(realprinciple/ 100.00));
					creditLoanPayDetail.setRealInterest(new BigDecimal(Integer.parseInt(String.valueOf(hkmap.get("planLateFee")))));
				}
				else{
					int realMoney = Integer.parseInt(String.valueOf(bcds.get("true_repayment_money"))) - realPenlty;
					creditLoanPayDetail.setRealMoney(new BigDecimal(realMoney/100.00));
					creditLoanPayDetail.setRealPenlty(new BigDecimal(realPenlty/100.00));
					creditLoanPayDetail.setRealPrinciple(new BigDecimal(0));
					int realInterest = Integer.parseInt(String.valueOf(hkmap.get("repaymentAmount"))) - Integer.parseInt(String.valueOf(hkmap.get("repaymented_amount")));
					creditLoanPayDetail.setRealInterest(new BigDecimal(realInterest/100.00));
				} 
				creditLoanPayDetail.setCreateDate(new Date());
				/*creditLoanPayDetail.put("returnType", rd.getRepaymentType());
				creditLoanPayDetail.put("remark", rd.getRemark());*/
				creditLoanPayDetail.setReturnType(String.valueOf(assetborrowordermap.get("repayment_type")));
				creditLoanPayDetail.setRemark(String.valueOf(assetborrowordermap.get("remark")));
				creditLoanPayDetailService.saveNotNull(creditLoanPayDetail);
			}
			
		}
	}

}
