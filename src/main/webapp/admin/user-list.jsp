<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>User Management</h1>

<!-- ADD USER BUTTON -->
<a href="${pageContext.request.contextPath}/user?action=new"
	class="add-btn">+ Add User</a>

<div class="user-table-container" style="display: flex; gap: 20px; align-items: flex-start;">

	<!-- USER TABLE -->
	<div class="table-container" style="flex: 1 1 auto; min-width: 600px; overflow-x: auto;">
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
					<tr>
						<td>${u.userId}</td>
						<td>${u.fullName}</td>
						<td>${u.email}</td>
						<td>${u.phone}</td>
						<td>${u.role}</td>
						<td>${u.status}</td>
						<td>
							<a class="action edit"
								href="${pageContext.request.contextPath}/user?action=edit&id=${u.userId}">Edit</a>
							| 
							<a class="action delete" href="#" data-id="${u.userId}">Delete</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<!-- FILTER PANEL -->
	<form method="get" action="${pageContext.request.contextPath}/user"
		class="filter-panel" style="flex-shrink: 0; width: 220px; height: fit-content;">
		<input type="hidden" name="action" value="list" /> 
		<input type="text" name="search" placeholder="Search..." value="${param.search}" />

		<div class="filter-group">
			<label> <input type="checkbox" name="role" value="admin"
				<c:if test="${param.role == 'admin'}">checked</c:if> /> Admin
			</label> 
			<label> <input type="checkbox" name="role" value="user"
				<c:if test="${param.role == 'user'}">checked</c:if> /> User
			</label>
		</div>

		<div class="filter-group">
			<label> <input type="checkbox" name="status" value="active"
				<c:if test="${param.status == 'active'}">checked</c:if> /> Active
			</label> 
			<label> <input type="checkbox" name="status" value="inactive"
				<c:if test="${param.status == 'inactive'}">checked</c:if> /> Inactive
			</label>
		</div>

		<button type="submit" class="submit-btn">Apply</button>
	</form>

</div>

<!-- DELETE MODAL -->
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