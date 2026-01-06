<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Beta Cinemas - Lịch Chiếu Theo Rạp</title>

    <link rel="stylesheet" href="css/index.css"> 
    <link rel="stylesheet" href="css/showtimes.css"> 
    
    <script>
        // HÀM JS ĐƯỢC ĐƠN GIẢN HÓA: Khi chọn Thành phố, tự động submit form để load Rạp
        // và giữ lại các tham số lọc khác.
        function autoSubmitCity() {
            // Khi người dùng thay đổi thành phố, form được submit để cập nhật danh sách rạp.
            // Action mặc định vẫn là /showtime?action=search
            document.getElementById('showtime-form').submit();
        }
    </script>
</head>
<body>

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

<div class="movie-tabs-container">
    <div class="movie-tabs">
         <a href="${pageContext.request.contextPath}/movie?action=coming_soon" 
           class="tab-item ${param.action == 'now_showing' || param.action == 'special_show' ? 'active' : ''}">
           PHIM SẮP CHIẾU
        </a> 
        
        <a href="${pageContext.request.contextPath}/movie?action=now_showing" 
           class="tab-item ${param.action == 'now_showing' ? 'active' : ''}">
           PHIM ĐANG CHIẾU
        </a> 
    </div>
</div>

<div class="filter-container">
    <form action="${pageContext.request.contextPath}/showtime" method="get" class="filter-form" id="showtime-form">
        <input type="hidden" name="action" value="search">
        
        <c:set var="todayDate">
            <%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>
        </c:set>
        
        <select name="city" onchange="autoSubmitCity()">
            <option value="">Chọn Thành phố</option>
            <c:forEach var="city" items="${cityList}">
                <option value="${city}" ${param.city == city ? 'selected' : ''}>${city}</option>
            </c:forEach>
        </select>

        <select name="cinemaId">
            <option value="">Chọn Rạp</option>
            <c:forEach var="cinema" items="${cinemaList}">
                <option value="${cinema.cinemaId}" ${param.cinemaId == cinema.cinemaId ? 'selected' : ''}>${cinema.name}</option>
            </c:forEach>
        </select>

        <input type="date" name="date" value="${param.date != null ? param.date : todayDate}" required>
		
        <select name="genre">
            <option value="">Tất cả Thể loại</option>
            <c:forEach var="genre" items="${genreList}">
                <option value="${genre}" ${param.genre == genre ? 'selected' : ''}>${genre}</option>
            </c:forEach>
        </select>
		       
        <select name="ageRating">
            <option value="">Tất cả Độ tuổi</option>
            <c:forEach var="rating" items="${ageRatingList}">
                <option value="${rating}" ${param.ageRating == rating ? 'selected' : ''}>${rating} (${rating} Max)</option>
            </c:forEach>
        </select>
		       
        <button type="submit">TÌM KIẾM</button>
    </form>
</div>

<div class="movie-list-container">

    <c:choose>
        <c:when test="${not empty showtimeMovies}">
        
            <c:if test="${not empty defaultMessage}">
                <div class="alert-info">
                    ${defaultMessage}
                </div>
            </c:if>
            
            <h2>
                <c:choose>
                    <c:when test="${not empty defaultMessage}">Phim Đang Chiếu</c:when>
                    <c:otherwise>Lịch Chiếu theo Rạp và Ngày</c:otherwise>
                </c:choose>
            </h2>

            <div class="movie-grid">
                <c:forEach var="movie" items="${showtimeMovies}">
                    <div class="movie-card">
                        
								<div class="movie-poster-container">
                             <span class="age-rating-badge">${movie.ageRating}</span>
                             
                             <c:set var="poster" value="${fn:trim(movie.posterUrl)}" />
                             <img src="${pageContext.request.contextPath}/images/movies/${fn:replace(poster,'/images/','')}"
                                  alt="${movie.title}" class="movie-poster">
                        </div>
                        
                        <div class="movie-details">
                            <h3>${movie.title}</h3>
                            <p>Thể loại: ${movie.genre}</p>
                            <p>Thời lượng: ${movie.duration} phút</p>
                        </div>
                        
                        <c:if test="${empty defaultMessage}">
                            <div class="showtimes-wrapper">
                                <h4>Chọn Suất Chiếu:</h4>
                                
                                <c:choose>
                                    <c:when test="${not empty movie.showtimes}">
                                        <c:forEach var="showtime" items="${movie.showtimes}">
                                            <div class="showtime-slot">
                                                <div class="showtime-info">
                                                    <strong>${showtime.startTime}</strong> | Phòng: ${showtime.roomId}<br>
                                                    Giá: ${showtime.ticketPrice} VND
                                                </div>
                                                <a href="booking?showtimeId=${showtime.showtimeId}" class="book-btn">ĐẶT VÉ</a>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p style="color: #999; font-size: 13px;">Không có suất chiếu nào được tìm thấy.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>

                        <c:if test="${not empty defaultMessage}">
                            <a href="${pageContext.request.contextPath}/movie?id=${movie.movieId}" class="buy-ticket-btn">MUA VÉ</a>
                        </c:if>
                        
                    </div> 
                </c:forEach>
            </div>
        </c:when>

        <c:otherwise>
            <div class="no-movies-found">
                <p style="color: white; text-align: center; padding: 50px;">
                    <c:choose>
                        <c:when test="${not empty errorMessage}">${errorMessage}</c:when>
                        <c:otherwise>Vui lòng chọn điều kiện tìm kiếm để xem lịch chiếu.</c:otherwise>
                    </c:choose>
                </p>
            </div>
        </c:otherwise>

    </c:choose>

</div>
<script src="<c:url value='/js/common.js'/>"></script>
<script src="<c:url value='/js/movie/movie.js'/>"></script>
</body>
</html>