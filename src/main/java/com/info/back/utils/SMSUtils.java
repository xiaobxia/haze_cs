package com.info.back.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.info.web.util.HttpUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: SMSSender
 * @Description: 小鱼儿 发送 短信
 * @author   duj
 * @date 2016-7-19 下午03:51:34
 *
 */


public class SMSUtils {


    //创蓝短信接口
    public String sendSms(String telephone, String content) {
        String code = "1";
//        // 普通短信地址
//        String smsSingleRequestServerUrl = SmsCLUtil.APIURL;
//        String account = SmsCLUtil.USER;
//        String pswd = SmsCLUtil.APPKEY;
//        // 短信内容
//        String msg =  content+"";
//
//        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, telephone );
//        String requestJson = JSON.toJSONString(smsSingleRequest);
//
//        String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//        if(null!=response){
//            SmsSendResponse r = JSON.parseObject(response, SmsSendResponse.class);
//            if(r.getCode().equals(Constant.SMS_SEND_SUCC)){
//                code = "0";
//            }
//        }

//        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, telephone);
//        String requestJson = JSON.toJSONString(smsSingleRequest);
        String sm = "";
        try{
            //做URLEncoder - UTF-8编码
            sm = URLEncoder.encode(content, "utf8");
        }catch (Exception e){
            return "0";
        }
        //将参数进行封装
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("un", SmsCLUtil.USER);
        paramMap.put("pw", SmsCLUtil.APPKEY);
        //单一内容时群发  将手机号用;隔开
        paramMap.put("da", telephone);
        paramMap.put("sm", sm);
        String result = HttpUtil.sendPost(SmsCLUtil.APIURL,paramMap);
        JSONObject jsonObject = JSON.parseObject(result);
        if(jsonObject==null || !jsonObject.getBoolean("success")){
            code = "0";
        }
        return code;
    }

    public String getRandomNumberByNum(int num) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            long randomNum = Math.round(Math.floor(Math.random() * 10.0D));
            sb.append(randomNum);
        }
        return sb.toString();
    }


//    /***
//     * 测试地址
//     * @throws Exception
//     *
//     * ***/
//    public static void main(String[] args) throws Exception {
//        SMSUtils smsUtils = new SMSUtils();
//        String message = "您好，您的验证码是"+smsUtils.getRandomNumberByNum(6)+"，30分钟有效，转发无效。";
//        System.out.println(message);
//        String phone = "18357005066";
//        String r = new SMSUtils().sendSms(phone, message);
//        System.out.println(r);
//    }

}
