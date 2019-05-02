package com.info.web.synchronization.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.info.back.vo.LoanRecord;
import com.info.back.vo.RenewOrPayRecord;
import com.info.mongo.document.ShuJuMoHeMessage;
import com.info.mongo.repository.IShuJuMoHeMessageRepository;
import com.info.risk.pojo.RiskOrders;
import com.info.web.copys.pojo.User;
import com.info.web.copys.pojo.UserContacts;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class DataDao extends BaseDao implements IDataDao {

//    @Resource
//    IShuJuMoHeMessageRepository shuJuMoHeMessageRepository;

    private String getLatestTdRawData(String idCard, String phone) {
//        List<ShuJuMoHeMessage> messages = shuJuMoHeMessageRepository.findByIdentityCardAndUserPhone(idCard, phone);
//        if (messages != null && messages.size() > 0) {
//            return messages.get(messages.size() - 1).getMessage();
//        }
        return null;
    }

    private User appendTdRawData(User user) {
        if (user != null) {
            String tdRawData = getLatestTdRawData(user.getIdNumber(), user.getUserPhone());
            if (tdRawData != null) {
                user.setTdRawData(tdRawData);
            }
        }
        return user;
    }

    @Override
    public HashMap<String, Object> getAssetBorrowOrder(HashMap<String, String> map) {
        return getSqlSessionTemplates().selectOne("getAssetBorrowOrder", map);
    }


    @Override
    public HashMap<String, Object> getAssetRepayment(HashMap<String, String> map) {
        return getSqlSessionTemplates().selectOne("getAssetRepayment", map);
    }

    @Override
    public List<HashMap<String, Object>> getAssetRepaymentDetail(HashMap<String, String> map) {
        return getSqlSessionTemplates().selectList("getAssetRepaymentDetail", map);
    }

    @Override
    public HashMap<String, Object> getUserCardInfo(HashMap<String, String> map) {
        return getSqlSessionTemplates().selectOne("getUserCardInfo", map);
    }

    @Override
    public List<HashMap<String, Object>> getUserContacts(HashMap<String, String> map) {
        return getSqlSessionTemplates().selectList("getUserContacts", map);
    }

    @Override
    public HashMap<String, Object> getUserInfo(HashMap<String, String> map) {
        return getSqlSessionTemplates().selectOne("getUserInfo", map);
    }

    @Override
    public User selectByUserId(Integer id) {
        return appendTdRawData(getSqlSessionTemplates().selectOne("selectByUserId", id));

    }

    /**
     * 查询
     */
    @Override
    public List<UserContacts> selectUserContacts(Map<String, Object> params) {
        return this.getSqlSessionTemplates().selectList("selectUserContactsPage", params);
    }

    /**
     * 根据订单id查询 creditReport信息
     *
     * @param parms
     * @return
     */
    @Override
    public RiskOrders selectCreditReportByBorrowId(HashMap<String, Object> parms) {
        return getSqlSessionTemplates().selectOne("selectCreditReportByBorrowId", parms);
    }

    @Override
    public HashMap<String, Object> selectUserLastLoginLog(String userId) {
        return getSqlSessionTemplates().selectOne("selectUserLastLoginLog", userId);
    }

    @Override
    public List<HashMap<Integer, Object>> findUserFromData(List<Integer> ids) {
        return getSqlSessionTemplates().selectList("findUserFromData", ids);
    }

    @Override
    public List<LoanRecord> findUserLoanRecord(Integer userId) {
        return getSqlSessionTemplates().selectList("findUserLoanRecord", userId);
    }

    @Override
    public List<RenewOrPayRecord> findUserRenewOrPayRecord(Integer assetOrderId) {
        return getSqlSessionTemplates().selectList("findUserRenewOrPayRecord", assetOrderId);
    }

    @Override
    public HashMap<String, Object> selectRenewalRecord(Integer repayId) {
        return getSqlSessionTemplates().selectOne("selectRenewalRecord", repayId);
    }

    @Override
    public List<HashMap<Integer, Long>> findOrderRenewalCount(List<Integer> ids) {
        return getSqlSessionTemplates().selectList("findOrderRenewalCount", ids);
    }

    @Override
    public List<Date> getOrderRenewTime(String repayId) {
        return getSqlSessionTemplates().selectList("getOrderRenewTime", repayId);
    }

    @Override
    public Date getOrderRepayTime(String repayId) {
        return getSqlSessionTemplates().selectOne("getOrderRepayTime", repayId);
    }
}
