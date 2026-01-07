<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Registration</title>
    <link rel="stylesheet" href="<c:url value='/css/register.css?v=2'/>">
</head>
<body>

<div class="container">
    <h2>Register</h2>

    <!-- error chung -->
    <c:if test="${not empty errors.general}">
        <div class="error">${errors.general}</div>
    </c:if>

    <form action="user" method="post">
        <input type="hidden" name="action" value="register">

        <div class="form-group">
            <label>Full Name:</label>
            <input type="text" name="fullName" value="${param.fullName}">
            <c:if test="${not empty errors.fullName}">
                <small class="error field-error">${errors.fullName}</small>
            </c:if>
        </div>

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

        <div class="form-group">
            <label>Phone (optional):</label>
            <input type="text" name="phone" value="${param.phone}">
            <c:if test="${not empty errors.phone}">
                <small class="error field-error">${errors.phone}</small>
            </c:if>
        </div>

        <button type="submit">Register</button>
    </form>

    <p style="margin-top: 15px">
        Already have an account?
        <a href="login.jsp">Login here</a>
    </p>
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