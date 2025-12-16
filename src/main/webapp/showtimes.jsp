<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Beta Cinemas - Lịch Chiếu Theo Rạp</title>

    <link rel="stylesheet" href="css/index.css"> 
    <link rel="stylesheet" href="css/showtimes.css"> 
</head>
<body>

<header class="header">
    <div class="logo-container">
        <img src="${pageContext.request.contextPath}/images/movies/action_blast.jpg" alt="Beta Cinemas Logo" class="logo">
        <span class="cinema-location">Beta Thái Nguyên</span>
    </div>

    <nav class="main-nav">
        <ul>
            <li><a href="showtimes.jsp" class="tab-item active">LỊCH CHIẾU THEO RẠP</a></li>
            <li><a href="movie.jsp">PHIM</a></li>
            <li><a href="#">RẠP VÉ</a></li>
            <li><a href="#">GIÁ VÉ</a></li>
            <li><a href="#">TIN MỚI VÀ ƯU ĐÃI</a></li>
            <li><a href="#">NHƯỢNG QUYỀN</a></li>
            <li><a href="#">THÀNH VIÊN</a></li>
        </ul> 
    </nav>
    
        <div class="user-status">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <span>Xin chào, ${sessionScope.user.fullName}</span>
                <a href="logout" class="logout-btn">Đăng xuất</a>
            </c:when>
            <c:otherwise>
                <a href="login.jsp" class="login-btn">Đăng nhập</a> 
            </c:otherwise>
        </c:choose>
    </div>
</header>

<div class="movie-tabs-container">
    <div class="movie-tabs">
        <a href="moviesoon.jsp" class="tab-item">PHIM SẮP CHIẾU</a>
        <a href="index.jsp" class="tab-item">PHIM ĐANG CHIẾU</a>
        <a href="#" class="tab-item">SUẤT CHIẾU ĐẶC BIỆT</a>
    </div>
</div>

<div class="filter-container">
    <form action="showtime" method="get" class="filter-form">
        <input type="hidden" name="action" value="search">

        <select name="city" required>
            <option value="">Chọn Thành phố</option>
            <option value="Thai Nguyen">Thái Nguyên</option>
            <option value="Ha Noi">Hà Nội</option>
        </select>

        <select name="cinemaId" required>
            <option value="">Chọn Rạp</option>
            <option value="1">Beta Thái Nguyên</option>
            <option value="2">Beta Hà Nội</option>
        </select>

        <input type="date" name="date"
		       min="2025-01-01"
		       max="2025-12-31"
		       required>
		       
        <button type="submit">TÌM KIẾM</button>
    </form>
</div>

<div class="movie-list-container">

    <c:choose>

        <c:when test="${not empty showtimeResults}">
            <c:forEach var="s" items="${showtimeResults}">
                <div class="movie-item">
                    <div class="movie-info">
                        <div class="movie-details">
                            <h3>Movie ID: ${s.movieId}</h3>
                            <p>Giờ chiếu: ${s.startTime}</p>
                            <p>Phòng chiếu: ${s.roomId}</p>
                            <p>Giá vé: ${s.ticketPrice} VND</p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>

        <c:otherwise>
            <div class="no-movies-found">
                <p></p>
            </div>
        </c:otherwise>

    </c:choose>

</div>

</body>
</html>
