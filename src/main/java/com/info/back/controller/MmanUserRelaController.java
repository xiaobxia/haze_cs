package com.info.back.controller;

import com.info.back.dao.IMmanUserInfoDao;
import com.info.back.service.IMmanLoanCollectionOrderService;
import com.info.back.service.IMmanUserRelaService;
import com.info.web.pojo.cspojo.MmanLoanCollectionOrder;
import com.info.web.pojo.cspojo.MmanUserInfo;
import com.info.web.pojo.cspojo.MmanUserRela;
import com.info.web.synchronization.dao.IDataDao;
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
@RequestMapping("mmanUserRela/")
public class MmanUserRelaController extends BaseController {


    @Resource
    private IMmanUserRelaService mmanUserRelaService;

    @Resource
    private IMmanLoanCollectionOrderService mmanLoanCollectionOrderService;
    @Resource
    private IDataDao dataDao;
    @Resource
    private IMmanUserInfoDao mmanUserInfoDao;

    @RequestMapping("getMmanUserRelaPage")
    public String getMmanUserRelaPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String orderId = params.get("id").toString();
        String erroMsg = null;
        try {
            MmanLoanCollectionOrder order = mmanLoanCollectionOrderService.getOrderById(orderId);
            params.put("userId", order.getUserId());
            int overdueDays = order.getOverdueDays();//逾期天数
            params.put("overdueDays", overdueDays);

//            if (overdueDays < 4) {
//                erroMsg = "逾期4天方可申请查看通讯录";
//                model.addAttribute(MESSAGE, erroMsg);
//                model.addAttribute("navTab", "true");
//                return "mycollectionorder/collectionOrder";
//            } else {

//			}else if(overdueDays >= 2 && overdueDays <= 3){
//				params.put("num",2);
//				List<MmanUserRela> list = mmanUserRelaService.getList(params);
//				model.addAttribute("list", list);
//			}else if(overdueDays >= 4 && overdueDays <= 5){
//				params.put("num",5);
//				List<MmanUserRela> list = mmanUserRelaService.getList(params);
//				model.addAttribute("list", list);
//			}else if(overdueDays >= 6 && overdueDays <= 10){
//				params.put("num",15);
//				List<MmanUserRela> list = mmanUserRelaService.getList(params);
//				model.addAttribute("list", list);
//			}else{
//				PageConfig<MmanUserRela> pageConfig = mmanUserRelaService.findPage(params);
//				model.addAttribute("pm", pageConfig);
//			}

            //显示通讯录
            List<HashMap<String, Object>> userContactsmap = new ArrayList<>();
            HashMap<String, String> param = new HashMap<>();
            param.put("USER_ID", order.getUserId());
            userContactsmap = dataDao.getUserContacts(param);
            model.addAttribute("userContactsmap", userContactsmap);
            //查找用户的信息
            MmanUserInfo mmanUserInfo = mmanUserInfoDao.get(order.getUserId());

            model.addAttribute("mmanUserInfo", mmanUserInfo);
//            }

        } catch (Exception e) {
            log.error("getMmanUserRelaPage error", e);
        }
        model.addAttribute("orderId", orderId);
        model.addAttribute("params", params);
        model.addAttribute(MESSAGE, erroMsg);
        return "mycollectionorder/mmanUserRelaList";
    }

    @RequestMapping("getMmanUserRelaCountPage")
    public String getMmanUserRelaCountPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        //String erroMsg = null;
        try {
            HashMap<String, Object> params = this.getParametersO(request);
            String orderId = params.get("id").toString();
            MmanLoanCollectionOrder order = mmanLoanCollectionOrderService.getOrderById(orderId);
            params.put("userId", order.getUserId());
            List<MmanUserRela> list = mmanUserRelaService.getList(params);
            model.addAttribute("list", list);
            PageConfig<MmanUserRela> page = mmanUserRelaService.findAllPage(params);
            model.addAttribute("pm", page);
            model.addAttribute("params", params);
        } catch (Exception e) {
            log.error("params error", e);
            model.addAttribute(MESSAGE, "服务器异常，请稍后重试！");
        }
        return "mycollectionorder/relaCountPage";
    }

}
