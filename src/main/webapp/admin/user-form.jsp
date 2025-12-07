<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/user-form.css?v=1">

<div class="user-form-page centered">
    <a href="${pageContext.request.contextPath}/user?action=list" class="back-btn">
        <i class="fas fa-arrow-left"></i> Back to User List
    </a>
	<div class="user-form-title">
		<h1>${user == null ? "Add User" : "Edit User"}</h1>
	</div>
	<form method="post" action="${pageContext.request.contextPath}/user">

		<c:if test="${user != null}">
			<input type="hidden" name="id" value="${user.userId}" />
		</c:if>

		<div class="form-group">
			<label>Full Name</label> <input type="text" name="fullName"
				value="${user != null ? user.fullName : ''}">
		</div>

		<div class="form-group">
			<label>Email</label> <input type="email" name="email"
				pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}" required
				value="${user != null ? user.email : ''}">
		</div>

		<div class="form-group">
			<label>Phone</label> <input type="text" name="phone" pattern="[0-9]+"
				inputmode="numeric"
				oninput="this.value=this.value.replace(/[^0-9]/g,'')" required
				value="${user != null ? user.phone : ''}">
		</div>

		<div class="form-group">
			<label>Role</label> <select name="role">
				<option value="admin"
					${user != null && user.role=='admin'?'selected':''}>Admin</option>
				<option value="user"
					${user != null && user.role=='user'?'selected':''}>User</option>
			</select>
		</div>

		<div class="form-group">
			<label>Status</label> <select name="status">
				<option value="active"
					${user != null && user.status=='active'?'selected':''}>Active</option>
				<option value="inactive"
					${user != null && user.status=='inactive'?'selected':''}>Inactive</option>
			</select>
		</div>

		<c:choose>
			<c:when test="${user == null}">
				<div class="form-group">
					<label>Password</label> <input type="password" name="password"
						required>
				</div>
			</c:when>

			<c:otherwise>
				<div class="form-group">
					<label>New Password (leave blank to keep old)</label> <input
						type="password" name="password">
				</div>
			</c:otherwise>
		</c:choose>

		<button type="submit" class="submit-btn">${user == null ? "Create User" : "Save Changes"}
		</button>

	</form>
</div>