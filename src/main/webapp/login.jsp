<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<c:url value='/css/login.css?v=2'/>">
</head>
<body>

<c:if test="${not empty sessionScope.user and not empty param.redirect}">
    <c:redirect url="${param.redirect}" />
</c:if>

<div class="login-container">

    <h2>Login</h2>

    <c:if test="${not empty errors.general}">
        <div class="error">${errors.general}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/user" method="post">
        <input type="hidden" name="action" value="login">

        <div class="form-group">
            <label>Email:</label>
            <input type="text" name="email" value="${param.email}">
			<c:if test="${not empty errors.email}">
			    <small class="error field-error">${errors.email}</small>
			</c:if>
        </div>

        <div class="form-group">
            <label>Password:</label>
            <input type="password" name="password">
			<c:if test="${not empty errors.password}">
			    <small class="error field-error">${errors.password}</small>
			</c:if>
        </div>

        <button type="submit" class="btn">Login</button>

        <div class="register-link">
            <a href="user?action=register">Create an account</a>
            <a href="user?action=forgot">Forgot Password?</a>
        </div>
    </form>
</div>
<script>
    setTimeout(() => {
        document.querySelectorAll('.field-error').forEach(err => {
            err.style.transition = 'opacity 0.4s ease';
            err.style.opacity = '0';
            setTimeout(() => err.remove(), 400);
        });
    }, 3000);
</script>
</body>
</html>