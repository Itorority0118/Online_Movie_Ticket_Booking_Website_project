<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="user-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Movie</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Ticket Price</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="s" items="${showtimes}">
            <tr data-id="${s.showtimeId}">
                <td>${s.showtimeId}</td>
                <td>${movieMap[s.movieId]}</td>
                <td>${s.startTime}</td>
                <td>${s.endTime}</td>
                <td>${s.ticketPrice}</td>
                <td>
                    <a class="action edit"
                       href="${pageContext.request.contextPath}/showtime?role=admin&action=edit&id=${s.showtimeId}&roomId=${param.roomId}">
                        Edit
                    </a> |
                    <a class="action delete"
                       href="#"
                       data-id="${s.showtimeId}"
                       data-type="showtime">
                        Delete
                    </a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty showtimes}">
            <tr>
                <td colspan="6" style="text-align:center;">
                    No showtimes found for this room
                </td>
            </tr>
        </c:if>
    </tbody>
</table>
