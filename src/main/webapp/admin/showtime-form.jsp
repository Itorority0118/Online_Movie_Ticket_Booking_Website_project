<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/showtime/showtime-form.css?v=1">

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
            <label>Movie</label>
            <select name="movieId" required>
                <option value="">-- Select Movie --</option>
                <c:forEach var="m" items="${movieList}">
                    <option value="${m.movieId}" 
                        <c:if test="${showtime != null && showtime.movieId == m.movieId}">selected</c:if>>
                        ${m.title}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Start Time</label>
            <input type="datetime-local" name="startTime" 
                   value="${showtime != null ? showtime.startTime : ''}" required>
        </div>

        <div class="form-group">
            <label>End Time</label>
            <input type="datetime-local" name="endTime" 
                   value="${showtime != null ? showtime.endTime : ''}" required>
        </div>

        <div class="form-group">
            <label>Ticket Price</label>
            <input type="number" name="ticketPrice" step="0.01" 
                   value="${showtime != null ? showtime.ticketPrice : ''}" required>
        </div>

        <button type="submit" class="submit-btn">
            <c:choose>
                <c:when test="${showtime != null}">Save Changes</c:when>
                <c:otherwise>Create Showtime</c:otherwise>
            </c:choose>
        </button>

    </form>
</div>
