<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Forgot Password</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>

<div class="container">

    <h2>Recover Password</h2>

    <c:if test="${not empty message}">
        <div class="success">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="user" method="post">

        <input type="hidden" name="action" value="forgot">

        <div class="form-group">
            <label>Enter your email:</label>
            <input type="email" name="email" required>
        </div>

        <button type="submit" class="btn">Reset Password</button>

        <div class="register-link">
            <a href="login.jsp">Back to Login</a>
        </div>
    </form>

</div>

</body>
</html>
