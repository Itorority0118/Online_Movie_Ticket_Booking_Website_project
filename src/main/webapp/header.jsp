<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header class="header">
    <div class="logo-container">
        <img src="${pageContext.request.contextPath}/images/movies/action_blast.jpg" class="logo">
        <span class="cinema-location">Beta Thái Nguyên</span>
    </div>

    <nav class="main-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/showtime">PHIM</a></li>
            <li><a href="#">TIN MỚI VÀ ƯU ĐÃI</a></li>
            <li><a href="#">NHƯỢNG QUYỀN</a></li>
            <li><a href="#">THÀNH VIÊN</a></li>
        </ul>
    </nav>

    <div class="user-status">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <div class="user-menu">
                    <span>Xin chào, ${sessionScope.user.fullName}</span>
                    <div class="user-icon" onclick="toggleUserDropdown()">👤</div>

                    <div class="user-dropdown" id="userDropdown">
                        <c:if test="${sessionScope.role == 'CUSTOMER'}">
                            <a href="javascript:void(0)" onclick="openProfileModal()">Thông tin cá nhân</a>
                        </c:if>

                        <a href="javascript:void(0)" onclick="openOrderModal()">Đơn hàng</a>

                        <c:if test="${sessionScope.role == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard">Trang quản trị</a>
                        </c:if>

                        <hr>
                        <a href="${pageContext.request.contextPath}/user?action=logout">Đăng xuất</a>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login.jsp" class="login-btn">Đăng nhập</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>
