<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal-overlay" id="orderModal" style="display:none;">
    <div class="order-modal">
        <div class="order-header">
            <h3>Đơn hàng của tôi</h3>
            <button class="order-close-btn" onclick="closeOrderModal()">✖</button>
        </div>

        <div class="order-body">
            <div class="order-column order-tickets">
                <h4>Vé đã chọn</h4>
                <div id="orderTickets">
                    <p>Chưa có vé</p>
                </div>
            </div>

            <div class="order-column order-summary">
                <h4>Thanh toán</h4>
                <div class="summary-item">
                    <span>Tổng tiền:</span>
                    <span id="orderTotalPrice">0₫</span>
                </div>
                <div class="summary-actions">
					<button class="btn-confirm" onclick="checkoutOrder()">Thanh toán</button>
                    <button class="btn-cancel" onclick="closeOrderModal()">Đóng</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="ticketDetailModal" class="modal-overlay">
    <div class="order-modal">
        <div class="order-header">
            <h3>Chi tiết vé</h3>
            <button class="order-close-btn" onclick="closeTicketDetailModal()">✖</button>
        </div>

        <div class="order-body" style="flex-direction: column; gap: 12px;">
            <div id="ticketDetailContent" style="color: #c9d1d9; font-size: 14px;"></div>
            <div class="summary-actions" style="margin-top: 12px;">
                <button class="btn-cancel" onclick="closeTicketDetailModal()">Đóng</button>
            </div>
        </div>
    </div>
</div>