<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome</title>
</head>
<body>

    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <h1>Welcome, ${sessionScope.user.fullName}!</h1>
        </c:when>

        <c:otherwise>
            <h1>Welcome!</h1>
            <p>You are not logged in.</p>
        </c:otherwise>
    </c:choose>

</body>
</html>
