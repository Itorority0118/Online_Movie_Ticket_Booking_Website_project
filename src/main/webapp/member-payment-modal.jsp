<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- MEMBER PAYMENT MODAL -->
<div id="memberModal" class="member-modal-overlay">
    <div class="member-modal-box">
        <h2>💳 Thanh toán thành viên</h2>

        <p>Gói thành viên: <strong id="memberType"></strong></p>
        <p>Giá: <strong id="memberPrice"></strong></p>

        <div class="member-modal-actions">
            <button class="confirm" onclick="confirmMember()">✅ Xác nhận</button>
            <button class="close" onclick="closeMemberModal()">❌ Hủy</button>
        </div>
    </div>
</div>
