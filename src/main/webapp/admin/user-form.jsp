<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-form.css?v=3">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<div class="user-form-page centered">

    <a href="${pageContext.request.contextPath}/user?action=list" class="back-btn">
        <i class="fas fa-arrow-left"></i> Back to User List
    </a>

    <div class="user-form-title">
        <h1>
            <c:choose>
                <c:when test="${formUser == null || formUser.userId == 0}">
                    Add User
                </c:when>
                <c:otherwise>
                    Edit User
                </c:otherwise>
            </c:choose>
        </h1>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/user">

        <c:if test="${formUser != null && formUser.userId > 0}">
            <input type="hidden" name="id" value="${formUser.userId}" />
        </c:if>

        <!-- FULL NAME -->
		<div class="form-group">
		    <label>Full Name <span class="required">*</span></label>
		    <input type="text" name="fullName"
		           class="${errors.fullName != null ? 'error-input' : ''}"
		           value="${formUser != null ? formUser.fullName : ''}">
		    <c:if test="${errors.fullName != null}">
		        <span class="error">${errors.fullName}</span>
		    </c:if>
		</div>

        <!-- EMAIL -->
		<div class="form-group">
		    <label>Email <span class="required">*</span></label>
		    <input type="email" name="email"
		           class="${errors.email != null ? 'error-input' : ''}"
		           pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"
		           value="${formUser != null ? formUser.email : ''}">
		    <c:if test="${errors.email != null}">
		        <span class="error">${errors.email}</span>
		    </c:if>
		</div>

        <!-- PHONE -->
		<div class="form-group">
		    <label>Phone</label>
		    <input type="text" name="phone"
		           class="${errors.phone != null ? 'error-input' : ''}"
		           inputmode="numeric"
		           oninput="this.value=this.value.replace(/[^0-9]/g,'')"
		           value="${formUser != null ? formUser.phone : ''}">
		    <c:if test="${errors.phone != null}">
		        <span class="error">${errors.phone}</span>
		    </c:if>
		</div>

        <!-- ROLE -->
		<div class="form-group">
		    <label>Role <span class="required">*</span></label>
		    <select name="role">
		        <option value="admin" ${formUser != null && formUser.role == 'admin' ? 'selected' : ''}>Admin</option>
		        <option value="user"  ${formUser != null && formUser.role == 'user' ? 'selected' : ''}>User</option>
		    </select>
		</div>

        <!-- STATUS -->
		<div class="form-group">
		    <label>Status <span class="required">*</span></label>
		    <select name="status">
		        <option value="active"   ${formUser != null && formUser.status == 'active' ? 'selected' : ''}>Active</option>
		        <option value="inactive" ${formUser != null && formUser.status == 'inactive' ? 'selected' : ''}>Inactive</option>
		    </select>
		</div>

        <!-- PASSWORD -->
		<div class="form-group">
		    <c:choose>
		        <c:when test="${formUser == null || formUser.userId == 0}">
		            <label>Password <span class="required">*</span></label>
		        </c:when>
		        <c:otherwise>
		            <label>New Password (leave blank to keep old)</label>
		        </c:otherwise>
		    </c:choose>
		    <input type="password" name="password"
		           class="${errors.password != null ? 'error-input' : ''}">
		    <c:if test="${errors.password != null}">
		        <span class="error">${errors.password}</span>
		    </c:if>
		</div>

        <button type="submit" class="submit-btn">
            <c:choose>
                <c:when test="${formUser == null || formUser.userId == 0}">
                    Create User
                </c:when>
                <c:otherwise>
                    Save Changes
                </c:otherwise>
            </c:choose>
        </button>
    </form>
</div>