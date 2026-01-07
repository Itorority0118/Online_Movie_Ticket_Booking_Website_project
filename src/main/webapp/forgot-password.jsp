<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Forgot Password</title>
    <link rel="stylesheet" href="<c:url value='/css/forgot.css?v=2'/>">
</head>
<body>

<div class="container">

    <h2>Recover Password</h2>

    <!-- success message -->
    <c:if test="${not empty message}">
        <div class="success auto-hide">${message}</div>
    </c:if>

    <!-- error chung -->
    <c:if test="${not empty error}">
        <div class="error auto-hide">${error}</div>
    </c:if>

    <form action="user" method="post">
        <input type="hidden" name="action" value="forgot">

        <div class="form-group">
            <label>Enter your email:</label>
            <input type="email" name="email" value="${param.email}">
        </div>

        <button type="submit" class="btn">Reset Password</button>

        <div class="register-link">
            <a href="login.jsp">Back to Login</a>
        </div>
    </form>

</div>

<script>
    setTimeout(() => {
        document.querySelectorAll('.auto-hide').forEach(el => {
            el.style.transition = 'opacity 0.4s ease';
            el.style.opacity = '0';
            setTimeout(() => el.remove(), 400);
        });
    }, 3000);
</script>

</body>
</html>