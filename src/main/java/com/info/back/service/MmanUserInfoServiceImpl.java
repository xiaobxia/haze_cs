package com.info.back.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.info.back.dao.IMmanUserInfoDao;
import com.info.back.result.MonthUseDetail;
import com.info.back.result.TDUserCallInfo;
import com.info.back.result.TDUserCallInfoDetail;
import com.info.web.pojo.cspojo.ContactInfo;
import com.info.web.pojo.cspojo.MmanUserInfo;
import com.info.web.synchronization.dao.IDataDao;
import com.info.web.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MmanUserInfoServiceImpl implements IMmanUserInfoService {

    @Resource
    private IMmanUserInfoDao mmanUserInfoDao;
    @Resource
    private IDataDao dataDao;

    @Override
    public MmanUserInfo getUserInfoById(String id) {
        return mmanUserInfoDao.get(id);
    }

    @Override
    public MmanUserInfo getxjxuser(Long id) {
        return mmanUserInfoDao.getxjxuser(id);
    }

    @Override
    public int saveNotNull(MmanUserInfo mmanUserInfo) {
        return mmanUserInfoDao.saveNotNull(mmanUserInfo);
    }

    @Override
    public List<ContactInfo> getContactInfo(String phoneNum) {
        return mmanUserInfoDao.getContactInfo(phoneNum);
    }

    @Override
    public Map<String, Object> getTdYysInfo(String tdData, MmanUserInfo userInfo) {
        Map<String, Object> dataResult = new HashMap<>();
        List<MonthUseDetail> serviceResult = new ArrayList<>();
        MonthUseDetail totalCount = new MonthUseDetail();
        totalCount.setMonth("总计");
        int firstConcatTotalCallTime = 0;
        int firstConcatTotalCallCount = 0;
        int secondConcatTotalCallTime = 0;
        int secondConcatTotalCallCount = 0;
        int totalCloseDays = 0;
        int totalNightCallDays = 0;
        int totalCallDays = 0;
        int totalFee = 0;
        int totalCallTime = 0;
        Map<String, Object> baseInfo = new HashMap<>();
        Map<String, Object> yysInfo = new HashMap<>();
        JSONObject pObject = (JSONObject) JSON.parse(tdData);
        JSONObject dataObject = pObject.getJSONObject("data");
        JSONObject taskData = dataObject.getJSONObject("task_data");
        baseInfo.put("name", userInfo.getRealname());
        baseInfo.put("idNumber", userInfo.getIdNumber());
        baseInfo.put("age", userInfo.getUserAge());
        baseInfo.put("gender", userInfo.getUserSex());
        baseInfo.put("taskTime", dataObject.get("created_time"));
        Date taskTime = DateUtil.formatDate(dataObject.get("created_time").toString(), "yyyy-MM-dd");
        baseInfo.put("taskId", pObject.get("task_id"));
        dataResult.put("baseInfo", baseInfo);
        //运营商信息
        String mobileName = taskData.getJSONObject("base_info").get("user_name").toString();
        yysInfo.put("mobileName", mobileName);
        yysInfo.put("mobile", dataObject.get("user_mobile"));
        yysInfo.put("address", userInfo.getPresentAddress());
        yysInfo.put("concatNo", taskData.getJSONObject("base_info").get("user_contact_no"));
        yysInfo.put("email", userInfo.getEmail());
        JSONObject acountInfo = taskData.getJSONObject("account_info");
        JSONArray array = taskData.getJSONArray("bill_info");
        Map<String, String> feeMap = getMonthFee(array);
        yysInfo.put("acountStatus", acountInfo.get("mobile_status"));
        yysInfo.put("acountLevel", acountInfo.get("credit_level"));
        String accountBalance = acountInfo.getString("account_balance");
        yysInfo.put("acountBalance", accountBalance == null ? 0 : Double.parseDouble(accountBalance) / 100);
        String netTimeStr = acountInfo.get("net_time").toString();
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String inNetTimeStr = "未知";
        if (StringUtils.isNotEmpty(netTimeStr) && DateUtil.isValidDate(netTimeStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int inNetTime = 0;
            try {
                inNetTime = DateUtil.daysBetween(sdf.parse(netTimeStr), new Date());
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage(), e.getCause());
            }
            inNetTimeStr = "" + inNetTime;
        }
        yysInfo.put("netTime", inNetTimeStr);
        char isSelfMobile = userInfo.getRealname().equals(mobileName) ? '是' : '否';
        yysInfo.put("isSelf", isSelfMobile);
        dataResult.put("yysInfo", yysInfo);
        //月使用情况
//				Map<String, String> firstConcatLastCall = new HashMap<>();
//				Map<String, Integer> otherCallCount = new HashMap<String, Integer>();
        List<TDUserCallInfo> firstConcatCall = new ArrayList<>();
        List<TDUserCallInfo> secondConcatCall = new ArrayList<>();
        List<TDUserCallInfo> otherConcatCall = new ArrayList<>();
        List<TDUserCallInfo> totalConcatCall = new ArrayList<>();
        List<TDUserCallInfo> originatingCount = new ArrayList<>();
        List<TDUserCallInfo> terminatingCount = new ArrayList<>();
        String firstConcat = userInfo.getFirstContactPhone();
        String secondConcat = userInfo.getSecondContactPhone();
        com.alibaba.fastjson.JSONArray callInfoArr = taskData.getJSONArray("call_info");
        JSONObject callInfoObj;
        com.alibaba.fastjson.JSONArray callRecord;
        MonthUseDetail monthUseDetail;
        Map<String, Integer> rankingMap;
        for (int i = 0; i < callInfoArr.size(); i++) {
            callInfoObj = callInfoArr.getJSONObject(i);
            if (callInfoObj != null) {
                if (callInfoObj.get("total_fee") != null) {
                    totalFee += Integer.parseInt(callInfoObj.get("total_fee").toString());
                }
                if (callInfoObj.get("total_call_time") != null) {
                    totalCallTime += Integer.parseInt(callInfoObj.get("total_call_time").toString());
                }
            }
            String callCycle = callInfoObj.get("call_cycle").toString();
            callRecord = callInfoObj.getJSONArray("call_record");
            JSONObject recordObj;
            TDUserCallInfo tdUserCallInfo;
            Map<String, Integer> groupOthers;
            Map<String, Integer> closeInfo;
            for (int j = 0; j < callRecord.size(); j++) {
                recordObj = callRecord.getJSONObject(j);
                tdUserCallInfo = new TDUserCallInfo(recordObj.get("call_time").toString(), recordObj.get("call_start_time").toString(), recordObj.get("call_other_number").toString(), recordObj.get("call_type_name").toString());
                if ("主叫".equals(recordObj.get("call_type_name"))) {
                    originatingCount.add(tdUserCallInfo);
                } else if ("被叫".equals(recordObj.get("call_type_name"))) {
                    terminatingCount.add(tdUserCallInfo);
                }
                if (firstConcat.equals(recordObj.get("call_other_number"))) {
                    firstConcatCall.add(tdUserCallInfo);
                    firstConcatTotalCallTime += Integer.parseInt(recordObj.get("call_time").toString());
                } else if (secondConcat.equals(recordObj.get("call_other_number"))) {
                    secondConcatCall.add(tdUserCallInfo);
                    firstConcatTotalCallTime += Integer.parseInt(recordObj.get("call_time").toString());
                } else {
                    otherConcatCall.add(tdUserCallInfo);
                }
            }
//					TDUserCallInfoDetail detail = new TDUserCallInfoDetail();
            monthUseDetail = new MonthUseDetail();
            if (CollectionUtils.isNotEmpty(firstConcatCall)) {
                callInfoSort(firstConcatCall);
                monthUseDetail.setFirstConcatLastCallTime(firstConcatCall.get(0).getStartTime());
            } else {
                monthUseDetail.setFirstConcatLastCallTime("无");
            }
            if (CollectionUtils.isNotEmpty(secondConcatCall)) {
                callInfoSort(secondConcatCall);
                monthUseDetail.setSecondConcatLastCallTime(secondConcatCall.get(0).getStartTime());
            } else {
                monthUseDetail.setSecondConcatLastCallTime("无");
            }
            monthUseDetail.setMonth(getMonth(callCycle));
            monthUseDetail.setFirstConcatCallCount(firstConcatCall.size() + "");
            monthUseDetail.setSecondConcatCallCount(secondConcatCall.size() + "");
            groupOthers = groupList(otherConcatCall);
            groupOthers.put(firstConcat, firstConcatCall.size());
            groupOthers.put(secondConcat, secondConcatCall.size());
            rankingMap = mapSortAndGet(groupOthers, firstConcat, secondConcat);
            monthUseDetail.setFirstConcatCallRanking(rankingMap.get(firstConcat).toString());
            monthUseDetail.setSecondConcatCallRanking(rankingMap.get(secondConcat).toString());

            totalConcatCall.addAll(firstConcatCall);
            totalConcatCall.addAll(secondConcatCall);
            totalConcatCall.addAll(otherConcatCall);
            closeInfo = getTelCloseDaysAndSeveralCloseMaxDays(totalConcatCall, callCycle, taskTime);
            monthUseDetail.setTelCloseDays(closeInfo.get("telCloseDays").toString());
            totalCloseDays += closeInfo.get("telCloseDays");
            monthUseDetail.setSeveralCloseMaxDays(closeInfo.get("severalCloseMaxDays").toString());
            monthUseDetail.setCallNumCount(getCallNumSet(totalConcatCall).size() + "");
            monthUseDetail.setCallNightActiveness(getCallNightActiveness(totalConcatCall).get("nightActiveness").toString());
            totalNightCallDays += Integer.parseInt(getCallNightActiveness(totalConcatCall).get("nightCount").toString());
            totalCallDays += totalConcatCall.size();
            monthUseDetail.setTotalFee(feeMap.get(callCycle));
            monthUseDetail.setTotalTime(new Formatter().format("%.2f", (Double.parseDouble(callInfoObj.get("total_call_time").toString()) / (60))).toString());
            serviceResult.add(monthUseDetail);
            firstConcatTotalCallCount += firstConcatCall.size();
            secondConcatTotalCallCount += secondConcatCall.size();
            firstConcatCall.clear();
            secondConcatCall.clear();
            otherConcatCall.clear();
            totalConcatCall.clear();
        }
        totalCount.setFirstConcatLastCallTime(firstConcatTotalCallTime + "");
        totalCount.setSecondConcatLastCallTime(secondConcatTotalCallTime + "");
        totalCount.setFirstConcatCallCount(firstConcatTotalCallCount + "");
        totalCount.setSecondConcatCallCount(secondConcatTotalCallCount + "");
        totalCount.setTelCloseDays(totalCloseDays + "");
        totalCount.setCallNightActiveness(totalNightCallDays + "/" + totalCallDays);
        totalCount.setTotalFee(feeMap.get("totalFee"));
        totalCount.setTotalTime(new Formatter().format("%.2f", (double) (totalCallTime) / (60)).toString());
        serviceResult.add(totalCount);
        dataResult.put("month", serviceResult);
//				通话详情（所有数据）
        Map<String, TDUserCallInfoDetail> data = new LinkedHashMap<>();
        TDUserCallInfoDetail detail;
        Map<String, String> remarkMap = getConcatsRemarkMap(userInfo.getId());
        for (TDUserCallInfo info : originatingCount) {
            detail = data.get(info.getCallOtherNumber());
            if (detail != null) {
                detail.setOriginatingTime((Integer.parseInt(detail.getOriginatingTime()) + Integer.parseInt(info.getCallTime())) + "");
                detail.setOriginatingCount((Integer.parseInt(detail.getOriginatingCount()) + 1) + "");
            } else {
                detail = new TDUserCallInfoDetail();
                detail.setOriginatingCount("1");
                detail.setOriginatingTime(info.getCallTime());
                detail.setTel(info.getCallOtherNumber());
                detail.setTelRemark(remarkMap.get(info.getCallOtherNumber()) == null ? "" : remarkMap.get(info.getCallOtherNumber()));
                detail.setTerminatingCount("0");
                detail.setTerminatingTime("0");
            }
            data.put(info.getCallOtherNumber(), detail);
        }
        TDUserCallInfoDetail detail2;
        for (TDUserCallInfo info : terminatingCount) {
            detail2 = data.get(info.getCallOtherNumber());
            if (detail2 != null) {
                detail2.setTerminatingTime((Integer.parseInt(detail2.getTerminatingTime()) + Integer.parseInt(info.getCallTime())) + "");
                detail2.setTerminatingCount((
                        Integer.parseInt(detail2.getTerminatingCount()) + 1) + "");
            }
            data.put(info.getCallOtherNumber(), detail2);
        }
        List<TDUserCallInfoDetail> dataList = new ArrayList<>();
        TDUserCallInfoDetail tid;
        for (Map.Entry<String, TDUserCallInfoDetail> entry : data.entrySet()) {
            tid = entry.getValue();
            if (tid != null) {
                tid.setTerminatingTime(new Formatter().format("%.2f", Double.parseDouble(tid.getTerminatingTime()) / 60).toString());
                tid.setOriginatingTime(new Formatter().format("%.2f", Double.parseDouble(tid.getOriginatingTime()) / 60).toString());
                dataList.add(entry.getValue());
            }
        }
        sortTDUserCallInfoDetail(dataList);
        dataResult.put("callInfoDetails", dataList);
        return dataResult;
    }

    private Map<String, String> getConcatsRemarkMap(String userId) {
        List<HashMap<String, Object>> userContactsmap;
        HashMap<String, String> concatParam = new HashMap<>();
        concatParam.put("USER_ID", userId);
        userContactsmap = dataDao.getUserContacts(concatParam);
        Map<String, String> resulMap = new HashMap<>(userContactsmap.size());
        userContactsmap.forEach(one -> {
            resulMap.put(one.get("contact_phone") == null ? "" : one.get("contact_phone").toString(), one.get("contact_name") == null ? "" : one.get("contact_name").toString());
        });
        return resulMap;
    }

    private void callInfoSort(List<TDUserCallInfo> list) {
        Collections.sort(list, (o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));
    }

    private void sortTDUserCallInfoDetail(List<TDUserCallInfoDetail> dataList) {
        Collections.sort(dataList, (o1, o2) -> {
            if (Double.parseDouble(o2.getConcatTime()) - Double.parseDouble(o1.getConcatTime()) > 0) {
                return 1;
            }
            return -1;
        });
    }

    private String getMonth(String callCycle) {
        callCycle = callCycle.substring(5);
        if (callCycle.startsWith("0")) {
            callCycle = callCycle.substring(1);
        }
        return callCycle;
    }

    private Map<String, Integer> groupList(List<TDUserCallInfo> list) {
        Map<String, Integer> otherCallCount = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i).getCallOtherNumber();
            if (otherCallCount.containsKey(key)) {
                otherCallCount.put(key, otherCallCount.get(key) + 1);
            } else {
                otherCallCount.put(key, 1);
            }
        }
        return otherCallCount;
    }

    private Map<String, Integer> mapSortAndGet(Map<String, Integer> map, String firstConcat, String secondConcat) {
        List<Map.Entry<String, Integer>> desList = new LinkedList<>(map.entrySet());
        Collections.sort(desList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        map.clear();
        for (int i = 0; i < desList.size(); i++) {
            if (firstConcat.equals(desList.get(i).getKey())) {
                map.put(firstConcat, i + 1);
            } else if (secondConcat.equals(desList.get(i).getKey())) {
                map.put(secondConcat, i + 1);
            }
        }
        return map;
    }

    private Map<String, Integer> getTelCloseDaysAndSeveralCloseMaxDays(List<TDUserCallInfo> paramList, String month, Date taskTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(taskTime);
        Map<String, Integer> result = new HashMap<>();
        int severalCloseMaxDays = 0;
        Set<String> record = getCallTimeSet(paramList);
        List<String> monthAllDay = monthAllDay(month, taskTime);
        List<Integer> maxDaysList = new ArrayList<>();
        for (String s : record) {
            if (monthAllDay.contains(s)) {
                monthAllDay.remove(s);
            }
        }
        result.put("telCloseDays", monthAllDay.size());
        for (int i = 0; i < monthAllDay.size() - 1; i++) {
            Integer days = Integer.parseInt(DateUtil.getDateFormat(monthAllDay.get(i), monthAllDay.get(i + 1), "yyyy-MM-dd"));
            if (days.intValue() == 1) {
                severalCloseMaxDays++;
                if (i == monthAllDay.size()) {
                    maxDaysList.add(severalCloseMaxDays);
                }
            } else {
                maxDaysList.add(severalCloseMaxDays + 1);
                severalCloseMaxDays = 0;
            }
        }
        Collections.sort(maxDaysList);
        if (maxDaysList.size() > 0) {
            result.put("severalCloseMaxDays", maxDaysList.get(maxDaysList.size() - 1));
        } else {
            result.put("severalCloseMaxDays", 0);

        }
        return result;
    }

    //获取指定月份的天数
    private int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    private List<String> monthAllDay(String month, Date taskTime) {
        Date dest = DateUtil.formatDate(month, "yyyy-MM");
        List<String> allDays = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        Calendar taskCal = Calendar.getInstance();
        taskCal.setTime(taskTime);
        //month 为指定月份任意日期
        cal.setTime(dest);
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int m2 = taskCal.get(Calendar.MONTH);
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        // 从一号开始
        cal.set(Calendar.DAY_OF_MONTH, 1);
        if (m == taskCal.get(Calendar.MONTH)) {
            dayNumOfMonth = taskCal.get(Calendar.DAY_OF_MONTH);
        }
        for (int i = 0; i < dayNumOfMonth - 1; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            allDays.add(DateUtil.getDateFormat(d, "yyyy-MM-dd"));
        }
        return allDays;
    }

    private Set<String> getCallTimeSet(List<TDUserCallInfo> paramList) {
        Set<String> result = new LinkedHashSet<>();
        for (int i = 0; i < paramList.size(); i++) {
            String days = DateUtil.getDateFormat(DateUtil.formatDate(paramList.get(i).getStartTime(), "yyyy-MM-dd"), "yyyy-MM-dd");
            result.add(days);
        }
        return result;
    }

    private Set<String> getCallNumSet(List<TDUserCallInfo> paramList) {
        Set<String> result = new LinkedHashSet<>();
        for (TDUserCallInfo aParamList : paramList) {
            result.add(aParamList.getCallOtherNumber());
        }
        return result;
    }

    private Map<String, Object> getCallNightActiveness(List<TDUserCallInfo> paramList) {
        Map<String, Object> result = new HashMap<>();
        int nightCount = 0;
        Date callTime;
        Calendar calendar = Calendar.getInstance();
        for (TDUserCallInfo info : paramList) {
            callTime = DateUtil.getDateTimeFormat(info.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            calendar.setTime(callTime);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (21 <= hour || 6 >= hour) {
                nightCount++;
            }
        }
        String nightActiveness = nightCount + "/" + paramList.size();
        result.put("nightActiveness", nightActiveness);
        result.put("nightCount", nightCount);
        return result;
    }

    private Map<String, String> getMonthFee(JSONArray array) {
        Map<String, String> result = new HashMap<>();
        double totalFee = 0;
        JSONObject object;
        for (int i = 0; i < array.size(); i++) {
            object = array.getJSONObject(i);
            if (object != null) {
                String billFee = object.getString("bill_fee");
                if (StringUtils.isEmpty(billFee)) {
                    result.put(object.getString("bill_cycle"), "未知");
                    continue;
                }
                double billFeed = Double.parseDouble(billFee);
                totalFee += billFeed;
            }
        }
        result.put("totalFee", totalFee / 100 + "");
        return result;
    }

    @Override
    public int getUserCount(Map<String, Object> map) {
        return mmanUserInfoDao.getUserCount(map);
    }

    @Override
    public List<Integer> getUserIdList(Map<String, Object> map) {
        return mmanUserInfoDao.getUserIds(map);
    }

    @Override
    public void addUserFrom(Map<Integer, String> dataMap) {
        mmanUserInfoDao.updateUserFrom(dataMap);
    }
}
