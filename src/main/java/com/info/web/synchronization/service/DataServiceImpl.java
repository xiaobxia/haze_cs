package com.info.web.synchronization.service;

import com.info.back.dao.IMmanLoanCollectionOrderDao;
import com.info.back.vo.LoanRecord;
import com.info.back.vo.MmmanOrderId;
import com.info.back.vo.RenewOrPayRecord;
import com.info.web.synchronization.dao.IDataDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataServiceImpl implements IDataService {


    @Resource
    private IDataDao dataDao;
    @Resource
    private IMmanLoanCollectionOrderDao mmanLoanCollectionOrderDao;


    @Override
    public List<HashMap<Integer, Object>> getUserFromData(List<Integer> ids) {
        return dataDao.findUserFromData(ids);
    }

    @Override
    public List<LoanRecord> getUserLoanRecord(Integer userId) {
        List<LoanRecord> loanRecords = dataDao.findUserLoanRecord(userId);
        if (CollectionUtils.isNotEmpty(loanRecords)) {
            List<Integer> loanIds = loanRecords.stream().map(LoanRecord::getLoanId).collect(Collectors.toList());
            List<MmmanOrderId> orderIds = mmanLoanCollectionOrderDao.getIdByLoanId(loanIds);
            Map<Integer, String> mmIdMap = null;
            if (CollectionUtils.isNotEmpty(orderIds)) {
                mmIdMap = orderIds.stream().collect(Collectors.toMap(MmmanOrderId::getLoanId, MmmanOrderId::getId));
            }
            //计算逾期总天数
            List<RenewOrPayRecord> renewOrPayRecords;
            int renewLateDays;
            for (LoanRecord loanRecord : loanRecords) {
                renewOrPayRecords = dataDao.findUserRenewOrPayRecord(loanRecord.getLoanId());
                if (MapUtils.isNotEmpty(mmIdMap)) {
                    String orderId = mmIdMap.get(loanRecord.getLoanId());
                    loanRecord.setMmanloanOrderId(orderId == null ? "" : orderId);
                }
                if (CollectionUtils.isNotEmpty(renewOrPayRecords)) {
                    for (RenewOrPayRecord renewOrPayRecord : renewOrPayRecords) {
                        if ("续期".equals(renewOrPayRecord.getHandleType())) {
                            renewLateDays = (renewOrPayRecord.getRepayedAmount() / 100 - 215) / 30;
                            loanRecord.setLateDays(loanRecord.getLateDays() + renewLateDays);
                        }
                    }
                }
            }

        }
        return loanRecords;
    }

    @Override
    public List<RenewOrPayRecord> getUserRenewOrPayRecord(Integer assetOrderId) {
        return dataDao.findUserRenewOrPayRecord(assetOrderId);
    }

    @Override
    public List<HashMap<Integer, Long>> getOrderRenewalCount(List<Integer> ids) {
        return dataDao.findOrderRenewalCount(ids);
    }
}
