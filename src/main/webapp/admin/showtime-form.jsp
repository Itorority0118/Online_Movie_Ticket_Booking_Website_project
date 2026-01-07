<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/showtime/showtime-form.css?v=2">

<div class="showtime-form-page centered">

	<a href="${pageContext.request.contextPath}/showtime?action=listByRoom&role=admin&roomId=${showtime != null ? showtime.roomId : param.roomId}"
	   class="back-btn">
	    <i class="fas fa-arrow-left"></i> Back to Showtime List
	</a>

    <h1 class="cinema-form-title">
        <c:choose>
            <c:when test="${showtime != null}">Edit Showtime</c:when>
            <c:otherwise>Add Showtime</c:otherwise>
        </c:choose>
    </h1>

    <form method="post" action="${pageContext.request.contextPath}/showtime">

        <input type="hidden" name="role" value="admin" />

        <c:if test="${showtime != null}">
            <input type="hidden" name="id" value="${showtime.showtimeId}" />
        </c:if>

        <input type="hidden" name="roomId" value="${roomId}" />

		<div class="form-group">
		    <label>Movie <span class="required">*</span></label>
		    <select name="movieId" class="${errors.movieId != null ? 'error-input' : ''}" required>
		        <option value="">-- Select Movie --</option>
		        <c:forEach var="m" items="${movieList}">
		            <option value="${m.movieId}" 
		                <c:if test="${showtime != null && showtime.movieId == m.movieId}">selected</c:if>>
		                ${m.title}
		            </option>
		        </c:forEach>
		    </select>
		    <c:if test="${errors.movieId != null}">
		        <span class="error">${errors.movieId}</span>
		    </c:if>
		</div>
		
		<div class="form-group">
		    <label>Start Time <span class="required">*</span></label>
		    <input type="datetime-local" name="startTime"
		           value="${showtime != null ? showtime.startTime : ''}"
		           class="${errors.startTime != null ? 'error-input' : ''}" required>
		    <c:if test="${errors.startTime != null}">
		        <span class="error">${errors.startTime}</span>
		    </c:if>
		</div>
		
		<div class="form-group">
		    <label>End Time <span class="required">*</span></label>
		    <input type="datetime-local" name="endTime"
		           value="${showtime != null ? showtime.endTime : ''}"
		           class="${errors.endTime != null ? 'error-input' : ''}" required>
		    <c:if test="${errors.endTime != null}">
		        <span class="error">${errors.endTime}</span>
		    </c:if>
		</div>
		
		<div class="form-group">
		    <label>
		        Ticket Price (VND) <span class="required">*</span>
		    </label>
		
		    <div class="price-input-wrapper">
		        <input type="number"
		               name="ticketPrice"
		               min="1000"
		               step="1000"
		               placeholder="e.g. 50000"
		               value="${showtime != null ? showtime.ticketPrice : ''}"
		               class="${errors.ticketPrice != null ? 'error-input' : ''}"
		               required>
		
		        <span class="currency">VND</span>
		    </div>
		
		    <c:if test="${errors.ticketPrice != null}">
		        <span class="error">${errors.ticketPrice}</span>
		    </c:if>
		</div>

        <button type="submit" class="submit-btn">
            <c:choose>
                <c:when test="${showtime != null}">Save Changes</c:when>
                <c:otherwise>Create Showtime</c:otherwise>
            </c:choose>
        </button>

    </form>
</div>
