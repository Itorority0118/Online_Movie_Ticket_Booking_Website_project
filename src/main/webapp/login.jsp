<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<c:url value='/css/login.css?v=1'/>">
</head>
<body>

<div class="login-container">

    <h2>Login</h2>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/user" method="post">
        <input type="hidden" name="action" value="login">

        <div class="form-group">
            <label>Email:</label>
            <input type="text" name="email" required value="${param.email}">
        </div>

        <div class="form-group">
            <label>Password:</label>
            <input type="password" name="password" required>
        </div>

        <button type="submit" class="btn">Login</button>

        <div class="register-link">
            <a href="user?action=register">Create an account</a>
            <a href="user?action=forgot">Forgot Password?</a>
        </div>
    </form>

</div>

</body>
</html>
