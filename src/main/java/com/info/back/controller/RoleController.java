package com.info.back.controller;

import com.info.back.service.IBackModuleService;
import com.info.back.service.IBackRoleService;
import com.info.back.utils.DwzResult;
import com.info.back.utils.SpringUtils;
import com.info.back.utils.TreeUtil;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackRole;
import com.info.web.pojo.cspojo.BackTree;
import com.info.web.pojo.cspojo.BackUser;
import com.info.web.util.PageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("role/")
public class RoleController extends BaseController {

    @Resource
    private IBackRoleService backRoleService;
    @Resource
    private IBackModuleService backModuleService;

    /**
     * 根据某个树节点查询子节点，角色管理树
     */
    @RequestMapping("getRoleList")
    public String getRoleList(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String target = "role/listRoleSearch";
        String errorMsg = null;
        try {
            BackUser backUser = loginAdminUser(request);
            params.put("userId", backUser.getId());
            if (!params.containsKey("roleId")) {
                // 得到UserId查询对应的权限
                List<BackTree> treeModels = backRoleService
                        .findRoleTree(params);
                String url = "role/getRoleList?myId=" + params.get("myId")
                        + "&roleId=";
                String roleList;
                roleList = TreeUtil.getTreeModelStrings(url, "roleBox", 0, 0,
                        treeModels);
                roleList = "<a href=" + url + "0"
                        + " target=\"ajax\" rel=\"roleBox\" >根目录</a>"
                        + roleList.replaceAll("<ul></ul>", "");
                model.addAttribute("roleList", roleList);
            } else {
                params.put("parentId", params.get("roleId"));
                PageConfig<BackRole> pageConfig = backRoleService
                        .findPage(params);
                model.addAttribute("pm", pageConfig);
            }
            model.addAttribute("params", params);
        } catch (Exception e) {
            errorMsg = "数据操作出现异常，请稍后再试！";
            log.error("展示角色权限树失败，异常信息:", e);
        }
        model.addAttribute(MESSAGE, errorMsg);
        return target;
    }

    /**
     * 添加或者更新权限
     */
    @RequestMapping("saveUpdateRole")
    public String saveUpdateRole(HttpServletRequest request,
                                 HttpServletResponse response, Model model, BackRole backRole) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            BackUser backUser = loginAdminUser(request);
            Integer userId = backUser.getId();
            params.put("userId", userId);
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                if (params.containsKey("id")) {
                    // 更新的页面跳转
                    backRole = backRoleService.findById(Integer.valueOf(String
                            .valueOf(params.get("id"))));
                    BackRole superBackRole = backRoleService.findById(backRole
                            .getRoleSuper());
                    model.addAttribute("backRole", backRole);
                    model.addAttribute("superBackRole", superBackRole);
                }
                url = "role/saveUpdateRole";
            } else if ("showRoleList"
                    .equals(String.valueOf(params.get("type")))) {
                List<BackTree> treeModels = backRoleService
                        .findRoleTree(params);
                String outGroupHtml = TreeUtil.getCallBackTreeString(0, 0,
                        treeModels);
                model.addAttribute("outGroupHtml", outGroupHtml);
                url = "showRight";
            } else {
                if (params.containsKey("district.id")) {
                    backRole.setRoleSuper(Integer.parseInt(params.get(
                            "district.id").toString()));
                } else {
                    if (userId.intValue() == Constant.ADMINISTRATOR_ID
                            .intValue()) {
                        backRole.setRoleSuper(0);
                    } else {
                        backRole.setRoleSuper(backRoleService.findRoleTree(
                                params).get(0).getPid());
                    }
                }
                // 更新或者添加操作
                if (backRole.getId() != null) {
                    backRoleService.updateById(backRole);
                } else {
                    backRoleService.insert(backRole, userId);
                }
                SpringUtils.renderDwzResult(response, true, "操作成功",
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }
        } catch (Exception e) {
            SpringUtils.renderDwzResult(response, false, "操作失败",
                    DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                            .toString());
            log.error("添加权限信息失败，异常信息:", e);
        }
        model.addAttribute(MESSAGE, erroMsg);
        model.addAttribute("params", params);
        return url;
    }

    /**
     * 删除角色
     */
    @RequestMapping("deleteRole")
    public void deleteRole(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> params = this.getParametersO(request);
        boolean flag = false;
        try {
            Integer id = Integer.parseInt(params.get("id").toString());
            backRoleService.deleteById(id);
            flag = true;
        } catch (Exception e) {
            log.error("deleteRole error", e);
        }
        SpringUtils.renderDwzResult(response, flag, flag ? "删除角色成功" : "删除角色失败",
                DwzResult.CALLBACK_CLOSECURRENTDIALOG, params.get("parentId")
                        .toString());
    }

    /**
     * 根据roleId显示用户列表
     */
    @RequestMapping("showUserListByRoleId")
    public String showUserListByRoleId(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String errorMsg = null;
        try {
            List<BackTree> userTreeModelList = backRoleService
                    .showUserListByRoleId(Integer.valueOf(String.valueOf(params
                            .get("id"))));
            model.addAttribute("userTreeModelList", userTreeModelList);
            model.addAttribute("userTreeModelList", userTreeModelList);
        } catch (Exception e) {
            errorMsg = "服务器异常，请稍后重试！";
            log.error("showUserListByRoleId error ", e);
        }
        model.addAttribute(MESSAGE, errorMsg);
        return "role/showUsersRole";
    }

    /**
     * 基于当前登录用户给某个角色授权
     */
    @RequestMapping("showRoleModuleList")
    public String showRoleModuleList(HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String url = null;
        String erroMsg = null;
        try {
            BackUser backUser = this.loginAdminUser(request);
            if ("toJsp".equals(String.valueOf(params.get("type")))) {
                params.put("userId", backUser.getId());
                List<BackTree> userAll = backModuleService
                        .findModuleTree(params);
                List<BackTree> haveList = new ArrayList<>();
                Integer id = Integer.valueOf(params.get("id").toString());
                if (Constant.ADMINISTRATOR_ID.intValue() == id.intValue()) {
                    haveList.addAll(userAll);
                } else {
                    haveList = backRoleService.getTreeByRoleId(id);
                }
                String outGroupHtml = TreeUtil.getTreeStringWidthCheckBox(0, 0,
                        userAll, haveList, "rightIds");
                model.addAttribute("outGroupHtml", outGroupHtml);
                params.put("url", "role/showRoleModuleList");
                params.put("ajaxType", "navTabAjaxDone");
                url = "showRolesRight";
            } else {
                params.put("rightIds", request.getParameterValues("rightIds"));
                backRoleService.addRoleModule(params);
                SpringUtils.renderDwzResult(response, true, "角色授权成功",
                        DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                                .toString());
            }
        } catch (Exception e) {
            SpringUtils.renderDwzResult(response, false, "操作失败",
                    DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
                            .toString());
            log.error("角色授权失败，异常信息:", e);
        }
        model.addAttribute(MESSAGE, erroMsg);
        model.addAttribute("params", params);
        return url;
    }

}
