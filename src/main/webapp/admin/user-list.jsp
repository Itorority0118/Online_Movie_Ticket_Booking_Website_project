<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinema/cinema-form.css?v=2">

<h1>User Management</h1>

<a href="${pageContext.request.contextPath}/user?action=new" class="add-btn">+ Add User</a>

<div class="user-table-container" style="display: flex; gap: 20px; align-items: flex-start;">

    <div class="table-container" style="flex:1 1 auto; min-width:700px; overflow-x:auto;">
        <table class="user-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr data-id="${u.userId}">
                        <td>${u.userId}</td>
                        <td>${u.fullName}</td>
                        <td>${u.email}</td>
                        <td>${u.phone}</td>
                        <td>${u.role}</td>
                        <td>${u.status}</td>
                        <td>
                            <a class="action edit" href="${pageContext.request.contextPath}/user?action=edit&id=${u.userId}">Edit</a> |
                            <a class="action delete" href="#" data-id="${u.userId}" data-type="user">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/user" class="filter-panel" style="flex-shrink:0; width:230px;">
        <input type="hidden" name="action" value="list"/>

        <div class="form-group">
            <label>Search</label>
            <input type="text" name="search" placeholder="Search..." value="${param.search != null ? param.search : ''}" />
        </div>

        <div class="form-group">
            <label>Role</label>
            <select name="role">
                <option value="">All</option>
                <option value="admin" <c:if test="${param.role == 'admin'}">selected</c:if>>Admin</option>
                <option value="user" <c:if test="${param.role == 'user'}">selected</c:if>>User</option>
            </select>
        </div>

        <div class="form-group">
            <label>Status</label>
            <select name="status">
                <option value="">All</option>
                <option value="active" <c:if test="${param.status == 'active'}">selected</c:if>>Active</option>
                <option value="inactive" <c:if test="${param.status == 'inactive'}">selected</c:if>>Inactive</option>
            </select>
        </div>

        <button type="submit" class="submit-btn">Apply</button>
    </form>

</div>

<div id="deleteModal" class="modal">
    <div class="modal-content">
        <h3>Are you sure?</h3>
        <p>This action cannot be undone.</p>
        <div class="modal-buttons">
            <button class="btn cancel" onclick="closeModal()">Cancel</button>
            <button id="confirmDeleteBtn" class="btn delete">Delete</button>
        </div>
    </div>
</div>