<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Welcome, Admin!</h1>

	<p>This is your dashboard.</p>

	<div class="cards">
		<div class="card">Total Users: ${totalUsers}</div>
		<div class="card">Total Movies: ${totalMovies}</div>
		<div class="card">Total Tickets Sold: ${totalTickets}</div>
	</div>
</body>
</html>