<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="modal-overlay" id="orderSuccessModal" style="display:none;">
    <div class="success-modal">

        <div class="success-icon">🎉</div>
        <h3>Thanh toán thành công</h3>
        <p>Vé của bạn đã được đặt thành công.</p>

        <div class="success-actions">
            <button class="btn-primary" onclick="openProfileModal()">
                🎟 Xem vé của tôi
            </button>
            <button class="btn-secondary" onclick="closeOrderSuccessModal()">
                Đóng
            </button>
        </div>

    </div>
</div>

</body>
</html>