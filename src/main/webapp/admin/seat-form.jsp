<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/cinema/cinema-form.css?v=1">

<div class="cinema-form-page centered">
    <a href="${pageContext.request.contextPath}/seat?action=list&roomId=${seat != null ? seat.roomId : param.roomId}"
       class="back-btn">
        <i class="fas fa-arrow-left"></i> Back to Seat List
    </a>

    <h1 class="cinema-form-title">
        <c:choose>
            <c:when test="${seat != null}">Edit Seat</c:when>
            <c:otherwise>Add Seat</c:otherwise>
        </c:choose>
    </h1>

    <form method="post" action="${pageContext.request.contextPath}/seat">
        <c:if test="${seat != null}">
            <input type="hidden" name="id" value="${seat.seatId}">
        </c:if>
        <input type="hidden" name="roomId"
               value="${seat != null ? seat.roomId : (param.roomId != null ? param.roomId : 0)}">
        <input type="hidden" name="seatRow" value="${seat != null ? seat.seatRow : ''}">
        <input type="hidden" name="seatCol" value="${seat != null ? seat.seatCol : ''}">

        <div class="form-group">
            <label>Seat Number</label>
            <input type="text" name="seatNumber"
                   value="${seat != null ? seat.seatNumber : ''}" readonly
                   placeholder="Example: A1" required>
        </div>

        <div class="form-group">
            <label>Seat Type</label>
            <select name="seatType" required>
                <option value="">-- Select Type --</option>
                <option value="Regular" ${seat != null && seat.seatType == 'Regular' ? 'selected' : ''}>Regular</option>
                <option value="VIP" ${seat != null && seat.seatType == 'VIP' ? 'selected' : ''}>VIP</option>
                <option value="Double" ${seat != null && seat.seatType == 'Double' ? 'selected' : ''}>Double</option>
            </select>
        </div>

        <div class="form-group">
            <label>Status</label>
            <select name="status" required>
                <option value="">-- Select Status --</option>
                <option value="Available" ${seat != null && seat.status == 'Available' ? 'selected' : ''}>Available</option>
                <option value="Booked" ${seat != null && seat.status == 'Booked' ? 'selected' : ''}>Booked</option>
                <option value="Unavailable" ${seat != null && seat.status == 'Unavailable' ? 'selected' : ''}>Unavailable</option>
            </select>
        </div>

        <c:if test="${param.roomId != null || seat != null}">
            <button type="submit" class="submit-btn">Save changes</button>
        </c:if>
    </form>
</div>
