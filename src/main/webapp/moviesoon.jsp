<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>Beta Cinemas - 
        <c:choose>
            <c:when test="${param.action == 'now_showing'}">Phim Đang Chiếu</c:when>
            <c:otherwise>Phim Sắp Chiếu</c:otherwise>
        </c:choose>
    </title>

    <c:if test="${empty requestScope.movieList}">
        <meta http-equiv="refresh" content="0; url=${pageContext.request.contextPath}/movie?action=coming_soon">
    </c:if>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"> 
</head>
<body>

<header class="header">
    <div class="logo-container">
        <img src="${pageContext.request.contextPath}/images/meme.png" alt="Beta Cinemas Logo" class="logo">
        <span class="cinema-location">Beta Thái Nguyên</span>
    </div>

    <nav class="main-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/showtime" class="tab-item">PHIM</a></li>
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
        <a href="${pageContext.request.contextPath}/movie?action=coming_soon" 
           class="tab-item ${param.action == 'now_showing' ? '' : 'active'}">
           PHIM SẮP CHIẾU
        </a> 
        
        <a href="${pageContext.request.contextPath}/movie?action=now_showing" 
           class="tab-item ${param.action == 'now_showing' ? 'active' : ''}">
           PHIM ĐANG CHIẾU
        </a> 
        
        <a href="${pageContext.request.contextPath}/movie?action=special_show" 
           class="tab-item ${param.action == 'special_show' ? 'active' : ''}">
           SUẤT CHIẾU ĐẶC BIỆT
        </a>
    </div>
</div>

<main class="main-content">
    <div class="movie-list">
        <c:forEach var="movie" items="${requestScope.movieList}">
            <div class="movie-card">

                <div class="movie-image-wrapper">

                    <c:set var="poster" value="${fn:trim(movie.posterUrl)}" />

                    <c:choose>
                        <c:when test="${not empty poster}">
                            <img
                                src="${pageContext.request.contextPath}/images/movies/${fn:replace(poster, '/images/', '')}"
                                alt="${movie.title}"
                                class="movie-poster">
                        </c:when>
                        <c:otherwise>
                            <img
                                src="${pageContext.request.contextPath}/images/movies/meme.jpg"
                                alt="No image"
                                class="movie-poster">
                        </c:otherwise>
                    </c:choose>

                    <c:if test="${movie.status eq 'Now Showing' or movie.status eq 'HOT'}">
                        <span class="badge hot">HOT</span>
                    </c:if>

                    <span class="badge rating-c18">C18</span> 
                </div>

                <h3 class="movie-title">${movie.title}</h3>
                <p class="movie-info">Thể loại: ${movie.genre}</p>
                <p class="movie-info">Thời lượng: ${movie.duration} phút</p>

                <button class="buy-ticket-btn">MUA VÉ</button>
            </div>
        </c:forEach>
        
        <c:if test="${empty requestScope.movieList}">
            <p style="text-align: center; width: 100%; margin-top: 50px;">
                Hiện tại chưa có phim nào.
            </p>
        </c:if>
    </div>
</main>
</body>
</html>
