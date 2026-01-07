<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinema/cinema-form.css?v=1">

<div class="cinema-form-page centered">

	<a href="${pageContext.request.contextPath}/room?action=list&cinemaId=${room != null ? room.cinemaId : param.cinemaId}" class="back-btn">
	    <i class="fas fa-arrow-left"></i> Back to Room List
	</a>

    <h1 class="cinema-form-title">
        <c:choose>
            <c:when test="${room != null}">Edit Room</c:when>
            <c:otherwise>Add Room</c:otherwise>
        </c:choose>
    </h1>

    <form method="post" action="${pageContext.request.contextPath}/room">

        <c:if test="${room != null}">
            <input type="hidden" name="id" value="${room.roomId}">
        </c:if>

        <input type="hidden" name="cinemaId"
               value="${room != null ? room.cinemaId : param.cinemaId}">

		<div class="form-group">
		    <label>Room Name <span class="required">*</span></label>
		    <input type="text" name="roomName"
		           class="${errors.roomName != null ? 'error-input' : ''}"
		           value="${room != null ? room.roomName : ''}"
		           placeholder="Example: Room 1">
		    <c:if test="${errors.roomName != null}">
		        <span class="error">${errors.roomName}</span>
		    </c:if>
		</div>

		<div class="form-group">
		    <label>Room Type <span class="required">*</span></label>
		    <select name="roomType" class="${errors.roomType != null ? 'error-input' : ''}">
		        <option value="">-- Select Type --</option>
		        <option value="2D" ${room != null && room.roomType == '2D' ? 'selected' : ''}>2D</option>
		        <option value="3D" ${room != null && room.roomType == '3D' ? 'selected' : ''}>3D</option>
		        <option value="IMAX" ${room != null && room.roomType == 'IMAX' ? 'selected' : ''}>IMAX</option>
		    </select>
		    <c:if test="${errors.roomType != null}">
		        <span class="error">${errors.roomType}</span>
		    </c:if>
		</div>

        <button type="submit" class="submit-btn">
            <c:choose>
                <c:when test="${room != null}">Save Changes</c:when>
                <c:otherwise>Create Room</c:otherwise>
            </c:choose>
        </button>

    </form>
</div>
