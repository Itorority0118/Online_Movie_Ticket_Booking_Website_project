<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Beta Cinemas - Lịch chiếu</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/showtimes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css?v=101">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css?v=16">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css?v=1">
    <c:if test="${movieList == null}">
        <meta http-equiv="refresh"
              content="0;url=${pageContext.request.contextPath}/movie?action=now_showing">
    </c:if>
</head>

<body>
<jsp:include page="header.jsp"/>

<div class="movie-tabs-container">
    <div class="movie-tabs">
        <a href="${pageContext.request.contextPath}/movie?action=coming_soon"
           class="tab-item ${param.action != 'now_showing' && param.action != 'special_show' ? '' : ''}">
           PHIM SẮP CHIẾU
        </a>

        <a href="${pageContext.request.contextPath}/movie?action=now_showing"
           class="tab-item ${param.action == 'now_showing'  && param.action != 'special_show' ? '' : ''}">
           PHIM ĐANG CHIẾU
        </a>

    </div>
</div>

<!-- ================== BỘ LỌC PHIM ================== -->
<div class="filter-container">
    <form action="${pageContext.request.contextPath}/showtime" method="get" class="filter-form">
        <input type="hidden" name="action" value="search"/>

        <!-- Thành phố -->
        <select name="city" onchange="this.form.submit()">
            <option value="">Chọn thành phố</option>
            <c:forEach var="city" items="${cityList}">
                <option value="${city}" ${param.city == city ? 'selected' : ''}>
                    ${city}
                </option>
            </c:forEach>
        </select>

        <!-- Rạp -->
        <select name="cinemaId">
            <option value="">Chọn rạp</option>
            <c:forEach var="cinema" items="${cinemaList}">
                <option value="${cinema.cinemaId}"
                        ${param.cinemaId == cinema.cinemaId ? 'selected' : ''}>
                    ${cinema.name}
                </option>
            </c:forEach>
        </select>

        <!-- Ngày -->
        <input type="date" name="date" value="${param.date}"/>

        <!-- Thể loại -->
        <select name="genre">
            <option value="">Tất cả thể loại</option>
            <c:forEach var="g" items="${genreList}">
                <option value="${g}" ${param.genre == g ? 'selected' : ''}>
                    ${g}
                </option>
            </c:forEach>
        </select>

        <!-- Độ tuổi -->
        <select name="ageRating">
            <option value="">Tất cả độ tuổi</option>
            <c:forEach var="r" items="${ageRatingList}">
                <option value="${r}" ${param.ageRating == r ? 'selected' : ''}>
                    ${r}
                </option>
            </c:forEach>
        </select>

        <button type="submit" class="search-btn">TÌM KIẾM</button>
    </form>
</div>

<!-- ================== DANH SÁCH PHIM ================== -->
<main class="main-content">
    <div class="movie-list">

        <c:forEach var="movie" items="${movieList}">
            <div class="movie-card">

                <div class="movie-image-wrapper"
				     onclick="openMovieModal(
				        '${fn:escapeXml(movie.title)}',
				        '${movie.genre}',
				        '${movie.duration}',
				        '${fn:escapeXml(movie.description)}',
				        '${pageContext.request.contextPath}/images/movies/${fn:replace(movie.posterUrl,'/images/','')}',
				        '${movie.trailerUrl}',
				        '${movie.movieId}'
				     )">
                    <c:set var="poster" value="${fn:trim(movie.posterUrl)}"/>
                    <img class="movie-poster"
                         src="${pageContext.request.contextPath}/images/movies/${fn:replace(poster,'/images/','')}"
                         alt="${movie.title}"/>
                </div>

                <h3 class="movie-title">${movie.title}</h3>
                <p class="movie-info">Thể loại: ${movie.genre}</p>
                <p class="movie-info">Thời lượng: ${movie.duration} phút</p>

                <button class="buy-ticket-btn"
				    onclick="openMovieModal(
				        '${fn:escapeXml(movie.title)}',
				        '${movie.genre}',
				        '${movie.duration}',
				        '${fn:escapeXml(movie.description)}',
				        '${pageContext.request.contextPath}/images/movies/${fn:replace(movie.posterUrl,'/images/','')}',
				        '${movie.trailerUrl}',
				        '${movie.movieId}',
				        true
				    )">
				    MUA VÉ
				</button>

            </div>
        </c:forEach>

        <!-- Không có phim -->
        <c:if test="${empty movieList}">
            <p style="text-align:center; margin-top:40px">
                Không tìm thấy phim phù hợp
            </p>
        </c:if>

    </div>
</main>
<jsp:include page="order-success-modal.jsp"/>
<jsp:include page="profile-modal.jsp"/>
<jsp:include page="order-modal.jsp"/>
<jsp:include page="movie-modal.jsp"/>
<jsp:include page="footer.jsp" />
<script>
    window.IS_LOGGED_IN = ${sessionScope.user != null};
</script>
<script>
    window.APP_CONTEXT = "${pageContext.request.contextPath}";
</script>
<script src="<c:url value='/js/movie.js'/>"></script>
<script src="<c:url value='/js/order-modal.js?v=10'/>"></script>
<script src="<c:url value='/js/common.js'/>"></script>
</body>
</html>
