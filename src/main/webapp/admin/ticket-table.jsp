<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="user-table ticket-table">
    <thead>
        <tr>
            <th class="col-id">ID</th>
            <th class="col-user">User</th>
            <th class="col-showtime">ShowTime</th>
            <th class="col-seat">Seat</th>
            <th class="col-price">Price</th>
            <th class="col-time">Booking Time</th>
            <th class="col-status">Status</th>
            <th class="col-action">Action</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="t" items="${tickets}">
            <tr data-id="${t.ticketId}">
                <td class="col-id">${t.ticketId}</td>
                <td class="col-user">${t.userId}</td>
                <td class="col-showtime">${t.showtimeId}</td>
                <td class="col-seat">${t.seatId}</td>
                <td class="col-price">${t.price}</td>
                <td class="col-time">${t.bookingTime}</td>
                <td class="col-status">
                    <span class="status ${t.status}">
                        ${t.status}
                    </span>
                </td>
                <td class="col-action">
	
				    <c:if test="${t.status eq 'Booked'}">
				        <a class="action cancel"
				           href="#"
				           data-id="${t.ticketId}">
				            Cancel
				        </a>
				    </c:if>
				
				    <c:if test="${t.status eq 'Cancelled'}">
				        <a class="action delete"
				           href="#"
				           data-id="${t.ticketId}"
				           data-type="ticket">
				            Delete
				        </a>
				    </c:if>
				
				    <c:if test="${t.status eq 'Used'}">
				        <span style="color:#999;">—</span>
				    </c:if>

				</td>

            </tr>
        </c:forEach>

        <c:if test="${empty tickets}">
            <tr>
                <td colspan="8" style="text-align:center;">
                    No tickets found
                </td>
            </tr>
        </c:if>
    </tbody>
</table>
