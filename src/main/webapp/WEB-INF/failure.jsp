<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作失败</title>
</head>
<body>
<!-- loginInfo是从登陆的servlet操作失败时存进去request的 -->
<h1>操作失败！错误信息为：${empty info? loginInfo : info }
	请重试或联系管理员</h1>
<c:if test="${! empty info }">
	<a href="${ctx }/user/frame">返回</a>
</c:if>
<c:if test="${! empty loginInfo }">
	<a href="${ctx }/user/login">返回</a>
</c:if>
</body>
</html>