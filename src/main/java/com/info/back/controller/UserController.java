package com.info.back.controller;

import com.info.back.annotation.ExcludeUrl;
import com.info.back.service.IBackRoleService;
import com.info.back.service.IBackUserService;
import com.info.back.service.ICollectionCompanyService;
import com.info.back.utils.DwzResult;
import com.info.back.utils.IdGen;
import com.info.back.utils.SpringUtils;
import com.info.back.utils.TreeUtil;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackTree;
import com.info.web.pojo.cspojo.BackUser;
import com.info.web.pojo.cspojo.BackUserCompanyPermissions;
import com.info.web.pojo.cspojo.MmanLoanCollectionCompany;
import com.info.web.util.PageConfig;
import com.info.web.util.encrypt.Md5coding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("user/")
public class UserController extends BaseController {

    @Resource
    private IBackUserService backUserService;
    @Resource
    private IBackRoleService backRoleService;
    @Resource
    private ICollectionCompanyService collectionCompanyService;

    @RequestMapping("getUserPage")
    public String getUserPage(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = getParametersO(request);
            params.put("noAdmin", Constant.ADMINISTRATOR_ID);
//			params.put("roleId", BackConstant.COLLECTION_ROLE_ID);
            PageConfig<BackUser> pageConfig = backUserService.findPage(params);
            model.addAttribute("pm", pageConfig);
            model.addAttribute("params", params);

        } catch (Exception e) {
            log.error("getUserPage error", e);
        }
        return "user/userList";
    }


    /**
     * xiangxiaoyan 跳转到添加页面
     */
    @RequestMapping("saveUpdateUser")
    public String saveUpdateUser(HttpServletRequest request,
                                 HttpServletResponse response, Model model, BackUser backUser) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                List<BackUserCompanyPermissions> companypermissionslist = null;
                if (params.containsKey("id")) {
                    // 更新的页面跳转
                    backUser = backUserService.findOneUser(params);
                    companypermissionslist = backUserService.findCompanyPermissions(Integer.parseInt(params.get("id").toString()));
                    model.addAttribute("backUser", backUser);
                }
                List<MmanLoanCollectionCompany> companyList = collectionCompanyService.selectCompanyList();
                String outGroupHtml = TreeUtil.getCompanyStringWidthCheckBoxOne(companyList, companypermissionslist, "dataComapnyIDs");
                model.addAttribute("outGroupHtml", outGroupHtml);
                model.addAttribute("companyList", companyList);
                url = "user/saveUpdateUser";
            } else {
                String[] dataComapnyIDs = request.getParameterValues("dataComapnyIDs");
                backUser.setDataComapnyIDs(dataComapnyIDs);
                // 更新或者添加操作
                if (backUser.getId() != null) {
                    backUserService.updateById(backUser);
                } else {
                    backUser.setUserAccount(String.valueOf(params.get("userAccount")).trim());
                    backUser.setAddIp(this.getIpAddr(request));
                    backUser.setUserPassword(Md5coding.getInstance().code(String.valueOf(params.get("userPassword"))));
                    backUser.setUuid(IdGen.uuid());
                    backUserService.insert(backUser);
                }
                SpringUtils.renderDwzResult(response, true, "操作成功",
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }
        } catch (Exception e) {
            erroMsg = "服务器异常，请稍后重试！";
            if (e.getLocalizedMessage().contains("UK_user_account")) {
                erroMsg = "用户名重复！";
            } else if (e.getLocalizedMessage().contains("UK_user_name")) {
                erroMsg = "真实姓名不能重复";
            }
            SpringUtils.renderDwzResult(response, false, "操作失败,原因："
                    + erroMsg, DwzResult.CALLBACK_CLOSECURRENT, params.get(
                    "parentId").toString());
            log.error("添加权限信息失败，异常信息:", e);
        }
        model.addAttribute(MESSAGE, erroMsg);
        model.addAttribute("params", params);
        return url;
    }

    /**
     * 删除角色
     */
    @RequestMapping("deleteUser")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> params = this.getParametersO(request);
        boolean flag = false;
        try {
            Integer id = Integer.parseInt(params.get("id").toString());
            backUserService.deleteById(id);
            flag = true;
        } catch (Exception e) {
            log.error("deleteRole error", e);
        }
        SpringUtils.renderDwzResult(response, flag, flag ? "删除用户成功" : "删除用户失败",
                DwzResult.CALLBACK_CLOSECURRENTDIALOG, params.get("parentId")
                        .toString());
    }

    /**
     * 用戶授权
     */

    @RequestMapping("saveUserRole")
    public String saveUserRole(HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        try {
            BackUser backUser = this.loginAdminUser(request);
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                params.put("userId", backUser.getId());
                List<BackTree> userAll = backRoleService.findRoleTree(params);
                Integer id = Integer.valueOf(params.get("id").toString());
                params.put("userId", id);
                List<BackTree> haveList = backRoleService.findRoleTree(params);
                String outGroupHtml = TreeUtil.getTreeStringWidthCheckBoxOne(
                        userAll, haveList, "roleIds");
                model.addAttribute("outGroupHtml", outGroupHtml);
                params.put("url", "user/saveUserRole");
                params.put("ajaxType", "dialogAjaxDone");
                url = "showRolesRight";
            } else {
                String[] roleIds = request.getParameterValues("roleIds");
//                if (roleIds.length > 1) {
//                    SpringUtils.renderDwzResult(response, false, "用户授权失败，一个用户不能设置多个角色",
//                            DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId").toString());
//                    return null;
//                }
                url = null;
                params.put("roleIds", roleIds);
                backUserService.addUserRole(params);
                SpringUtils.renderDwzResult(response, true, "用户授权成功",
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }
        } catch (Exception e) {
            SpringUtils.renderDwzResult(response, false, "操作失败",
                    DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                            .toString());
            log.error("用户授权失败，异常信息:", e);
        }
        model.addAttribute("params", params);
        return url;
    }

    /**
     * 修改密码
     */
    @ExcludeUrl
    @RequestMapping("updateUserPassword")
    public String updateUserPassword(HttpServletRequest request,
                                     HttpServletResponse response, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String target = "";
        try {
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                model.addAttribute("id", params.get("id"));
                target = "user/updateUserPwd";
            } else {
                BackUser backUser;
                if (null == params.get("id")) {
                    backUser = this.loginAdminUser(request);
                } else {
                    HashMap<String, Object> paraUserMap = new HashMap<>();
                    paraUserMap.put("id", String.valueOf(params.get("id")));
                    backUser = backUserService.findOneUser(paraUserMap);
                }

                Integer id = null == params.get("id") ? backUser.getId() : Integer.parseInt(params.get("id").toString());
                String newPassword = Md5coding.getInstance().code(
                        params.get("newPassword").toString());
                BackUser backUser2 = new BackUser();
                backUser2.setId(id);
                backUser2.setUserPassword(newPassword);
                backUserService.updatePwdById(backUser2);
                SpringUtils.renderDwzResult(response, true, "操作成功", DwzResult.CALLBACK_CLOSECURRENT);
                target = null;
            }
        } catch (Exception e) {
            log.error("updateUserPassWord error ", e);
        }
        return target;
    }
}
