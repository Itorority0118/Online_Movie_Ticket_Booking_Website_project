<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div id="paymentModal" class="modal-overlay">


    <div class="payment-box">

        <button class="close-btn" onclick="closePaymentModal()">✕</button>

        <h2>💳 Xác nhận thanh toán</h2>

        <!-- USER -->
        <div class="payment-section">
            <p><b>Họ tên:</b> ${user.fullName}</p>
            <p><b>SĐT:</b> ${user.phone}</p>
            <p><b>Email:</b> ${user.email}</p>
            <p><b>Thời gian đặt:</b>
                <fmt:formatDate value="${orderTime}" pattern="dd/MM/yyyy HH:mm"/>
            </p>
        </div>

        <hr>

        <!-- TICKETS -->
        <div class="payment-section">
            <c:forEach var="t" items="${tickets}">
                <p>🎬 ${t.movieTitle} – ${t.seatLabel} – ${t.price} đ</p>
            </c:forEach>
        </div>

        <hr>

        <p><b>Tổng tiền:</b> ${totalAmount} đ</p>

        <button id="confirmPayBtn" onclick="confirmPayment()">
            ✅ Xác nhận thanh toán
        </button>

    </div>
</div>


