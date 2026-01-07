<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/room/room-form.css?v=1">

<div class="room-form-page">

    <c:set var="roomId" value="${roomId}" />
    <c:set var="showtimeId" value="${showtimeId}" />

    <a href="<c:choose>
                <c:when test='${not empty roomId}'>
                    ${pageContext.request.contextPath}/seat?action=list&roomId=${roomId}
                </c:when>
                <c:otherwise>
                    ${pageContext.request.contextPath}/room
                </c:otherwise>
             </c:choose>"
       class="back-btn">
        ← Back to Seat Map
    </a>

    <h1>Generate Seat Layout</h1>

    <form method="post" action="${pageContext.request.contextPath}/seat">

        <input type="hidden" name="action" value="generate"/>
        <input type="hidden" name="roomId" value="${roomId}"/>
        <c:if test="${not empty showtimeId}">
            <input type="hidden" name="showtimeId" value="${showtimeId}"/>
        </c:if>

		<div class="form-group">
		    <label>From Row <span class="required">*</span></label>
		    <input type="text" name="fromRow"
		           value="${param.fromRow}"
		           class="${errors.fromRow != null || errors.rowOrder != null ? 'error-input' : ''}"
		           placeholder="A" required>
		
		    <c:if test="${errors.fromRow != null}">
		        <span class="error">${errors.fromRow}</span>
		    </c:if>
		</div>

		<div class="form-group">
		    <label>To Row <span class="required">*</span></label>
		    <input type="text" name="toRow"
		           value="${param.toRow}"
		           class="${errors.toRow != null || errors.rowOrder != null ? 'error-input' : ''}"
		           placeholder="H" required>
		
		    <c:if test="${errors.toRow != null}">
		        <span class="error">${errors.toRow}</span>
		    </c:if>
		</div>

		<c:if test="${errors.rowOrder != null}">
		    <span class="error">${errors.rowOrder}</span>
		</c:if>

		<div class="form-group">
		    <label>Seats per Row <span class="required">*</span></label>
		    <input type="number" name="seatPerRow"
		           value="${param.seatPerRow}"
		           class="${errors.seatPerRow != null ? 'error-input' : ''}"
		           min="1" required>
		
		    <c:if test="${errors.seatPerRow != null}">
		        <span class="error">${errors.seatPerRow}</span>
		    </c:if>
		</div>

		<div class="form-group">
		    <label>Default Seat Type <span class="required">*</span></label>
		    <select name="seatType"
		            class="${errors.seatType != null ? 'error-input' : ''}" required>
		        <option value="">-- Select Type --</option>
		        <option value="Regular" ${param.seatType == 'Regular' ? 'selected' : ''}>Regular</option>
		        <option value="VIP" ${param.seatType == 'VIP' ? 'selected' : ''}>VIP</option>
		        <option value="Double" ${param.seatType == 'Double' ? 'selected' : ''}>Double</option>
		    </select>
		
		    <c:if test="${errors.seatType != null}">
		        <span class="error">${errors.seatType}</span>
		    </c:if>
		</div>

        <button type="submit" class="submit-btn">
            Generate Seats
        </button>
    </form>
</div>