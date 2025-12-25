<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="user-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Room Name</th>
            <th>Cinema ID</th>
            <th>Seat Count</th>
            <th>Room Type</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="r" items="${rooms}">
            <tr data-id="${r.roomId}">
                <td>${r.roomId}</td>
                <td>${r.roomName}</td>
                <td>${r.cinemaId}</td>
                <td>${r.seatCount}</td>
                <td>${r.roomType}</td>
                <td>
                    <a class="action edit"
                       href="${pageContext.request.contextPath}/room?action=edit&id=${r.roomId}">
                        Edit
                    </a> |
					<a class="action delete"
					   href="#"
					   data-id="${r.roomId}"
					   data-type="room">
					    Delete
					</a> |
                    <a class="action"
                       href="${pageContext.request.contextPath}/seat?action=list&roomId=${r.roomId}">
                        Seat
                    </a> |
					<a class="action"
					   href="${pageContext.request.contextPath}/showtime?role=admin&action=listByRoom&roomId=${r.roomId}&page=showtime-list.jsp">
					    Showtimes
					</a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty rooms}">
            <tr>
                <td colspan="6" style="text-align:center;">
                    No rooms found
                </td>
            </tr>
        </c:if>
    </tbody>
</table>