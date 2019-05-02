package com.info.mqlistener;

import com.info.back.exception.BusinessException;

import java.util.List;
import java.util.Map;

public interface MessageConsumerStrategy {

    void process(String tag, List<String> repaymentIdList, Map<String, String> callBackParams) throws BusinessException;
}
