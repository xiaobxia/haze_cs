package com.info.back.service;

import com.info.back.dao.IPaginationDao;
import com.info.back.dao.ITemplateSmsDao;
import com.info.back.result.JsonResult;
import com.info.back.utils.IdGen;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.TemplateSms;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class TemplateSmsServiceImpl implements ITemplateSmsService {


    @Resource
    private IPaginationDao<TemplateSms> paginationDao;

    @Resource
    private ITemplateSmsDao templateSmsDao;

    @Override
    public PageConfig<TemplateSms> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "TemplateSms");
        return paginationDao.findPage("findAll", "findAllCount", params, null);
    }

    @Override
    public TemplateSms getTemplateSmsById(String id) {
        return templateSmsDao.getTemplateSmsById(id);
    }

    @Override
    public JsonResult updateTemplateSms(TemplateSms templateSms) {
        JsonResult result = new JsonResult("-1", "修改短信模板失败");
        try {
            if (templateSmsDao.update(templateSms) > 0) {
                result.setCode("0");
                result.setMsg("修改短信模板成功");
            }
        } catch (Exception e) {
            log.error("TemplateSmsService updateById", e);
        }
        return result;
    }

    @Override
    public JsonResult insert(TemplateSms templateSms) {
        JsonResult result = new JsonResult("-1", "添加短信模板失败");
        templateSms.setId(IdGen.uuid());
        try {
            if (templateSmsDao.insert(templateSms) > 0) {
                result.setCode("0");
                result.setMsg("添加短信模板成功");
            }
        } catch (Exception e) {
            log.error("TemplateSmsService insert", e);
        }
        return result;
    }

    @Override
    public JsonResult deleteTemplateSms(String id) {
        JsonResult result = new JsonResult("-1", "删除短信模板失败");
        try {
            TemplateSms templateSms = templateSmsDao.getTemplateSmsById(id);
            if (templateSms != null) {
                if (templateSmsDao.delete(id) > 0) {
                    result.setCode("0");
                    result.setMsg("删除短信模板成功");
                }
            } else {
                result.setCode("-1");
                result.setMsg("未找到此信息模板");
            }
        } catch (Exception e) {
            log.error("TemplateSmsService delete", e);
        }
        return result;
    }

    /**
     * 分組查詢
     */
    @Override
    public List<TemplateSms> getType(HashMap<String, Object> params) {

        return templateSmsDao.getType(params);
    }

}
