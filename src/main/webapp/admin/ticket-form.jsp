<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/ticket/ticket-form.css?v=1">
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<div class="cinema-form-page centered">

    <a href="${pageContext.request.contextPath}/admin/ticket?action=list"
       class="back-btn">
        <i class="fas fa-arrow-left"></i> Back to Ticket List
    </a>

    <h1 class="cinema-form-title">
        <c:choose>
            <c:when test="${ticket != null}">Edit Ticket</c:when>
            <c:otherwise>Book Ticket</c:otherwise>
        </c:choose>
    </h1>

    <form method="post"
          action="${pageContext.request.contextPath}/admin/ticket">

        <!-- ticketId (edit only) -->
        <c:if test="${ticket != null}">
            <input type="hidden" name="ticketId"
                   value="${ticket.ticketId}">
        </c:if>

        <!-- User -->
        <div class="form-group">
            <label>User ID</label>
            <input type="number"
                   name="userId"
                   value="${ticket != null ? ticket.userId : ''}"
                   placeholder="User ID"
                   required>
        </div>

        <!-- Showtime -->
        <div class="form-group">
            <label>Showtime ID</label>
            <input type="number"
                   name="showtimeId"
                   value="${ticket != null ? ticket.showtimeId : ''}"
                   placeholder="Showtime ID"
                   required>
        </div>

        <!-- Seat -->
        <div class="form-group">
            <label>Seat ID</label>
            <input type="number"
                   name="seatId"
                   value="${ticket != null ? ticket.seatId : ''}"
                   placeholder="Seat ID"
                   required>
        </div>

        <!-- Price -->
        <div class="form-group">
            <label>Price</label>
            <input type="number"
                   step="0.01"
                   name="price"
                   value="${ticket != null ? ticket.price : ''}"
                   placeholder="Example: 75000"
                   required>
        </div>

        <!-- Booking Time -->
        <div class="form-group">
            <label>Booking Time</label>
            <input type="datetime-local"
                   name="bookingTime"
                   value="${ticket != null ? ticket.bookingTime : ''}"
                   required>
        </div>

        <!-- Status -->
        <div class="form-group">
            <label>Status</label>
            <select name="status" required>
                <option value="">-- Select Status --</option>
                <option value="Booked"
                    ${ticket != null && ticket.status == 'Booked' ? 'selected' : ''}>
                    Booked
                </option>
                <option value="Cancelled"
                    ${ticket != null && ticket.status == 'Cancelled' ? 'selected' : ''}>
                    Cancelled
                </option>
                <option value="Used"
                    ${ticket != null && ticket.status == 'Used' ? 'selected' : ''}>
                    Used
                </option>
            </select>
        </div>

        <button type="submit" class="submit-btn">
            <c:choose>
                <c:when test="${ticket != null}">Save Changes</c:when>
                <c:otherwise>Book Ticket</c:otherwise>
            </c:choose>
        </button>

    </form>
</div>
