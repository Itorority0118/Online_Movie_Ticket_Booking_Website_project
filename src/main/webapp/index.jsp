<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Beta Cinemas - Phim Đang Chiếu</title>
    <link rel="stylesheet" href="css/index.css"> 
</head>
<body>

    <header class="header">
        <div class="logo-container">
            <img src="images/logo.png" alt="Beta Cinemas Logo" class="logo">
            <span class="cinema-location">Beta Thái Nguyên</span>
        </div>
        <nav class="main-nav">
            <ul>
                <li><a href="#">LỊCH CHIẾU THEO RẠP</a></li>
                <li><a href="#">PHIM</a></li>
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
            <span>Xin chào, **${sessionScope.user.fullName}**</span>
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
            <a href="#" class="tab-item">PHIM SẮP CHIẾU</a>
            <a href="#" class="tab-item active">PHIM ĐANG CHIẾU</a>
            <a href="#" class="tab-item">SUẤT CHIẾU ĐẶC BIỆT</a>
        </div>
    </div>
    
    <main class="main-content">
        <div class="movie-list">
            
            <div class="movie-card">
                <div class="movie-image-wrapper">
                    <img src="images/mortal-kombat.jpg" alt="Mortal Kombat: Cuộc Chiến Sinh Tử" class="movie-poster">
                    <span class="badge hot">HOT</span>
                    <span class="badge rating-c18">C18</span>
                </div>
                <h3 class="movie-title">Mortal Kombat: Cuộc Chiến Sinh Tử</h3>
                <p class="movie-info">Thể loại: Hành động</p>
                <p class="movie-info">Thời lượng: 110 phút</p>
                <button class="buy-ticket-btn">MUA VÉ</button>
            </div>
            
            <div class="movie-card">
                <div class="movie-image-wrapper">
                    <img src="images/an-quy.jpg" alt="Ản Quỷ" class="movie-poster">
                    <span class="badge hot">HOT</span>
                    <span class="badge rating-c18">C18</span>
                </div>
                <h3 class="movie-title">Ản Quỷ</h3>
                <p class="movie-info">Thể loại: Kinh dị</p>
                <p class="movie-info">Thời lượng: 100 phút</p>
                <button class="buy-ticket-btn">MUA VÉ</button>
            </div>

            <div class="movie-card">
                <div class="movie-image-wrapper">
                    <img src="images/godzilla-kong.jpg" alt="Godzilla Đại Chiến Kong" class="movie-poster">
                    <span class="badge hot">HOT</span>
                    <span class="badge rating-c13">C13</span>
                </div>
                <h3 class="movie-title">Godzilla Đại Chiến Kong</h3>
                <p class="movie-info">Thể loại: Khoa học, viễn tưởng</p>
                <p class="movie-info">Thời lượng: 113 phút</p>
                <button class="buy-ticket-btn">MUA VÉ</button>
            </div>
            
            <div class="movie-card">
                <div class="movie-image-wrapper">
                    <img src="images/kieu.jpg" alt="Kiều" class="movie-poster">
                    <span class="badge hot">HOT</span>
                    <span class="badge rating-c18">C18</span>
                </div>
                <h3 class="movie-title">Kiều</h3>
                <p class="movie-info">Thể loại: Tâm lý</p>
                <p class="movie-info">Thời lượng: 95 phút</p>
                <button class="buy-ticket-btn">MUA VÉ</button>
            </div>

        </div>
    </main>
    
</body>
</html>
