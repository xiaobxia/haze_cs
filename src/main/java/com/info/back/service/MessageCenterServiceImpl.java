package com.info.back.service;

import com.info.back.dao.IMessageCenterDao;
import com.info.back.dao.IPaginationDao;
import com.info.back.sms.SmsUtil;
import com.info.back.smtp.MailSendTool;
import com.info.back.utils.RequestUtils;
import com.info.back.utils.Result;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackMessageCenter;
import com.info.web.pojo.cspojo.BackNotice;
import com.info.web.pojo.cspojo.User;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class MessageCenterServiceImpl implements IMessageCenterService {

    @Resource
    private IMessageCenterDao backMessageCenterDao;
    @Resource
    private IBackNoticeService backNoticeService;
    @Resource
    private IPaginationDao paginationDao;
    @Resource
    private IBackInfoUserService backZbUserService;

    @Override
    public int delete(HashMap<String, Object> params) {
        return backMessageCenterDao.delete(params);
    }

    @Override
    public BackMessageCenter findById(Integer id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<BackMessageCenter> list = backMessageCenterDao.findParams(params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public PageConfig<BackMessageCenter> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "MessageCenter");
        return paginationDao.findPage("findParams", "findParamsCount", params,
                null);
    }

    @Override
    public boolean send(BackMessageCenter center, String noticeCode) {
        boolean flag = false;
        try {
            if (center.getSendUserId() == null) {
                center.setSendUserId(Constant.ADMINISTRATOR_ID);
            }
            if (StringUtils.isBlank(center.getMessageSendIp())) {
                center.setMessageSendIp(RequestUtils.getIpAddr());
            }
            User receiveUser = null;
            if (center.getReceiveUser() != null) {
                receiveUser = center.getReceiveUser();
            } else {
                receiveUser = backZbUserService.findById(center
                        .getReceiveUserId());
                center.setReceiveUser(receiveUser);
            }
            String code = noticeCode.toLowerCase();
            BackNotice notice = backNoticeService.findByCode(code);
            if (notice != null) {
                if (BackNotice.EMAIL_REQUIRED_SELECTED.intValue() == notice
                        .getEmail().intValue()) {
                    try {
                        this.sendEmail(center, code);
                    } catch (Exception e) {
                        log.error("sendEmail error center=" + center
                                + ",code=" + code, e);
                    }
                }
                if (BackNotice.MESSAGE_REQUIRED_SELECTED.intValue() == notice
                        .getMessage().intValue()) {
                    try {
                        this.sendMessage(center, code);
                    } catch (Exception e) {
                        log.error("sendMessage error center=" + center
                                + ",code=" + code, e);
                    }
                }
                if (BackNotice.PHONE_REQUIRED_SELECTED.intValue() == notice
                        .getPhone().intValue()) {
                    try {
                        this.sendSms(center, code);
                    } catch (Exception e) {
                        log.error("sendSms error center=" + center
                                + ",code=" + code, e);
                    }
                }
            }
            flag = true;
        } catch (Exception e) {
            log.error("send error:" + noticeCode + ";" + center, e);
        }
        return flag;
    }

    @Override
    public boolean sendEmail(BackMessageCenter center, String noticeCode) {
        User receiveUser = null;
        if (center.getReceiveUser() != null) {
            receiveUser = center.getReceiveUser();
        } else {
            receiveUser = backZbUserService.findById(center.getReceiveUserId());
        }
        if (center.getSendUserId() == null) {
            center.setSendUserId(Constant.ADMINISTRATOR_ID);
        }
        center.setMessageAddress(receiveUser.getUserEmail());
        Result result = MailSendTool.getInstance().sendEmail(
                center.getMessageTitle(), center.getMessageContent(),
                center.getMessageAddress());
        if (result.isSuccessed()) {
            center.setMessageStatus(BackMessageCenter.STATUS_SUCCESS);
        } else {
            center.setMessageStatus(BackMessageCenter.STATUS_FAILD);
        }
        center.setNoticeTypeId(BackMessageCenter.EMAIL);
        backMessageCenterDao.insert(center);
        return true;
    }

    public boolean sendMessage(BackMessageCenter center, String noticeCode) {
        User receiveUser = null;
        if (center.getReceiveUser() != null) {
            receiveUser = center.getReceiveUser();
        } else {
            receiveUser = backZbUserService.findById(center.getReceiveUserId());
        }
        center.setMessageAddress(receiveUser.getUserAccount());
        center.setMessageStatus(BackMessageCenter.STATUS_UNREAD);
        center.setNoticeTypeId(BackMessageCenter.MESSAGE);
        backMessageCenterDao.insert(center);
        return true;

    }

    public boolean sendSms(BackMessageCenter center, String noticeCode) {
        User receiveUser = null;
        if (center.getReceiveUser() != null) {
            receiveUser = center.getReceiveUser();
        } else {
            receiveUser = backZbUserService.findById(center.getReceiveUserId());
        }
        center.setMessageAddress(receiveUser.getUserTelephone());
        int ret = SmsUtil.sendSms(center.getMessageAddress(), center
                .getMessageContent(), Constant.NOTICE, null, null);// 发送短信
        // 发送成功为0，其他都为失败
        if (ret == 0) {
            center.setMessageStatus(BackMessageCenter.STATUS_SUCCESS);
        } else {
            center.setMessageStatus(BackMessageCenter.STATUS_FAILD);
        }
        center.setNoticeTypeId(BackMessageCenter.SMS);
        backMessageCenterDao.insert(center);
        return true;
    }

    @Override
    public int update(BackMessageCenter backMessageCenter) {
        return backMessageCenterDao.update(backMessageCenter);
    }

    @Override
    public int findParamsCount(HashMap<String, Object> params) {
        return backMessageCenterDao.findParamsCount(params);
    }

}
