package com.info.back.utils;

import com.info.web.pojo.cspojo.SysDict;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BackConstant {

	public final static String ON = "1";// 开关-开

	public final static String OFF = "0";// 开关-关

	public static final String CREDITLOANAPPLY_OVERDUE = "4";// 逾期

	public static final Map<String,String> GROUP_NAME_MAP = new LinkedHashMap<>();

	public static final Map<String,String> GROUP_NAME_TYPE_MAP = new LinkedHashMap<>();

	public static final Map<String,String> GROUP_STATUS_MAP = new HashMap<>();//催收员状态

	public static final Map<String,String> TYPE_MAP = new LinkedHashMap<>(); // 分期类型

	public static final Map<String,String> TYPE_NAME_MAP = new LinkedHashMap<>(); // 分期名称

	public final static String XJX_JXL_STATUS_REFUSE = "0";// 小鱼儿聚信立通讯录申请审核状态 拒绝

	public final static String XJX_OVERDUE_LEVEL_S1 = "3";// 小鱼儿逾期等级或催收组S1

	public final static String XJX_OVERDUE_LEVEL_S2 = "4";// 小鱼儿逾期等级或催收组S2

	public final static String XJX_OVERDUE_LEVEL_M1_M2 = "5";// 小鱼儿逾期等级或催收组M1_M2

	public final static String XJX_OVERDUE_LEVEL_M2_M3 = "6";// 小鱼儿逾期等级或催收组M2_M3

	public final static String XJX_OVERDUE_LEVEL_M3P = "7";// 小鱼儿逾期等级或催收组M3+

	public final static String XJX_OVERDUE_LEVEL_S3 = "8";// 小鱼儿逾期等级或催收组M3+

	public final static Integer COLLECTION_ROLE_ID=10021;//催收员的角色ID

	public final static Integer OUTSOURCE_MANAGER_ROLE_ID=10022;//委外经理角色ID
	
	public final static String XJX_COLLECTION_ORDER_STATE_SUCCESS = "4";// 小鱼儿催收状态催收成功
	
	public final static String XJX_COLLECTION_ORDER_STATE_PAYING = "5";// 小鱼儿催收状态续期（续期中不管之前怎样现在不能操作）
	
	public final static String XJX_COLLECTION_ORDER_STATE_ING = "1";// 小鱼儿催收状态催收中
	
	public final static String XJX_COLLECTION_ORDER_STATE_PROMISE = "2";// 小鱼儿催收状态承诺还款
	public final static String XJX_COLLECTION_ORDER_STATE_OUTSIDE = "3";// 小鱼儿催收状态委外中
	
	public final static String XJX_COLLECTION_ORDER_STATE_WAIT = "0";// 小鱼儿催收状态待催收

	public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_IN = "1";// 小鱼儿催收状态流转类型入催
	
	public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_CONVERT = "2";// 小鱼儿催收状态流转类型逾期等级转换
	
	public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_OTHER = "3";// 小鱼儿催收状态流转类型转单
	
	public final static String XJX_COLLECTION_STATUS_MOVE_TYPE_OUTSIDE = "4";// 小鱼儿催收状态流转类型委外

	/**
	 * 留单状态 true
	 */
	public final static String SAVE = "Y";
	/**
	 * 留单状态 false
	 */
	public final static String NOT_SAVE = "N";




	static{
		GROUP_NAME_MAP.put(BackConstant.XJX_OVERDUE_LEVEL_S1,"S1");
		GROUP_NAME_MAP.put(BackConstant.XJX_OVERDUE_LEVEL_S2,"S2");
		GROUP_NAME_MAP.put(BackConstant.XJX_OVERDUE_LEVEL_M1_M2,"M1-M2");
		GROUP_NAME_MAP.put(BackConstant.XJX_OVERDUE_LEVEL_M2_M3,"M2-M3");
		GROUP_NAME_MAP.put(BackConstant.XJX_OVERDUE_LEVEL_M3P,"M3+");
		GROUP_NAME_MAP.put(BackConstant.XJX_OVERDUE_LEVEL_S3,"S3");

		GROUP_NAME_TYPE_MAP.put("33","S1");
		GROUP_NAME_TYPE_MAP.put("43","S2-S1");
		GROUP_NAME_TYPE_MAP.put("44","S2-S2");
		GROUP_NAME_TYPE_MAP.put("55","M1-M2");
		GROUP_NAME_TYPE_MAP.put("66","M2-M3");
		GROUP_NAME_TYPE_MAP.put("77","M3+");
		GROUP_NAME_TYPE_MAP.put("88","S3");

		GROUP_STATUS_MAP.put("0", "禁用");
		GROUP_STATUS_MAP.put("1", "启用");
		GROUP_STATUS_MAP.put("3", "删除");

		TYPE_MAP.put("2","二期还款");
		TYPE_MAP.put("3","三期还款");
		TYPE_MAP.put("4","四期还款");

		TYPE_NAME_MAP.put("1","一期");
		TYPE_NAME_MAP.put("2","二期");
		TYPE_NAME_MAP.put("3","三期");
		TYPE_NAME_MAP.put("4","四期");
	}
	
	public final static String XJX_COLLECTION_ORDER_DELETED = "0";// 派单状态-无效
	
	/**
	 * 获取字典Map
	 * @return
	 */
	public static HashMap<String, String> orderState(List<SysDict> dictList){
		HashMap<String, String> dictMap=new HashMap<>();
		for(SysDict dict:dictList){
			dictMap.put(dict.getValue(), dict.getLabel());
		}
		return dictMap;
	}
	
}
