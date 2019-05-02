package com.info.config;

import com.info.web.util.PropertiesLoader;

/**
 * 小鱼儿对接地址常量
 */
public class PayContents {
	private static PropertiesLoader propertiesLoader = new PropertiesLoader("config.properties");
    public static final String XJX_REIMBURSEMENT_STATUS_URL = propertiesLoader.getProperty("XJX.reimbursementstatus.url");	//小鱼儿复查订单状态
    public static final String XJX_PUSH_STATUS_URL = propertiesLoader.getProperty("XJX.push.status_url");	//触发小鱼儿推送接口
    public static final String XJX_WITHHOLDING_NOTIFY_KEY=propertiesLoader.getProperty("XJX.withholding.key");	//触发小鱼儿代扣KEY
    public static final String XJX_WITHHOLDING_NOTIFY_URL = propertiesLoader.getProperty("XJX.withholding.url");	//触发小鱼儿代扣
    public static final String XJX_DOMAINNAME_URL=propertiesLoader.getProperty("XJX.domainname.url");	//小鱼儿域名
    public static final String COLLECTION_ADVISE_UPDATE_URL=propertiesLoader.getProperty("collection_advise_update_url");//小鱼儿风控催收建议推送地址
    public static final String XJX_JIANMIAN_URL=propertiesLoader.getProperty("XJX.jianmian.url");//小鱼儿减免推送地址

    public static final String ZZC_USERNAME = propertiesLoader.getProperty("zzc.username");
    public static final String ZZC_PASSWORD = propertiesLoader.getProperty("zzc.password");
    public static final String ZZC_PUSHBLACKLIST_URL = propertiesLoader.getProperty("zzc.pushblacklist.url");

    public static final String APP_NAME = propertiesLoader.getProperty("APP_NAME");
    public static final String MQ_TOPIC = propertiesLoader.getProperty("MQ_TOPIC");
    public static final String MQ_CALLBACK_URL = propertiesLoader.getProperty("MQ_CALLBACK_URL");

}
