package cn.smbms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.smbms.pojo.User;

public class SysInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("SysInterceptor preHandle()=======================");
		User userSession = (User)request.getSession().getAttribute("userSession");
		String contextPath = request.getContextPath();
		String servletPath = request.getServletPath();
//		String validationPath = File.separator + "jsp" + File.separator;
		if(null == userSession 
				&& servletPath.indexOf("login") < 0 
				&& servletPath.indexOf("logout") < 0
				&& servletPath.indexOf("error") < 0
				&& servletPath.indexOf("failure") < 0){
			response.sendRedirect(contextPath + "/error");
			return false;
		}
		return true;
	}
	
	
	
}
