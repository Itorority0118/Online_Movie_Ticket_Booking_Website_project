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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css?v=13">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css?v=1">
    <script>
        function autoSubmitCity() {
            document.getElementById('showtime-form').submit();
        }
    </script>
</head>

<body>
<jsp:include page="header.jsp"/>
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
<script src="<c:url value='/js/common.js?v=8'/>"></script>
<script src="<c:url value='/js/movie.js'/>"></script>
<script src="<c:url value='/js/order-modal.js?v=10'/>"></script>
</body>
</html>