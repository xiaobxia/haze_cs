package com.info.back.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.info.back.service.ICollectionCompanyService;
import com.info.back.utils.DwzResult;
import com.info.back.service.IBackRoleService;
import com.info.back.service.ICollectionService;
import com.info.back.utils.IdGen;
import com.info.web.pojo.cspojo.BackRole;
import com.info.back.result.JsonResult;
import com.info.back.utils.BackConstant;
import com.info.back.utils.SpringUtils;
import com.info.web.pojo.cspojo.Collection;
import com.info.web.util.PageConfig;
import com.info.web.util.encrypt.Md5coding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.info.web.pojo.cspojo.MmanLoanCollectionCompany;

/**
 * 催收员管理Controller
 * @author xieyaling
 * @date 2017-02-10
 */
@Slf4j
@Controller
@RequestMapping("collection/")
public class CollectionController extends BaseController{

	@Resource
	private ICollectionService collectionService;
	@Resource
	private ICollectionCompanyService collectionCompanyService;
	@Resource
	private IBackRoleService backRoleService;
	/**
	 * 查询催收员列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("getCollectionPage")
	public String getCompanyPage(HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			HashMap<String, Object> params = getParametersO(request);
			BackRole topCsRole = backRoleService.getTopCsRole();
			List<Integer> roleChildIds = backRoleService.showChildRoleListByRoleId(topCsRole.getId());
			params.put("realIds", roleChildIds);
			PageConfig<Collection> pageConfig = collectionService.findPage(params);
			model.addAttribute("pm", pageConfig);
			model.addAttribute("params", params);
			List<MmanLoanCollectionCompany> companyList=collectionCompanyService.selectCompanyList();
			model.addAttribute("groupNameMap", BackConstant.GROUP_NAME_MAP);
			model.addAttribute("companyList",companyList);
			model.addAttribute("groupStatusMap",BackConstant.GROUP_STATUS_MAP);
		} catch (Exception e) {
			log.error("getCollectionPage error", e);
		}
		return "collection/collectionList";
	}
	
	
	
	/**
	 * xiangxiaoyan 跳转到添加页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("saveUpdateCollection")
	public String saveUpdateUser(HttpServletRequest request, HttpServletResponse response, Model model, Collection collection) {
		HashMap<String, Object> params = this.getParametersO(request);
		BackRole topCsRole = backRoleService.getTopCsRole();
		String url = null;
		String erroMsg = null;
		try {
			if ("toJsp".equals(String.valueOf(params.get("type")))) {
				if (params.containsKey("id")) {
					// 更新的页面跳转
					collection = collectionService.findOneCollection(Integer.parseInt(params.get("id").toString()));
					model.addAttribute("collection", collection);
				}
				List<MmanLoanCollectionCompany> companyList=collectionCompanyService.selectCompanyList();
				model.addAttribute("groupNameMap",BackConstant.GROUP_NAME_MAP);
				model.addAttribute("companyList",companyList);
				url = "collection/SUCollection";
			} else {
				JsonResult result=new JsonResult("-1","操作失败");
				// 更新或者添加操作
				if (collection.getId() != null) {
					result=collectionService.updateById(collection);
				} else {
					collection.setAddIp(this.getIpAddr(request));
					collection.setUserPassword(Md5coding.getInstance().code(String.valueOf(params.get("userPassword"))));
					collection.setUuid(IdGen.uuid());
					collection.setRoleId(topCsRole.getId().toString());
					result=collectionService.insert(collection);
				}
				SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
						DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId")
								.toString());
			}
		} catch (Exception e) {
			if (url == null) {
				erroMsg = "服务器异常，请稍后重试！";
				if (e.getLocalizedMessage().indexOf("UK_user_account") >= 0) {
					erroMsg = "用户名重复！";
				}else if(e.getLocalizedMessage().indexOf("UK_user_name") >= 0){
					erroMsg = "真实姓名不能重复";
				}
				SpringUtils.renderDwzResult(response, false, "操作失败,原因："
						+ erroMsg, DwzResult.CALLBACK_CLOSECURRENT, params.get(
						"parentId").toString());
			}
			log.error("添加权限信息失败，异常信息:", e);
		}
		model.addAttribute(MESSAGE, erroMsg);
		model.addAttribute("params", params);
		return url;
	}
	
	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("deleteCollection")
	public void deleteCollection(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> params =this.getParameters(request);
		JsonResult result=new JsonResult("-1","删除催收员失败");
		try {
			String id=params.get("id")==null?"":params.get("id");
			if(StringUtils.isNotBlank(id)){
				result=collectionService.deleteCollection(Integer.parseInt(id));
			}else{
				result.setMsg("参数错误");
			}
		} catch (Exception e) {
			log.error("deleteCompany error", e);
		}
		SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
				DwzResult.CALLBACK_CLOSECURRENTDIALOG, params.get("parentId").toString());
	}
	
	
}
