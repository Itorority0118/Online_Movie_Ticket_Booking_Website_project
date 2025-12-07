<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/movie/movie-list.css?v=2">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
<div class="movie-management-header">
    <h1>Movie Management</h1>
    <form action="${pageContext.request.contextPath}/movie" method="get" class="movie-search-form">
        <input type="hidden" name="action" value="list" />
        <input type="text" name="keyword" placeholder="Search movie..." value="${param.keyword != null ? param.keyword : ''}" />
        <button type="submit"><i class="fa fa-search"></i></button>
    </form>
</div>

<a href="${pageContext.request.contextPath}/movie?action=new" class="add-movie-btn">+ Add Movie</a>
	<!-- Movie Cards -->
	<div class="movie-cards-wrapper" id="movieCardsWrapper">
		<c:forEach var="m" items="${movies}">
			<div class="movie-card" data-id="${m.movieId}">
				<div class="movie-poster"
					onclick="window.open('${m.trailerUrl}', '_blank')">
					<img
						src="${pageContext.request.contextPath}/images/movies/${m.posterUrl}"
						alt="${m.title}" />
				</div>
				<div class="movie-info">
					<h3 class="movie-title">${m.title}</h3>
					<p>
						<strong>Genre:</strong> ${m.genre}
					</p>
					<p>
						<strong>Director:</strong> ${m.director}
					</p>
					<p>
						<strong>Duration:</strong> ${m.duration} min
					</p>
					<p>
						<strong>Status:</strong> ${m.status}
					</p>
				</div>
				<div class="movie-actions">
					<a class="action edit"
						href="${pageContext.request.contextPath}/movie?action=edit&id=${m.movieId}">Edit</a>
					<a class="action delete" href="#" data-id="${m.movieId}" data-type="movie">Delete</a>
					<a class="action trailer" href="${m.trailerUrl}" target="_blank"
						title="Watch Trailer"> <i class="fab fa-youtube"></i>
					</a>
				</div>
			</div>
		</c:forEach>
	</div>

	<div class="pagination">
	    <c:forEach var="i" begin="1" end="${totalPages}">
	        <a class="page-link"
	           href="${pageContext.request.contextPath}/movie?action=list&page=${i}&keyword=${param.keyword != null ? param.keyword : ''}">
	           ${i}
	        </a>
	    </c:forEach>
	</div>

	<!-- DELETE MODAL -->
	<div id="deleteModal" class="modal">
		<div class="modal-content">
			<h3>Are you sure?</h3>
			<p>This action cannot be undone.</p>
			<div class="modal-buttons">
				<button type="button" class="btn cancel">Cancel</button>
				<button type="button" id="confirmDeleteBtn" class="btn delete">Delete</button>
			</div>
		</div>
	</div>
	<!-- contextPath JS -->
<script>
    window.movieContext = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/movie/movie-list.js"></script>
</div>