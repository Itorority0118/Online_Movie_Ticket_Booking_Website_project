<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie/movie-form.css?v=1">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<div class="movie-form-page centered">

    <a href="${pageContext.request.contextPath}/movie?action=list" class="back-btn">
        <i class="fas fa-arrow-left"></i> Back to Movie List
    </a>

    <div class="movie-form-title">
        <h1>
            <c:choose>
                <c:when test="${movie == null || movie.movieId == 0}">
                    Add Movie
                </c:when>
                <c:otherwise>
                    Edit Movie
                </c:otherwise>
            </c:choose>
        </h1>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/movie">

        <c:if test="${movie != null && movie.movieId > 0}">
            <input type="hidden" name="id" value="${movie.movieId}" />
        </c:if>

		        <!-- Title -->
		<div class="form-group">
		    <label>Title <span class="required">*</span></label>
		    <input type="text" name="title"
		           class="${errors.title != null ? 'error-input' : ''}"
		           value="${movie != null ? movie.title : ''}">
		    <c:if test="${errors.title != null}">
		        <span class="error">${errors.title}</span>
		    </c:if>
		</div>
		
		<!-- Genre -->
		<div class="form-group">
		    <label>Genre <span class="required">*</span></label>
		    <input type="text" name="genre"
		           class="${errors.genre != null ? 'error-input' : ''}"
		           value="${movie != null ? movie.genre : ''}">
		    <c:if test="${errors.genre != null}">
		        <span class="error">${errors.genre}</span>
		    </c:if>
		</div>
		
		<!-- Director -->
		<div class="form-group">
		    <label>Director <span class="required">*</span></label>
		    <input type="text" name="director"
		           class="${errors.director != null ? 'error-input' : ''}"
		           value="${movie != null ? movie.director : ''}">
		    <c:if test="${errors.director != null}">
		        <span class="error">${errors.director}</span>
		    </c:if>
		</div>
		
		<!-- Cast -->
		<div class="form-group">
		    <label>Cast <span class="required">*</span></label>
		    <input type="text" name="cast"
		           class="${errors.cast != null ? 'error-input' : ''}"
		           value="${movie != null ? movie.cast : ''}">
		    <c:if test="${errors.cast != null}">
		        <span class="error">${errors.cast}</span>
		    </c:if>
		</div>
		
		<!-- Description -->
		<div class="form-group">
		    <label>Description <span class="required">*</span></label>
		    <textarea name="description" rows="4"
		              class="${errors.description != null ? 'error-input' : ''}">${movie != null ? movie.description : ''}</textarea>
		    <c:if test="${errors.description != null}">
		        <span class="error">${errors.description}</span>
		    </c:if>
		</div>
		
		<!-- Duration -->
		<div class="form-group">
		    <label>Duration (minutes) <span class="required">*</span></label>
		    <input type="number" name="duration" min="1"
		           class="${errors.duration != null ? 'error-input' : ''}"
		           value="${movie != null ? movie.duration : ''}">
		    <c:if test="${errors.duration != null}">
		        <span class="error">${errors.duration}</span>
		    </c:if>
		</div>
		
		<!-- Language -->
		<div class="form-group">
		    <label>Language <span class="required">*</span></label>
		    <input type="text" name="language"
		           class="${errors.language != null ? 'error-input' : ''}"
		           value="${movie != null ? movie.language : ''}">
		    <c:if test="${errors.language != null}">
		        <span class="error">${errors.language}</span>
		    </c:if>
		</div>
		
		<!-- Release Date -->
		<div class="form-group">
		    <label>Release Date <span class="required">*</span></label>
		    <input type="date" name="releaseDate"
		           class="${errors.releaseDate != null ? 'error-input' : ''}"
		           value="${movie != null ? movie.releaseDate : ''}">
		    <c:if test="${errors.releaseDate != null}">
		        <span class="error">${errors.releaseDate}</span>
		    </c:if>
		</div>
		
		<!-- Poster Upload -->
		<div class="form-group">
		    <label>Poster <span class="required">*</span></label>
		    
		    <label for="posterFile" class="upload-btn">
		        <i class="fas fa-upload"></i> Choose Poster
		    </label>
		    <input type="file" id="posterFile" style="display:none;">
		    
		    <c:choose>
		        <c:when test="${movie != null && fn:length(movie.posterUrl) > 0}">
		            <img id="posterPreview"
		                 src="${pageContext.request.contextPath}/images/movies/${movie.posterUrl}"
		                 alt="Poster Preview"
		                 style="max-width:150px; display:block; margin-top:10px;">
		        </c:when>
		        <c:otherwise>
		            <img id="posterPreview"
		                 src=""
		                 alt="Poster Preview"
		                 style="max-width:150px; display:block; margin-top:10px;">
		        </c:otherwise>
		    </c:choose>
		
		    <input type="hidden" name="posterUrl" value="${movie != null ? movie.posterUrl : ''}">
		</div>

		<!-- Trailer (URL or Upload) -->
		<div class="form-group">
		    <label>Trailer <span class="required">*</span></label>
		
		    <!-- Text input -->
		    <input type="text" name="trailerUrl" id="trailerUrlInput"
		           class="${errors.trailerUrl != null ? 'error-input' : ''}"
		           placeholder="Enter trailer URL"
		           value="${movie != null ? movie.trailerUrl : ''}">
		
		    <!-- Upload button -->
		    <label for="trailerFile" class="upload-btn" style="margin-top:8px;">
		        <i class="fas fa-upload"></i> Upload Trailer File
		    </label>
		    <input type="file" id="trailerFile" style="display:none;" accept="video/*">
		
		    <c:if test="${errors.trailerUrl != null}">
		        <span class="error">${errors.trailerUrl}</span>
		    </c:if>
		</div>
		
		<!-- Status -->
		<div class="form-group">
		    <label>Status <span class="required">*</span></label>
		    <select name="status" class="${errors.status != null ? 'error-input' : ''}">
		        <option value="Now Showing" ${movie != null && movie.status == 'Now Showing' ? 'selected' : ''}>Now Showing</option>
		        <option value="Coming Soon" ${movie != null && movie.status == 'Coming Soon' ? 'selected' : ''}>Coming Soon</option>
		        <option value="Archived" ${movie != null && movie.status == 'Archived' ? 'selected' : ''}>Archived</option>
		    </select>
		    <c:if test="${errors.status != null}">
		        <span class="error">${errors.status}</span>
		    </c:if>
		</div>


        <button type="submit" class="submit-btn">
            <c:choose>
                <c:when test="${movie == null || movie.movieId == 0}">
                    Create Movie
                </c:when>
                <c:otherwise>
                    Save Changes
                </c:otherwise>
            </c:choose>
        </button>
    </form>
</div>

<script>
const contextPath = '${pageContext.request.contextPath}';

document.getElementById('posterFile').addEventListener('change', function() {
    const file = this.files[0];
    if(!file) return;

    const formData = new FormData();
    formData.append('posterFile', file);

    fetch(contextPath + '/uploadPoster', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if(data.success){
            const fileName = data.fileUrl.substring(data.fileUrl.lastIndexOf('/') + 1);
            document.getElementById('posterPreview').src = contextPath + '/images/movies/' + fileName;
            document.querySelector('input[name="posterUrl"]').value = fileName;
        } else {
            alert(data.message);
        }
    })
    .catch(err => console.error(err));
});

//Trailer file upload
const trailerFile = document.getElementById('trailerFile');
const trailerInput = document.getElementById('trailerUrlInput');

trailerFile.addEventListener('change', function() {
    const file = this.files[0];
    if(!file) return;

    const formData = new FormData();
    formData.append('trailerFile', file);

    fetch(contextPath + '/uploadTrailer', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if(data.success){
            trailerInput.value = data.fileUrl;
        } else {
            alert(data.message);
        }
    })
    .catch(err => console.error(err));
});

trailerInput.addEventListener('input', function() {
    if(this.value.trim() !== ''){
        trailerFile.value = '';
    }
});
</script>