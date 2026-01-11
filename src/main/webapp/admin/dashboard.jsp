<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<title>Admin Dashboard</title>
<link rel="stylesheet" href="<c:url value='/css/admin.css?v=14'/>">
<link rel="stylesheet" href="<c:url value='/css/modal.css?v=1'/>">
</head>
<body>

    <div class="sidebar">
        <h2>
            <a data-page="${pageContext.request.contextPath}/admin?action=dashboard"
               class="home-link">Admin Panel</a>
        </h2>
		<c:if test="${not empty sessionScope.user}">
		    <div class="admin-avatar" onclick="openProfileModal()">
		        👤
		    </div>
		</c:if>
        <ul>
            <li>
		        <a href="${pageContext.request.contextPath}/index.jsp" 
		           data-page="${pageContext.request.contextPath}/index.jsp">
		           Main Page
		        </a>
		    </li>
       
            <li><a href="#"
                data-page="${pageContext.request.contextPath}/user?action=list">
                Manage Users</a></li>

			<li class="${requestScope.activeSidebar eq 'movie' ? 'active' : ''}">
			    <a href="#" data-page="${pageContext.request.contextPath}/movie?action=list">Manage Movies</a>
			</li>

			<li class="${requestScope.activeSidebar eq 'cinema' ? 'active' : ''}">
			    <a href="${pageContext.request.contextPath}/cinema?action=list"
			       data-page="${pageContext.request.contextPath}/cinema?action=list">Manage Cinemas</a>
			</li>
			
			<li class="${requestScope.activeSidebar eq 'ticket' ? 'active' : ''}">
			    <a href="#" data-page="${pageContext.request.contextPath}/ticket?action=list">Manage Tickets</a>
			</li>

			<li class="${requestScope.activeSidebar eq 'ticket' ? 'active' : ''}">
			    <a href="#" data-page="${pageContext.request.contextPath}/payment?action=list">Manage Payments</a>
			</li>
        </ul>

        <ul class="logout">
            <li><a href="${pageContext.request.contextPath}/user?action=logout">
                LOGOUT</a></li>
        </ul>
    </div>

    <div class="content" id="content-area">
        <c:choose>
            <c:when test="${param.page != null}">
                <jsp:include page="${param.page}" />
            </c:when>
            <c:otherwise>
                <jsp:include page="dashboardContent.jsp" />
            </c:otherwise>
        </c:choose>
           
    </div>
<div class="admin-profile-overlay" id="adminProfileModal">
    <div class="admin-profile-modal">

        <div class="admin-profile-header">
            <div class="admin-profile-avatar">👤</div>

            <div class="admin-profile-title">
                <h3>Thông tin cá nhân</h3>
                <span>Administrator</span>
            </div>

            <button class="admin-profile-close" onclick="closeProfileModal()">✕</button>
        </div>

        <form id="adminProfileForm">
            <div class="form-group">
                <label>Họ và tên</label>
                <input type="text" value="${sessionScope.user.fullName}" readonly>
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email" value="${sessionScope.user.email}" readonly>
            </div>

            <div class="form-group">
                <label>Số điện thoại</label>
                <input type="text" value="${sessionScope.user.phone}" readonly>
            </div>
        </form>

    </div>
</div>

    
    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>
    <script src="<c:url value='/js/admin.js?v=6'/>"></script>
	<script>
		function openProfileModal() {
		    const modal = document.getElementById("adminProfileModal");
		    modal.style.display = "flex";
		    requestAnimationFrame(() => modal.classList.add("show"));
		}
	
		function closeProfileModal() {
		    const modal = document.getElementById("adminProfileModal");
		    modal.classList.remove("show");
		    setTimeout(() => modal.style.display = "none", 300);
		}
		document.getElementById("adminProfileModal").addEventListener("click", function (e) {
		    if (e.target.id === "adminProfileModal") {
		        closeProfileModal();
		    }
		});
	</script>
</body>
</html>
