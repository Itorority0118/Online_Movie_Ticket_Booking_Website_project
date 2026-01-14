<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="movieModal" class="modal-overlay" style="display:none;">
    <div class="movie-modal">
        <span class="close-btn" onclick="closeMovieModal()">✖</span>

        <div class="movie-modal-content">
            <img id="modalPoster" class="modal-poster">

            <div class="modal-info">
                <h2 id="modalTitle"></h2>
                <p><b>Thể loại:</b> <span id="modalGenre"></span></p>
                <p><b>Thời lượng:</b> <span id="modalDuration"></span> phút</p>
                <p id="modalDescription"></p>

                <a id="modalTrailer" target="_blank" class="trailer-btn">
                    🎬 Xem trailer
                </a>
                <p id="movieDescription"></p>
						
						<hr>
						
						<div class="booking-section">
						
							<label>Chọn thành phố:</label>
							<select id="citySelect" onchange="loadCinemasByCity()">
							    <option value="">-- Chọn thành phố --</option>
							</select>
							
							<br><br>
						
						    <label>Chọn rạp:</label>
						    <select id="cinemaSelect" onchange="loadShowtimesInModal()" display>
						        <option value="">-- Chọn rạp --</option>
						    </select>
						
							<div style="margin-top:10px">
							    <strong>Giờ chiếu:</strong>
							    <div id="showtimeList"></div>
							</div>
							
							<div id="selectedTicketInfo" style="display:none; margin-top:10px;">
							    <h4>🎟 Vé đã chọn</h4>
							    <p><b>Ghế:</b> <span id="selectedSeatsText"></span></p>
							    <p><b>Tổng tiền:</b> <span id="selectedTotalText"></span></p>
							</div>
						    
							<div id="seatModal" class="seat-modal-overlay">
							  <div class="seat-modal">
							
								<div class="cinema-screen">MÀN HÌNH</div>
							
							    <div class="seat-map-wrapper">
							      <div id="seatMap" class="seat-map"></div>
							    </div>
							
							    <div class="seat-legend">
								  <div class="legend-item">
								    <span class="seat-sample booked"></span>
								    <span>Đã đặt</span>
								  </div>
								
								  <div class="legend-item">
								    <span class="seat-sample selected"></span>
								    <span>Ghế bạn chọn</span>
								  </div>
								
								  <div class="legend-item">
								    <span class="seat-sample normal"></span>
								    <span>Ghế thường</span>
								  </div>
								
								  <div class="legend-item">
								    <span class="seat-sample vip"></span>
								    <span>Ghế VIP</span>
								  </div>
								
								  <div class="legend-item">
								    <span class="seat-sample couple"></span>
								    <span>Ghế đôi</span>
								  </div>
								</div>
			
							    <div id="bookingSummary"></div>
									<div class="seat-actions">
									  <button class="confirm-btn" onclick="confirmSeat()">Xác nhận</button>
									  <button class="cancel" onclick="closeSeatModal()">Hủy</button>
									</div>

							  </div>
							</div>

						    <p><b>Tổng tiền:</b> <span id="totalPrice">0</span> </p>
						    
						
						    <div style="margin-top:15px" class="booking-actions">
						        <button onclick="buyTicketInModal()">MUA VÉ</button>
						        <button onclick="addToCartInModal()">THÊM VÀO ĐƠN HÀNG</button>
						    </div>
						
						</div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="order-success-modal.jsp" />
<script>
window.buyTicketInModal = function () {
    if (!selectedTickets || selectedTickets.length === 0) {
        alert("Vui lòng chọn vé trước khi mua");
        return;
    }

    closeSeatModal?.();
    closeMovieModal?.();

    setTimeout(() => {
        checkoutOrder();
    }, 300);
};
</script>