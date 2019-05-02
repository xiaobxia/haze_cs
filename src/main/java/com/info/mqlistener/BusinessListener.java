package com.info.mqlistener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.info.back.utils.SpringUtils;
import com.info.web.util.JsonUtil;
import com.tools.mq.consumer.AliMessageListener;
import com.tools.mq.consumer.BaseListener;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author  by cqry_2016 on 2018/6/28
 * 逾期和部分还款监听
 */
@Slf4j
@AliMessageListener(topicName = "order", tagName = "overdue||renewal||repay")
public class BusinessListener extends BaseListener {
    @Override
    public Action messageConsume(Message message, ConsumeContext consumeContext) {
        int callSize = 2;
        Map<String, String> callBackParams = new HashMap<>(callSize);
        Action result;
        String tagName = message.getTag();
        List<String> repaymentIdList;
        try {
            String msgStr = new String(message.getBody());
            if (JsonUtil.isJSONValid(msgStr)) {
                JSONObject body = JSON.parseObject(msgStr);
                String partRepayId = body.getString("context");
                String mqKey = body.getString("mqKey");
                callBackParams.put("mqKey", mqKey);
                repaymentIdList = Collections.singletonList(partRepayId);
            } else {
                String overdueBody = new String(message.getBody());
                repaymentIdList = Arrays.asList(overdueBody.split(","));
            }
            ConsumeService consumerStrategy = SpringUtils.getBean("consumeService");
            consumerStrategy.process(tagName, repaymentIdList, callBackParams);
            result = Action.CommitMessage;
        } catch (Exception e) {
            result = Action.ReconsumeLater;
            log.error("BusinessListener messageConsume error: {}", e);
        }
        return result;
    }
}
