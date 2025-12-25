<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/room/room-form.css">

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
            <label>From Row</label>
            <input type="text" name="fromRow" placeholder="A" required>
        </div>

        <div class="form-group">
            <label>To Row</label>
            <input type="text" name="toRow" placeholder="H" required>
        </div>

        <div class="form-group">
            <label>Seats per Row</label>
            <input type="number" name="seatPerRow" min="1" required>
        </div>

        <div class="form-group">
            <label>Default Seat Type</label>
            <select name="seatType" required>
                <option value="Regular">Regular</option>
                <option value="VIP">VIP</option>
                <option value="Double">Double</option>
            </select>
        </div>

        <button type="submit" class="submit-btn">
            Generate Seats
        </button>

    </form>

</div>