<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie/movie-form.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<div class="movie-form-page centered">
    <a href="${pageContext.request.contextPath}/movie?action=list" class="back-btn">
        <i class="fas fa-arrow-left"></i> Back to Movie List
    </a>
    <h1 class="movie-form-title">${movie == null ? "Add Movie" : "Edit Movie"}</h1>

    <form method="post" action="${pageContext.request.contextPath}/movie">

        <c:if test="${movie != null}">
            <input type="hidden" name="id" value="${movie.movieId}" />
        </c:if>

        <div class="form-group">
            <label>Title</label>
            <input type="text" name="title" value="${movie != null ? movie.title : ''}" required>
        </div>

        <div class="form-group">
            <label>Genre</label>
            <input type="text" name="genre" value="${movie != null ? movie.genre : ''}" required>
        </div>

        <div class="form-group">
            <label>Director</label>
            <input type="text" name="director" value="${movie != null ? movie.director : ''}" required>
        </div>

        <div class="form-group">
            <label>Cast</label>
            <input type="text" name="cast" value="${movie != null ? movie.cast : ''}" required>
        </div>

        <div class="form-group">
            <label>Description</label>
            <textarea name="description" rows="4">${movie != null ? movie.description : ''}</textarea>
        </div>

        <div class="form-group">
            <label>Duration (minutes)</label>
            <input type="number" name="duration" min="1" value="${movie != null ? movie.duration : ''}" required>
        </div>

        <div class="form-group">
            <label>Language</label>
            <input type="text" name="language" value="${movie != null ? movie.language : ''}" required>
        </div>

        <div class="form-group">
            <label>Release Date</label>
            <input type="date" name="releaseDate" value="${movie != null ? movie.releaseDate : ''}" required>
        </div>

        <div class="form-group">
            <label>Poster URL</label>
            <input type="text" name="posterUrl" value="${movie != null ? movie.posterUrl : ''}">
        </div>

        <div class="form-group">
            <label>Trailer URL</label>
            <input type="text" name="trailerUrl" value="${movie != null ? movie.trailerUrl : ''}">
        </div>

        <div class="form-group">
            <label>Status</label>
		<select name="status" required>
		    <option value="Now Showing" ${movie != null && movie.status == 'Now Showing' ? 'selected' : ''}>Now Showing</option>
		    <option value="Coming Soon" ${movie != null && movie.status == 'Coming Soon' ? 'selected' : ''}>Coming Soon</option>
		    <option value="Archived" ${movie != null && movie.status == 'Archived' ? 'selected' : ''}>Archived</option>
		</select>
        </div>

        <button type="submit" class="submit-btn">
            ${movie == null ? "Create Movie" : "Save Changes"}
        </button>

    </form>
</div>
