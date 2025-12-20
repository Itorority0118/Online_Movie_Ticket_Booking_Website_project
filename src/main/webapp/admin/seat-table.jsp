<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="user-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Seat Number</th>
            <th>Seat Type</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="s" items="${seats}">
            <tr data-id="${s.seatId}">
                <td>${s.seatId}</td>
                <td>${s.seatNumber}</td>
                <td>${s.seatType}</td>
                <td>${s.status}</td>
                <td>
                    <a class="action edit"
                       href="${pageContext.request.contextPath}/seat?action=edit&id=${s.seatId}">
                        Edit
                    </a> |
                    <a class="action delete"
                       href="#"
                       data-id="${s.seatId}"
                       data-type="seat">
                        Delete
                    </a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty seats}">
            <tr>
                <td colspan="5" style="text-align:center;">
                    No seats found
                </td>
            </tr>
        </c:if>
    </tbody>
</table>
