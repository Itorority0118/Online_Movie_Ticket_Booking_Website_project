<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Beta Cinemas - Lọc Phim</title>

    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="css/showtimes.css">

    <style>
        /* ===== GENRE STYLE ===== */
        .genre-box {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            align-items: center;
        }

        .filter-label {
            color: #fff;
            font-weight: 600;
            margin-right: 10px;
        }

        .genre-item {
            background-color: #111;
            border: 1px solid #333;
            border-radius: 20px;
            padding: 6px 14px;
            font-size: 13px;
            color: #ccc;
            cursor: pointer;
            transition: 0.2s ease;
            user-select: none;
        }

        .genre-item:hover {
            border-color: #e50914;
            color: #fff;
        }

        .genre-item.active {
            background: #e50914;
            border-color: #e50914;
            color: white;
            box-shadow: 0 0 10px rgba(229,9,20,0.4);
        }
    </style>
</head>

<body>

<!-- ===== HEADER GIỮ NGUYÊN ===== -->
<header class="header">
    <div class="logo-container">
        <img src="${pageContext.request.contextPath}/images/movies/gengu1.jpg" class="logo">
        <span class="cinema-location">Beta Thái Nguyên</span>
    </div>

    <nav class="main-nav">
        <ul>
            <li><a href="showtimes.jsp">LỊCH CHIẾU THEO RẠP</a></li>
            <li><a href="movie.jsp" class="tab-item active">PHIM</a></li>
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
                <a href="${pageContext.request.contextPath}/user?action=logout">Đăng xuất</a>
            </c:when>
            <c:otherwise>
                <a href="login.jsp" class="login-btn">Đăng nhập</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>

<!-- ===== TABS ===== -->
<div class="movie-tabs-container">
    <div class="movie-tabs">
        <a href="moviesoon.jsp" class="tab-item">PHIM SẮP CHIẾU</a>
        <a href="index.jsp" class="tab-item">PHIM ĐANG CHIẾU</a>
        <a href="#" class="tab-item">SUẤT CHIẾU ĐẶC BIỆT</a>
    </div>
</div>

<!-- ===== FILTER ===== -->
<div class="filter-container">
    <form action="movie" method="get" class="filter-form">

        <!-- GENRE -->
        <div class="genre-box">
            <span class="filter-label">Thể loại:</span>

            <div class="genre-item" data-value="Action">Hành động</div>
            <div class="genre-item" data-value="Comedy">Hài</div>
            <div class="genre-item" data-value="Horror">Kinh dị</div>
            <div class="genre-item" data-value="Romance">Tình cảm</div>
            <div class="genre-item" data-value="Sci-Fi">Khoa học viễn tưởng</div>
            <div class="genre-item" data-value="Animation">Hoạt hình</div>
            <div class="genre-item" data-value="Adventure">Phiêu lưu</div>
            <div class="genre-item" data-value="Drama">Tâm lý</div>
            <div class="genre-item" data-value="Thriller">Giật gân</div>
            <div class="genre-item" data-value="Fantasy">Giả tưởng</div>
        </div>

        <input type="hidden" name="genres" id="genresInput">

        <!-- AGE -->
        <select name="age">
            <option value="">Độ tuổi</option>
            <option value="P">Mọi lứa tuổi</option>
            <option value="13">13+</option>
            <option value="16">16+</option>
            <option value="18">18+</option>
        </select>

        <!-- DURATION -->
        <select name="duration">
            <option value="">Thời lượng</option>
            <option value="90">Dưới 90 phút</option>
            <option value="120">Dưới 120 phút</option>
            <option value="150">Trên 120 phút</option>
        </select>

        <button type="submit">LỌC PHIM</button>
    </form>
</div>

<!-- ===== MOVIE LIST ===== -->
<div class="movie-list-container">

    <c:choose>
        <c:when test="${not empty movieList}">
            <div class="movie-grid">
                <c:forEach var="m" items="${movieList}">
                    <div class="movie-card">
                        <img src="${m.imageUrl}" class="movie-poster">

                        <div class="movie-info">
                            <h3 class="movie-title">${m.title}</h3>
                            <p>⏱ ${m.duration} phút</p>
                            <p>🎬 ${m.genre}</p>
                            <p>🔞 ${m.ageLimit}+</p>

                            <a href="moviedetail?id=${m.id}" class="movie-btn">
                                Chi tiết
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>

        <c:otherwise>
            <div class="no-movies-found">
                <p>Không tìm thấy phim phù hợp.</p>
            </div>
        </c:otherwise>
    </c:choose>

</div>

<script>
document.addEventListener("DOMContentLoaded", function () {
    const genreItems = document.querySelectorAll(".genre-item");
    const genresInput = document.getElementById("genresInput");
    let selectedGenres = [];

    genreItems.forEach(item => {
        item.addEventListener("click", () => {
            const value = item.getAttribute("data-value");

            if (selectedGenres.includes(value)) {
                selectedGenres = selectedGenres.filter(g => g !== value);
                item.classList.remove("active");
            } else {
                selectedGenres.push(value);
                item.classList.add("active");
            }

            genresInput.value = selectedGenres.join(",");
        });
    });
});
</script>
<script src="<c:url value='/js/common.js'/>"></script>
<script src="<c:url value='/js/movie/movie.js'/>"></script>
</body>
</html>
