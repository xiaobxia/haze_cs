package com.info.back.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.info.back.result.JsonResult;
import com.info.back.service.ICollectionCompanyService;
import com.info.back.utils.DwzResult;
import com.info.constant.Constant;
import com.info.web.pojo.cspojo.MmanLoanCollectionCompany;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.info.back.utils.SpringUtils;
import com.info.web.util.PageConfig;
/**
 * 公司服Controller
 * @author xieyaling
 * @date 2017-02-10
 */
@Slf4j
@Controller
@RequestMapping("company/")
public class CollectionCompanyController extends BaseController{

	@Resource
	private ICollectionCompanyService collectioncompanyservice;
	/**
	 * 查询公司列表
	 */
	@RequestMapping("getCompanyPage")
	public String getCompanyPage(HttpServletRequest request, Model model) {
		try {
			HashMap<String, Object> params = getParametersO(request);
			params.put("noAdmin", Constant.ADMINISTRATOR_ID);
			PageConfig<MmanLoanCollectionCompany> pageConfig = collectioncompanyservice.findPage(params);
			model.addAttribute("pm", pageConfig);
			model.addAttribute("params", params);
		} catch (Exception e) {
			log.error("getCompanyPage error", e);
		}
		return "company/companyList";
	}
	/**
	 * 添加
	 */
	@RequestMapping("addCompany")
	public String addCompany(HttpServletRequest request, Model model){
		HashMap<String, Object> params = getParametersO(request);
		model.addAttribute("params", params);
		return "company/addCompany";
	}
	/**
	 * 保存
	 */
	@RequestMapping("saveCompany")
	public void saveCompany(HttpServletRequest request, HttpServletResponse response){
		JsonResult result=new JsonResult("-1","添加公司失败");
		Map<String, String> params =this.getParameters(request);
		try{
			result=collectioncompanyservice.saveCompany(params);
		}catch(Exception e){
			log.error("saveCompany error", e);
		}
		SpringUtils.renderDwzResult(response, "0".equals(result.getCode()),result.getMsg(), DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId"));
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("updateCompany")
	public String updateCompany(HttpServletRequest request, Model model){
		HashMap<String, Object> params = getParametersO(request);
		String id=request.getParameter("id")==null?"":request.getParameter("id");
		MmanLoanCollectionCompany company =collectioncompanyservice.get(id);
		model.addAttribute("params", params);
		model.addAttribute("company",company );
		return "company/updateCompany";
	}
	/**
	 * 修改
	 */
	@RequestMapping("updateDateCompany")
	public void updateDateCompany(HttpServletRequest request, HttpServletResponse response){
		JsonResult result=new JsonResult("-1","修改公司信息失败");
		Map<String, String> params =this.getParameters(request);
		try{
			result=collectioncompanyservice.updateCompany(params);
		}catch(Exception e){
			log.error("updateDateCompany error", e);
		}
		SpringUtils.renderDwzResult(response, "0".equals(result.getCode()),result.getMsg(),
				DwzResult.CALLBACK_CLOSECURRENT, params.get("parentId"));
	}
	/**
	 * 删除
	 * 
	 */
	@RequestMapping("deleteCompany")
	public void deleteCompany(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> params =this.getParameters(request);
		JsonResult result=new JsonResult("-1","删除公司失败");
		try {
			String compayId=params.get("id")==null?"":params.get("id");
			if(StringUtils.isNotBlank(compayId)){
				result=collectioncompanyservice.deleteCompany(compayId);
			}else{
				result.setMsg("参数错误");
			}
		} catch (Exception e) {
			log.error("deleteCompany error", e);
		}
		SpringUtils.renderDwzResult(response, "0".equals(result.getCode()), result.getMsg(),
				DwzResult.CALLBACK_CLOSECURRENTDIALOG, params.get("parentId"));
	}
	
}
