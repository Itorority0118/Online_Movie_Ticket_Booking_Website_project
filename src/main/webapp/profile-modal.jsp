<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${sessionScope.role == 'CUSTOMER'}">
<div class="modal-overlay" id="profileModal" style="display:none;">
    <div class="profile-modal">
        <div class="profile-header">
            <div class="avatar">👤</div>
            <h3>Thông tin cá nhân</h3>
        </div>

        <form action="${pageContext.request.contextPath}/user" method="post">
            <input type="hidden" name="action" value="updateProfile">

            <div class="form-group">
                <label>Họ và tên</label>
                <input type="text" name="fullName" value="${sessionScope.user.fullName}" required>
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email" value="${sessionScope.user.email}" disabled>
            </div>

            <div class="form-group">
                <label>Số điện thoại</label>
                <input type="text" name="phone" value="${sessionScope.user.phone}">
            </div>

            <div class="profile-actions">
                <button type="submit" class="btn-save">Lưu</button>
                <button type="button" class="btn-cancel" onclick="closeProfileModal()">Hủy</button>
            </div>
        </form>
    </div>
</div>
</c:if>