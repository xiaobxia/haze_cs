package com.info.back.controller;

import com.info.back.annotation.ExcludeUrl;
import com.info.back.service.IBackModuleService;
import com.info.back.utils.SpringUtils;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.BackModule;
import com.info.web.pojo.cspojo.BackUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述：首页类 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 下午02:57:46 <br>
 */
@Slf4j
@Controller
public class IndexController extends BaseController {

    @Resource
    private IBackModuleService backModuleService;

    @ExcludeUrl
    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            BackUser backUser = loginAdminUser(request);
            if (backUser == null) {
                return "login";
            }
            params.put("userId", backUser.getId());
            params.put("parentId", "0");
            // 获得顶级菜单
            List<BackModule> menuModuleList = backModuleService
                    .findAllModule(params);
            if (menuModuleList != null && menuModuleList.size() > 0) {
                int moduleId = menuModuleList.get(0).getId();
                params.put("parentId", moduleId);
                // 获得某个顶级菜单的子菜单（二级菜单）
                List<BackModule> subMenu = backModuleService
                        .findAllModule(params);
                for (BackModule backModule : subMenu) {
                    params.put("parentId", backModule.getId());
                    // 获得某个二级菜单的子菜单（三级菜单）
                    List<BackModule> thirdMenu = backModuleService
                            .findAllModule(params);
                    backModule.setModuleList(thirdMenu);
                }
                model.addAttribute("subMenu", subMenu);
            }
            model.addAttribute(Constant.BACK_USER, backUser);
            model.addAttribute("menuModuleList", menuModuleList);
            model.addAttribute("seatExt", backUser.getSeatExt());
        } catch (Exception e) {
            log.error("back index error ", e);
        }
        return "index";
    }

    @RequestMapping("subMenu")
    public String subMenu(HttpServletRequest request,
                          Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        try {
            BackUser backUser = loginAdminUser(request);
            params.put("userId", backUser.getId());
            params.put("parentId", params.get("myId"));
            // 获得某个顶级菜单的子菜单（二级菜单）
            List<BackModule> subMenu = backModuleService.findAllModule(params);
            for (BackModule backModule : subMenu) {
                params.put("parentId", backModule.getId());
                // 获得某个二级菜单的子菜单（三级菜单）
                List<BackModule> thirdMenu = backModuleService
                        .findAllModule(params);
                backModule.setModuleList(thirdMenu);
            }
            model.addAttribute("subMenu", subMenu);
        } catch (Exception e) {
            log.error("展示权限树失败，异常信息:", e);
        }
        return "subMenu";
    }

    @ExcludeUrl
    @RequestMapping("rightSubList")
    public String rightSubList(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = new HashMap<>();
        try {
            params.put("parentId", request.getParameter("parentId"));
            params.put("myId", request.getParameter("myId"));
            BackUser backUser = loginAdminUser(request);
            params.put("userId", backUser.getId());
            // 获得当前登录用户的myId下的子权限
            List<BackModule> rightSubList = backModuleService
                    .findAllModule(params);
            model.addAttribute("rightSubList", rightSubList);
        } catch (Exception e) {
            log.error("rightSubList error ", e);
            model.addAttribute(MESSAGE, "权限查询异常！");
        }

        return "rightSubList";
    }

    /**
     * 更新系统缓存<br>
     */
    @ExcludeUrl
    @RequestMapping("updateCache")
    public void updateCache(HttpServletRequest request,
                            HttpServletResponse response) {
        boolean succ = false;
        try {
            BackUser backUser = loginAdminUser(request);
            if (backUser != null) {
                succ = true;

            }
        } catch (Exception e) {
            log.error("updateCache error ", e);
            succ = false;
        }
        SpringUtils.renderDwzResult(response, succ, succ ? "刷新缓存成功" : "刷新缓存失败");

    }
}
