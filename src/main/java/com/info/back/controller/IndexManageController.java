package com.info.back.controller;

import com.info.back.annotation.ExcludeUrl;
import com.info.back.service.IBackIndexService;
import com.info.back.utils.DwzResult;
import com.info.back.utils.Result;
import com.info.back.utils.SpringUtils;
import com.info.web.pojo.cspojo.BackUser;
import com.info.web.pojo.cspojo.IndexPage;
import com.info.web.pojo.index.Banner;
import com.info.web.pojo.index.Cv;
import com.info.web.pojo.index.Link;
import com.info.web.pojo.index.Notice;
import com.info.web.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述： 前台首页维护 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-7-1 下午03:10:38 <br>
 */
@Slf4j
@Controller
@RequestMapping("indexManage/")
public class IndexManageController extends BaseController {
    @Resource
    private IBackIndexService backIndexService;

    /**
     * 跳转到首页某个部分的管理页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("findList")
    public String findList(HttpServletRequest request,
                           HttpServletResponse response, Model model) {
        String url = null;
        String errorMsg = null;
        HashMap<String, Object> params = this.getParametersO(request);
        try {
            if (params.containsKey("type")) {
                String type = String.valueOf(params.get("type"));
                if (StringUtils.isNotBlank(type)) {
                    IndexPage index = this.backIndexService.searchIndex();// 查询数据库
                    if ("banner".equals(type)) {
                        List<Banner> bannerList = JsonUtil.jsonArrayToBean(
                                index.getIndexBanner(), Banner.class);// bannerList
                        model.addAttribute("bannerList", bannerList);
                        url = "index/bannerList";
                    } else if ("notice".equals(type)) {
                        List<Notice> noticeList = JsonUtil.jsonArrayToBean(
                                index.getIndexNotice(), Notice.class);// 公告
                        model.addAttribute("noticeString", index
                                .getIndexNotice().replaceAll("\"", "\'"));
                        model.addAttribute("noticeList", noticeList);
                        url = "index/noticeList";
                    } else if ("cv".equals(type)) {
                        Cv cv = JsonUtil.jsonToBean(index.getIndexCv(),
                                Cv.class);// 公司简介
                        model.addAttribute("cv", cv);
                        url = "index/saveUpdateCv";
                    } else if ("updateCv".equals(type)) {

                    } else if ("link".equals(type)) {
                        List<Link> linkList = JsonUtil.jsonArrayToBean(index
                                .getIndexLink(), Link.class);// 友情链接
                        model.addAttribute("linkString", index.getIndexLink()
                                .replaceAll("\"", "\'"));
                        model.addAttribute("linkList", linkList);
                        url = "index/linkList";
                    } else if ("".equals(type)) {

                    } else if ("".equals(type)) {

                    }
                } else {
                    errorMsg = "参数非法！";
                }
            } else {
                errorMsg = "参数非法！";
            }
        } catch (Exception e) {
            log.error("findList error ", e);
        }
        model.addAttribute("params", params);
        model.addAttribute(MESSAGE, errorMsg);
        return url;
    }

    /**
     * 保存banner
     *
     * @param request
     * @param response
     * @param model
     * @param banner
     * @return
     */
    @RequestMapping("saveUpdateBanner")
    public String saveUpdateBanner(HttpServletRequest request,
                                   HttpServletResponse response, Model model, Banner banner) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                url = "index/saveUpdateBanner";
            } else {
                IndexPage index = this.backIndexService.searchIndex();// 查询数据库
                List<Banner> list = JsonUtil.jsonArrayToBean(index
                        .getIndexBanner(), Banner.class);
                list.add(banner);
                IndexPage zbIndex = new IndexPage();
                zbIndex.setIndexBanner(JsonUtil.beanListToJson(list));
                backIndexService.update(zbIndex);
                SpringUtils.renderDwzResult(response, true, "操作成功",
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }

        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            log.error("saveUpdateBanner error ", e);
        }
        model.addAttribute("params", params);
        model.addAttribute(MESSAGE, erroMsg);
        return url;
    }

    /**
     * 删除banner
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("deleteBanner")
    public void deleteBanner(HttpServletRequest request,
                             HttpServletResponse response, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String erroMsg = null;
        boolean flag = false;
        try {
            IndexPage zindex = this.backIndexService.searchIndex();// 查询数据库
            List<Banner> list = JsonUtil.jsonArrayToBean(zindex
                    .getIndexBanner(), Banner.class);
            list.remove(Integer.valueOf(String.valueOf(params.get("id")))
                    .intValue());
            IndexPage zbIndex = new IndexPage();
            zbIndex.setIndexBanner(JsonUtil.beanListToJson(list));
            backIndexService.update(zbIndex);
            flag = true;

        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            log.error("deleteBanner error ", e);
        }
        SpringUtils.renderDwzResult(response, flag, flag ? "操作成功" : "操作失败，"
                + erroMsg, DwzResult.CALLBACK_CLOSECURRENTDIALOG, params.get(
                "parentId").toString());

    }

    /**
     * 保存公告
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("saveNotice")
    public String saveNotice(HttpServletRequest request,
                             HttpServletResponse response, Model model, Notice notice) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                if (params.get("noticeString") != null) {
                    model.addAttribute("noticeString", params
                            .get("noticeString"));
                }
                params.put("url", "saveNotice");
                url = "index/saveUpdateNotice";
            } else {
                IndexPage zindex = this.backIndexService.searchIndex();// 查询数据库
                List<Notice> list = JsonUtil.jsonArrayToBean(zindex
                        .getIndexNotice(), Notice.class);
                list.add(notice);
                IndexPage zbIndex = new IndexPage();
                zbIndex.setIndexNotice(JsonUtil.beanListToJson(list));
                backIndexService.update(zbIndex);
                SpringUtils.renderDwzResult(response, true, "操作成功",
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }

        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            log.error("saveNotice error ", e);
        }
        model.addAttribute("params", params);
        model.addAttribute(MESSAGE, erroMsg);
        return url;
    }

    /**
     * 更新公告
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("updateNotice")
    public String updateNotice(HttpServletRequest request,
                               HttpServletResponse response, Model model, Notice notice) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                IndexPage zindex = this.backIndexService.searchIndex();// 查询数据库
                List<Notice> list = JsonUtil.jsonArrayToBean(zindex
                        .getIndexNotice(), Notice.class);
                model.addAttribute("notice", list.get(Integer.valueOf(String
                        .valueOf(params.get("id")))));
                params.put("url", "updateNotice");
                url = "index/saveUpdateNotice";
            } else {
                IndexPage zindex = this.backIndexService.searchIndex();// 查询数据库
                List<Notice> list = JsonUtil.jsonArrayToBean(zindex
                        .getIndexNotice(), Notice.class);
                int index = Integer.valueOf(String.valueOf(params.get("id")));
                list.set(index, notice);
                IndexPage zbIndex = new IndexPage();
                zbIndex.setIndexNotice(JsonUtil.beanListToJson(list));
                backIndexService.update(zbIndex);

                SpringUtils.renderDwzResult(response, true, "操作成功",
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }

        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            log.error("updateNotice error ", e);
        }
        model.addAttribute("params", params);
        model.addAttribute(MESSAGE, erroMsg);
        return url;
    }

    /**
     * 删除公告
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("deleteNotice")
    public void deleteNotice(HttpServletRequest request,
                             HttpServletResponse response, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String erroMsg = null;
        boolean flag = false;
        try {
            IndexPage zindex = this.backIndexService.searchIndex();// 查询数据库
            List<Notice> list = JsonUtil.jsonArrayToBean(zindex
                    .getIndexNotice(), Notice.class);
            int index = Integer.valueOf(String.valueOf(params.get("id")));
            list.remove(index);
            IndexPage zbIndex = new IndexPage();
            zbIndex.setIndexNotice(JsonUtil.beanListToJson(list));
            backIndexService.update(zbIndex);
            flag = true;

        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            log.error("deleteNotice error ", e);
        }
        SpringUtils.renderDwzResult(response, flag, flag ? "操作成功" : "操作失败，"
                + erroMsg, DwzResult.CALLBACK_CLOSECURRENTDIALOG, params.get(
                "parentId").toString());

    }

    /**
     * 保存更新公司简介
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @ExcludeUrl
    @RequestMapping("saveUpdateCv")
    public void saveUpdateCv(HttpServletRequest request,
                             HttpServletResponse response, Model model, Cv cv) {
        HashMap<String, Object> params = this.getParametersO(request);
        String erroMsg = null;
        String code = "500";
        try {
            BackUser backUser = loginAdminUser(request);
            if (backUser != null) {
                IndexPage zbIndex = new IndexPage();
                zbIndex.setIndexCv(JsonUtil.beanToJson(cv));
                backIndexService.update(zbIndex);
                code = Result.SUCCESS;
                erroMsg = "操作成功！";
            } else {
                erroMsg = "请登录后操作！";
            }
        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            log.error("saveUpdateCv error ", e);
        }
        model.addAttribute("params", params);
        model.addAttribute(MESSAGE, erroMsg);
        SpringUtils.renderJson(response, new DwzResult(code, erroMsg));
    }

    /**
     * 保存友情链接
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("saveUpdateLink")
    public String saveUpdateLink(HttpServletRequest request,
                                 HttpServletResponse response, Model model, Link link) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                url = "index/saveUpdateLink";
            } else {
                IndexPage index = this.backIndexService.searchIndex();// 查询数据库
                List<Link> list = JsonUtil.jsonArrayToBean(
                        index.getIndexLink(), Link.class);
                list.add(link);
                IndexPage zbIndex = new IndexPage();
                zbIndex.setIndexLink(JsonUtil.beanListToJson(list));
                backIndexService.update(zbIndex);
                SpringUtils.renderDwzResult(response, true, "操作成功",
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }

        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            log.error("saveUpdateLink error ", e);
        }
        model.addAttribute("params", params);
        model.addAttribute(MESSAGE, erroMsg);
        return url;
    }

    /**
     * 删除banner
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("deleteLink")
    public void deleteLink(HttpServletRequest request,
                           HttpServletResponse response, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String erroMsg = null;
        boolean flag = false;
        try {
            IndexPage zindex = this.backIndexService.searchIndex();// 查询数据库
            List<Link> list = JsonUtil.jsonArrayToBean(zindex.getIndexLink(),
                    Link.class);
            list.remove(Integer.valueOf(String.valueOf(params.get("id")))
                    .intValue());
            IndexPage zbIndex = new IndexPage();
            zbIndex.setIndexLink(JsonUtil.beanListToJson(list));
            backIndexService.update(zbIndex);
            flag = true;

        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            log.error("deleteBanner error ", e);
        }
        SpringUtils.renderDwzResult(response, flag, flag ? "操作成功" : "操作失败，"
                + erroMsg, DwzResult.CALLBACK_CLOSECURRENTDIALOG, params.get(
                "parentId").toString());

    }
}
