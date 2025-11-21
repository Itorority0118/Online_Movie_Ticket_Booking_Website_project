<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="../css/admin.css">
</head>
<body>

<div class="sidebar">
    <h2>Admin Panel</h2>
    <ul>
		<li><a href="${pageContext.request.contextPath}/user?action=list">Manage Users</a></li>
		<li><a href="${pageContext.request.contextPath}/movie?action=list">Manage Movies</a></li>
		<li><a href="${pageContext.request.contextPath}/schedule?action=list">Manage Schedules</a></li>
		<li><a href="${pageContext.request.contextPath}/ticket?action=list">Manage Tickets</a></li>
    </ul>
</div>

<div class="content">
    <h1>Welcome, Admin!</h1>

    <p>This is your dashboard.</p>

    <div class="cards">
        <div class="card">Total Users: ${totalUsers}</div>
        <div class="card">Total Movies: ${totalMovies}</div>
        <div class="card">Total Tickets Sold: ${totalTickets}</div>
    </div>
</div>

</body>
</html>
