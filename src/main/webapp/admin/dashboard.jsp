<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>Admin Dashboard</title>
<link rel="stylesheet"
	href="<c:url value='/css/admin.css?v=7'/>">

</head>
<body>

	<div class="sidebar">
		<h2>
<a data-page="${pageContext.request.contextPath}/admin?action=dashboard"
   class="home-link">Admin Panel</a>


		</h2>
		<ul>
			<li><a
				data-page="${pageContext.request.contextPath}/user?action=list">Manage
					Users</a></li>
			<li><a
				data-page="${pageContext.request.contextPath}/movie?action=list">Manage
					Movies</a></li>
			<li><a
				data-page="${pageContext.request.contextPath}/schedule?action=list">Manage
					Schedules</a></li>
			<li><a
				data-page="${pageContext.request.contextPath}/ticket?action=list">Manage
					Tickets</a></li>
		</ul>

		<ul class="logout">
			<li><a
				href="${pageContext.request.contextPath}/user?action=logout">LOGOUT</a></li>
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
	<script
		src="<c:url value='/js/admin.js?v=1'/>"></script>
</body>
</html>