<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<header class="header">
    <div class="logo-container">
        <img src="${pageContext.request.contextPath}/images/movies/action_blast.jpg" class="logo">
		<a class="cinema-location" href="index.jsp">
		    T131GENG
		</a>

    </div>

	<nav class="main-nav">
	    <ul>
	        <li>
	            <a href="${pageContext.request.contextPath}/showtime"
	               class="${pageContext.request.servletPath == '/showtime' ? 'active' : ''}">
	                PHIM
	            </a>
	        </li>
	
	        <li>
	            <a href="${pageContext.request.contextPath}/news"
	               class="${pageContext.request.servletPath == '/news' ? 'active' : ''}">
	                TIN MỚI VÀ ƯU ĐÃI
	            </a>
	        </li>
	
	        <li>
	            <a href="${pageContext.request.contextPath}/member"
	               class="${pageContext.request.servletPath == '/member' ? 'active' : ''}">
	                THÀNH VIÊN
	            </a>
	        </li>
	    </ul>
	    
	</nav>
		
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <div class="user-status">
                <div class="user-menu" onclick="toggleUserDropdown()">
                    <span class="greeting-text">Xin chào,</span>
                    <span class="user-name">
                        ${fn:substring(sessionScope.user.fullName, 0, 12)}
                    </span>
                    <div class="user-icon">👤</div>

                    <div class="user-dropdown">
                        <a href="javascript:void(0)" onclick="openProfileModal()">Thông tin cá nhân</a>
                        <a href="javascript:void(0)" onclick="openOrderModal()">Đơn hàng</a>
                        <a href="${pageContext.request.contextPath}/admin?action=dashboard">Trang quản trị</a>
                        <hr>
                        <a href="${pageContext.request.contextPath}/user?action=logout">Đăng xuất</a>
                    </div>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <a href="${pageContext.request.contextPath}/login.jsp" class="login-btn">Đăng nhập</a>
        </c:otherwise>
    </c:choose>
</header>
