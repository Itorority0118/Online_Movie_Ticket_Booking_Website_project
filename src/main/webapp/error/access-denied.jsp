<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Access Denied</title>
    <link rel="stylesheet" href="<c:url value='/css/error/access-denied.css'/>">
</head>
<body>

<div class="denied-container">

    <div class="denied-icon">🚫</div>

    <div class="denied-title">Access Denied</div>

    <div class="denied-message">
        Bạn không có quyền truy cập trang này.<br/>
        Chỉ tài khoản <strong>Admin</strong> mới được phép.
    </div>

	<c:if test="${not empty sessionScope.user}">
	    <div class="current-role">
	        <span>Quyền hiện tại của bạn</span>
	        <strong>${sessionScope.user.role}</strong>
	    </div>
	</c:if>

    <a href="<%=request.getContextPath()%>/index.jsp">
        <button class="btn">Về trang chủ</button>
    </a>

    <div class="denied-links">
        <a href="<%=request.getContextPath()%>/login.jsp">Đăng nhập lại</a>
    </div>

</div>

</body>
</html>
