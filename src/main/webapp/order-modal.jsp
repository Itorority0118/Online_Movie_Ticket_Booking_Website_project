<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- NỀN MODAL -->
<div id="orderModal" style="display:flex;" onclick="closeOrderModal()">

    <!-- HỘP MODAL -->
    <div class="order-modal" onclick="event.stopPropagation()">

        <!-- ❌ NÚT ĐÓNG -->
        <button class="order-close-btn" onclick="closeOrderModal()">✕</button>

        <div class="order-layout">

            <!-- ================= BÊN TRÁI ================= -->
            <div class="order-list">
                <h2>🛒 Đơn hàng hiện tại</h2>

                <c:forEach var="order" items="${orderList}">
                    <c:if test="${order.status eq 'HOLD'}">

                        <div class="order-item"
                             data-ticket-id="${order.ticketId}"
                             data-price="${order.price}">

                            <!-- CHECKBOX -->
                            <label class="order-check">
                                <input type="checkbox"
                                       checked
                                       onchange="toggleTicket(this)"
                                       data-id="${order.ticketId}"
                                       data-movie="${order.movieTitle}"
                                       data-seat="${order.seatLabel}"
                                       data-price="${order.price}">
                                <span>Chọn</span>
                            </label>

                            <p><b>Phim:</b> ${order.movieTitle}</p>
                            <p><b>Ghế:</b> ${order.seatLabel}</p>

                            <p>
                                <b>Trạng thái:</b>
                                <span class="hold">⏳ Giữ ghế</span>
                            </p>

                            <p class="hold-timer"
                               data-booking-time="${order.bookingTime.time}">
                                ⏳ Còn lại:
                                <span class="countdown">05:00</span>
                            </p>

                            <button class="cancel-btn"
                                    onclick="cancelHold(this, ${order.ticketId})">
                                ❌ Bỏ vé
                            </button>
                        </div>

                    </c:if>
                </c:forEach>
            </div>

            <!-- ================= BÊN PHẢI ================= -->
            <div class="order-side">
                <h3>🧾 Thanh toán</h3>

                <div id="selectedTickets">
                    <p>Chưa chọn vé</p>
                </div>

                <hr>

                <p>
                    <b>Tổng tiền:</b>
                    <span id="orderTotalPrice">0 đ</span>
                </p>

                <button class="pay-all-btn"
				        onclick="openPaymentModal()">
				    💳 Thanh toán
				</button>
            </div>

        </div>
    </div>
</div>

