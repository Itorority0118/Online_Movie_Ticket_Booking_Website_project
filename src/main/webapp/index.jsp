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
            <c:when test="${param.action == 'special_show'}">Suất Chiếu Đặc Biệt</c:when>
            <c:otherwise>Phim Sắp Chiếu</c:otherwise>
        </c:choose>
    </title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css?v=101">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css?v=13">
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
           class="tab-item ${param.action != 'now_showing' && param.action != 'special_show' ? 'active' : ''}">
           PHIM SẮP CHIẾU
        </a>

        <a href="${pageContext.request.contextPath}/movie?action=now_showing"
           class="tab-item ${param.action == 'now_showing' ? 'active' : ''}">
           PHIM ĐANG CHIẾU
        </a>

    </div>
</div>

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
			        <img src="${pageContext.request.contextPath}/images/movies/${fn:replace(movie.posterUrl,'/images/','')}"
			             class="movie-poster">
			    </div>
			
			    <h3>${movie.title}</h3>
			    <p>Thể loại: ${movie.genre}</p>
			    <p>Thời lượng: ${movie.duration} phút</p>
			
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

        <c:if test="${not empty movieList && movieList.size() == 0}">
            <p style="text-align:center; margin-top:50px;">
                Hiện tại chưa có phim nào trong mục này.
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
<script src="<c:url value='/js/common.js?v=8'/>"></script>
<script src="<c:url value='/js/movie.js'/>"></script>
<script src="<c:url value='/js/order-modal.js?v=10'/>"></script>
</body>
</html>