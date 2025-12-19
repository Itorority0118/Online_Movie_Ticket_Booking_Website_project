<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="user-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Cinema Name</th>
            <th>City</th>
            <th>Address</th>
            <th>Phone</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="c" items="${cinemas}">
            <tr data-id="${c.cinemaId}">
                <td>${c.cinemaId}</td>
                <td>${c.name}</td>
                <td>${c.city}</td>
                <td>${c.address}</td>
                <td>${c.phone}</td>
                <td>
                    <a class="action edit"
                       href="${pageContext.request.contextPath}/cinema?action=edit&id=${c.cinemaId}">
                        Edit
                    </a>
                    |
                    <a class="action delete"
                       href="#"
                       data-id="${c.cinemaId}"
                       data-type="cinema">
                        Delete
                    </a>
                    |
                    <a class="action"
                       href="${pageContext.request.contextPath}/room?action=list&cinemaId=${c.cinemaId}">
                        Room
                    </a>
                    |
                    <a class="action"
                       href="https://maps.google.com/?q=${c.address}"
                       target="_blank">
                        Map
                    </a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty cinemas}">
            <tr>
                <td colspan="6" style="text-align:center;">
                    No cinemas found
                </td>
            </tr>
        </c:if>
    </tbody>
</table>
