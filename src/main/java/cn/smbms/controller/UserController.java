package cn.smbms.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	
//		if(method != null && method.equals("add")){
//			//增加操作
//			this.add(request, response);
//		}else if(method != null && method.equals("query")){
//			this.query(request, response);
//		}else if(method != null && method.equals("getrolelist")){
//			this.getRoleList(request, response);
//		}else if(method != null && method.equals("ucexist")){
//			this.userCodeExist(request, response);
//		}else if(method != null && method.equals("deluser")){
//			this.delUser(request, response);
//		}else if(method != null && method.equals("view")){
//			this.getUserById(request, response,"userview.jsp");
//		}else if(method != null && method.equals("modify")){
//			this.getUserById(request, response,"usermodify.jsp");
//		}else if(method != null && method.equals("modifyexe")){
//			this.modify(request, response);
//		}else if(method != null && method.equals("pwdmodify")){
//			this.getPwdByUserId(request, response);
//		}else if(method != null && method.equals("savepwd")){
//			this.updatePwd(request, response);
//		}
	
	@RequestMapping("/dologin")
	public String doLogin(HttpServletRequest request) {
		System.out.println("login ============ " );
		//获取用户名和密码
		String userCode = request.getParameter("userCode");
		String userPassword = request.getParameter("userPassword");
		//调用service方法，进行用户匹配
		User user;
		try {
			user = userService.login(userCode,userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("loginInfo", e.getMessage());
			return "failure";
		}
		if(null != user){//登录成功
			//放入session
			request.getSession().setAttribute(Constants.USER_SESSION, user);
			//页面跳转（frame.jsp）
			return "jsp/frame";
		}else{
			//页面跳转（login.jsp）带出提示信息--转发
			request.setAttribute("error", "用户名或密码不正确");
			return "login";
		}
	}
	
	@RequestMapping("/dologout")
	public String doLogout(HttpServletRequest request) {
		//清除session
		request.getSession().removeAttribute(Constants.USER_SESSION);
		return "login";
	}
	
	@RequestMapping("/updatepwd")
	public String updatePwd(HttpServletRequest request) {
		
		Object o = request.getSession().getAttribute(Constants.USER_SESSION);
		String newpassword = request.getParameter("newpassword");
		boolean flag = false;
		if(o != null && !StringUtils.isNullOrEmpty(newpassword)){
			try {
				flag = userService.updatePwd(((User)o).getId(),newpassword);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("info", e.getMessage());
				return "failure";
			}
			if(flag){
				request.setAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
				request.getSession().removeAttribute(Constants.USER_SESSION);//session注销
			}else{
				request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
			}
		}else{
			request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
		}
		return "jsp/pwdmodify";
	}
	
	@RequestMapping(value="/getpwdbyuserid", produces="application/json;charset=utf-8")
	@ResponseBody
	public String getPwdByUserId(HttpServletRequest request) {
		Object o = request.getSession().getAttribute(Constants.USER_SESSION);
		String oldpassword = request.getParameter("oldpassword");
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(null == o ){//session过期
			resultMap.put("result", "sessionerror");
		}else if(StringUtils.isNullOrEmpty(oldpassword)){//旧密码输入为空
			resultMap.put("result", "error");
		}else{
			String sessionPwd = ((User)o).getUserPassword();
			if(oldpassword.equals(sessionPwd)){
				resultMap.put("result", "true");
			}else{//旧密码输入不正确
				resultMap.put("result", "false");
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	
	@RequestMapping("/modify")
	public String modify(@ModelAttribute User user, Model model, HttpServletRequest request) {
		user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		user.setModifyDate(new Date());
		try {
			if(userService.modify(user)){
				return "redirect:/user/query";
			}else{
				return "jsp/usermodify";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("info", e.getMessage());
			return "failure";
		}
	}
	
	@RequestMapping("/getuserbyid")
	public String getUserById(String uid, Model model,String method) {
		if(!StringUtils.isNullOrEmpty(uid)){
			//调用后台方法得到user对象
			User user = null;
			try {
				user = userService.getUserById(uid);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("info", e.getMessage());
				return "failure";
			}
			model.addAttribute("user", user);
			if(method != null && method.equals("view")){
				return "jsp/userview";
			} else /*if(method != null && method.equals("modify")) */{
				return "jsp/usermodify";
			}
		} else {
			model.addAttribute("info", "用户编号为空，请重试！");
			return "failure";
		}
	}
	
	@RequestMapping(value="/deluser", produces="application/json;charset=utf-8")
	@ResponseBody
	public String delUser(String uid, Model model) {
		Integer delId = 0;
		try{
			delId = Integer.parseInt(uid);
		}catch (Exception e) {
			delId = 0;
		}
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(delId <= 0){
			resultMap.put("delResult", "notexist");
		}else{
			try {
				if(userService.deleteUserById(delId)){
					resultMap.put("delResult", "true");
				}else{
					resultMap.put("delResult", "false");
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("delResult", "failed");
				return JSONArray.toJSONString(resultMap);
			}
		}
		//把resultMap转换成json对象输出
		return JSONArray.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/ucexist", produces="application/json;charset=utf-8")
	@ResponseBody
	public String userCodeExist(String userCode) {
		//判断用户账号是否可用
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(userCode)){
			//userCode == null || userCode.equals("")
			resultMap.put("userCode", "exist");
		}else{
			User user = null;
			try {
				user = userService.selectUserCodeExist(userCode);
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("userCode", "failed");
				return JSONArray.toJSONString(resultMap);
			}
			if(null != user){
				resultMap.put("userCode","exist");
			}else{
				resultMap.put("userCode", "noexist");
			}
		}
		//把resultMap转为json字符串以json的形式输出
		//把resultMap转为json字符串 输出
		return JSONArray.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/getrolelist", produces="application/json;charset=utf-8")
	@ResponseBody
	public String getRoleList() {
		List<Role> roleList = null;
		try {
			roleList = roleService.getRoleList();
		} catch (Exception e) {
			e.printStackTrace();
			return "failed";
		}
		//把roleList转换成json对象输出
		return JSONArray.toJSONString(roleList);
	}
	
	@RequestMapping("/query")
	public String query(Model model, @RequestParam(value="queryname",required=false) String queryUserName, 
						@RequestParam(value="queryUserRole",required=false)String temp, 
						@RequestParam(value="pageIndex",required=false)String pageIndex) {
		//查询用户列表
		int queryUserRole = 0;
		List<User> userList = null;
		//设置页面容量
    	int pageSize = Constants.pageSize;
    	//当前页码
    	int currentPageNo = 1;
		/**
		 * http://localhost:8090/SMBMS/userlist.do
		 * ----queryUserName --NULL
		 * http://localhost:8090/SMBMS/userlist.do?queryname=
		 * --queryUserName ---""
		 */
		System.out.println("queryUserName servlet--------"+queryUserName);  
		System.out.println("queryUserRole servlet--------"+queryUserRole);  
		System.out.println("query pageIndex--------- > " + pageIndex);
		if(queryUserName == null){
			queryUserName = "";
		}
		if(temp != null && !temp.equals("")){
			queryUserRole = Integer.parseInt(temp);
		}
		
    	if(pageIndex != null){
    		try{
    			currentPageNo = Integer.valueOf(pageIndex);
    		}catch(NumberFormatException e){
    			model.addAttribute("info", e.getMessage());
				return "failure";
    		}
    	}	
    	//总数量（表）	
    	int totalCount = 0;
		try {
			totalCount = userService.getUserCount(queryUserName,queryUserRole);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("info", e.getMessage());
			return "failure";
		}
    	//总页数
    	PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	
    	int totalPageCount = pages.getTotalPageCount();
    	
    	//控制首页和尾页
    	if(currentPageNo < 1){
    		currentPageNo = 1;
    	}else if(currentPageNo > totalPageCount){
    		currentPageNo = totalPageCount;
    	}
		try {
			userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("info", e.getMessage());
			return "failure";
		}
		model.addAttribute("userList", userList);
		List<Role> roleList = null;
		try {
			roleList = roleService.getRoleList();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("info", e.getMessage());
			return "failure";
		}
		model.addAttribute("roleList", roleList);
		model.addAttribute("queryUserName", queryUserName);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "jsp/userlist";
	}
	
	@RequestMapping("/add")
	public String add(User user, Model model,HttpServletRequest request) {
		System.out.println("add()================");
		user.setCreationDate(new Date());
		user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		
		try {
			if(userService.add(user)){
				return "redirect:/user/query";
			}else{
				return "jsp/useradd";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("info", e.getMessage());
			return "failure";
		}
	}
	
	/**
	 * 跳转页面的公共方法1
	 * @return
	 */
	@RequestMapping("/forwardtojsp1")
	public String forwardtojsp1(String url) {
		return "jsp/" + url;
	}
	
	/**
	 * 跳转页面的公共方法2
	 * @return
	 */
	@RequestMapping("/forwardtojsp2")
	public String login(String url){
		return url;
	}
	
	/**
	 * 书本的json测试
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/testjson", produces="application/json;charset=utf-8")
	@ResponseBody // 用了response的话，这个标注等于没用。这个注解和上面的produces都是针对返回值的
	public void view(String id, HttpServletResponse response) {
		String cjson = "";
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null == id || "".equals(id)) {
			out.print("nodata");
		} else {
			try {
				User user = userService.getUserById(id);
				cjson = JSON.toJSONString(user);
			} catch (Exception e) {
				e.printStackTrace();
				out.print("failed");
			}
			out.print(cjson);
			out.flush();
			out.close();
		}
	}
}
