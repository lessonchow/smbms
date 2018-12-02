package cn.smbms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/provider")
public class ProviderController {

	@Resource
	private ProviderService providerService;
	
//		String method = request.getParameter("method");
//		if(method != null && method.equals("query")){
//			this.query(request,response);
//		}else if(method != null && method.equals("add")){
//			this.add(request,response);
//		}else if(method != null && method.equals("view")){
//			this.getProviderById(request,response,"providerview.jsp");
//		}else if(method != null && method.equals("modify")){
//			this.getProviderById(request,response,"providermodify.jsp");
//		}else if(method != null && method.equals("modifysave")){
//			this.modify(request,response);
//		}else if(method != null && method.equals("delprovider")){
//			this.delProvider(request,response);
//		}

	@RequestMapping(value="/delprovider", produces="application/json;charset=utf-8")
	@ResponseBody
	public String delProvider(String proid,HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(!StringUtils.isNullOrEmpty(proid)){
			int flag = 0;
			try {
				flag = providerService.deleteProviderById(proid);
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("delResult", "failed");
				return JSONArray.toJSONString(resultMap);
			}
			if(flag == 0){//删除成功
				resultMap.put("delResult", "true");
			}else if(flag == -1){//删除失败
				resultMap.put("delResult", "false");
			}else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
				resultMap.put("delResult", String.valueOf(flag));
			}
		}else{
			resultMap.put("delResult", "notexit");
		}
		//把resultMap转换成json对象输出
		return JSONArray.toJSONString(resultMap);
	}
	
	@RequestMapping("/modify")
	public String modify(Provider provider, Model model, HttpServletRequest request) {
		
		provider.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		provider.setModifyDate(new Date());
		boolean flag = false;
		try {
			flag = providerService.modify(provider);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("info", e.getMessage());
			return "failure";
		}
		if(flag){
			return "redirect:/provider/query";
		}else{
			return "jsp/providermodify";
		}
	}
	
	@RequestMapping("/getproviderbyid")
	public String getProviderById(String proid, String method, Model model) {
		if(!StringUtils.isNullOrEmpty(proid)){
			Provider provider = null;
			try {
				provider = providerService.getProviderById(proid);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("info", e.getMessage());
				return "failure";
			}
			model.addAttribute("provider", provider);
			if(method != null && method.equals("view")){
				return "jsp/providerview";
			}else {
				return "jsp/providermodify";
			}
		} else {
			model.addAttribute("info", "供应商编号为空，请重试！");
			return "failure";
		}
	}
	
	@RequestMapping("/add")
	public String add(Provider provider, HttpServletRequest request) {
		provider.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		provider.setCreationDate(new Date());
		boolean flag = false;
		try {
			flag = providerService.add(provider);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("info", e.getMessage());
			return "failure";
		}
		if(flag){
			return "redirect:/provider/query";
		}else{
			return "provideradd";
		}
	}
	
	@RequestMapping("/query")
	public String query(String queryProName, String queryProCode, Model model) {
		if(StringUtils.isNullOrEmpty(queryProName)){
			queryProName = "";
		}
		if(StringUtils.isNullOrEmpty(queryProCode)){
			queryProCode = "";
		}
		List<Provider> providerList = new ArrayList<Provider>();
		try {
			providerList = providerService.getProviderList(queryProName,queryProCode);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("info", e.getMessage());
			return "failure";
		}
		model.addAttribute("providerList", providerList);
		model.addAttribute("queryProName", queryProName);
		model.addAttribute("queryProCode", queryProCode);
		return "jsp/providerlist";
	}
	
	/**
	 * 跳转页面的公共方法1
	 * @return
	 */
	@RequestMapping("/forwardtojsp1")
	public String forwardtojsp1(String url) {
		return "jsp/" + url;
	}
}
