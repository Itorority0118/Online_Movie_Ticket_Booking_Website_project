<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>User Form</title>
    <link rel="stylesheet" href="../css/admin.css">
</head>
<body>

<h2>User Form</h2>

<form action="user" method="post">

    <input type="hidden" name="id" value="${user.userId}"/>

    <label>Full Name:</label>
    <input type="text" name="fullName" value="${user.fullName}" required><br>

    <label>Email:</label>
    <input type="text" name="email" value="${user.email}" required><br>

    <label>Phone:</label>
    <input type="text" name="phone" value="${user.phone}"><br>

    <label>Role:</label>
    <select name="role">
        <option value="user" ${user.role == 'user' ? 'selected' : ''}>User</option>
        <option value="admin" ${user.role == 'admin' ? 'selected' : ''}>Admin</option>
    </select><br>

    <label>Status:</label>
    <select name="status">
        <option value="active" ${user.status == 'active' ? 'selected' : ''}>Active</option>
        <option value="inactive" ${user.status == 'inactive' ? 'selected' : ''}>Inactive</option>
    </select><br>

    <button type="submit">Save</button>

</form>

</body>
</html>
