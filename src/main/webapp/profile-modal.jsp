<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal-overlay" id="profileModal" style="display:none;">
    <div class="profile-modal">

        <div class="profile-header">
            <div class="avatar">👤</div>
            <h3>Thông tin cá nhân</h3>
        </div>

        <div class="profile-body">
            <form id="profileForm">
                <input type="hidden" name="action" value="updateProfile">

                <div id="profileErrors" style="color:red; margin-bottom:10px;"></div>

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

                <div class="form-group">
                    <label>Mật khẩu mới</label>
                    <input type="password" name="password" placeholder="Để trống nếu không đổi">
                </div>         

                <div class="profile-actions">
                    <button type="button" class="btn-save" onclick="saveProfile()">Lưu</button>
                    <button type="button" class="btn-cancel" onclick="closeProfileModal()">Hủy</button>
                </div>
            </form>

            <div class="profile-tickets">
                <h4>Vé của tôi</h4>
                <div id="profileTickets" class="ticket-grid">Đang tải vé...</div>
            </div>

        </div>
    </div>
</div>