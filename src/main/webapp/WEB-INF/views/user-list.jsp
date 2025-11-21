<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
    <link rel="stylesheet" href="../css/admin.css">
</head>
<body>

<h2>User Management</h2>

<a href="user?action=new">+ Add User</a>

<table border="1" cellpadding="10">
    <tr>
        <th>ID</th>
        <th>Full Name</th>
        <th>Email</th>
        <th>Role</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

    <c:forEach var="u" items="${users}">
        <tr>
            <td>${u.userId}</td>
            <td>${u.fullName}</td>
            <td>${u.email}</td>
            <td>${u.role}</td>
            <td>${u.status}</td>
            <td>
                <a href="user?action=edit&id=${u.userId}">Edit</a> |
                <a href="user?action=delete&id=${u.userId}" onclick="return confirm('Delete?')">Delete</a>
            </td>
        </tr>
    </c:forEach>

</table>

</body>
</html>
