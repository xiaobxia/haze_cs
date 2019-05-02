package com.info.mqlistener;

import com.info.back.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 消费类
 *
 * @author cqry_2016
 *  2018-09-10 14:01
 **/

@Component
public class ConsumeService {

    private List<MessageConsumerStrategy> rules;

    public void setRules(List<MessageConsumerStrategy> rules) {
        this.rules = rules;
    }

    public void process(String tag, List<String> repaymentIdList, Map<String, String> callBackParams) throws BusinessException {
        for (MessageConsumerStrategy rule : rules) {
                rule.process(tag, repaymentIdList, callBackParams);
        }
    }
}
