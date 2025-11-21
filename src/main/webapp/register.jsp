<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Registration</title>
    <link rel="stylesheet" href="css/register.css">
</head>
<body>

<div class="container">
    <h2>Register</h2>

    <!-- Hiển thị lỗi -->
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="user" method="post">
        <input type="hidden" name="action" value="register">

        <label>Full Name:</label>
        <input type="text" name="fullName" required>

        <label>Email:</label>
        <input type="email" name="email" required>

        <label>Password:</label>
        <input type="password" name="password" required>

        <label>Phone:</label>
        <input type="text" name="phone" required>

        <button type="submit">Register</button>
    </form>

    <p style="margin-top: 15px">
        Already have an account?
        <a href="login.jsp">Login here</a>
    </p>
</div>

</body>
</html>