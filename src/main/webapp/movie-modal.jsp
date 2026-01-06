<%@ page contentType="text/html; charset=UTF-8" %>

<!-- ===== MODAL CHI TIẾT PHIM ===== -->
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

                <hr>

                <div class="booking-section">

                    <label>Chọn thành phố:</label>
                    <select id="citySelect" onchange="loadCinemasByCity()">
                        <option value="">-- Chọn thành phố --</option>
                    </select>

                    <br><br>

                    <label>Chọn rạp:</label>
                    <select id="cinemaSelect" onchange="loadShowtimesInModal()">
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
                            <div class="screen-label">MÀN HÌNH</div>
                            <div id="seatMap" class="seat-map"></div>

                            <div class="seat-legend">
                                <div class="legend-item"><span class="seat-sample booked"></span> Đã đặt</div>
                                <div class="legend-item"><span class="seat-sample selected"></span> Ghế bạn chọn</div>
                                <div class="legend-item"><span class="seat-sample normal"></span> Ghế thường</div>
                                <div class="legend-item"><span class="seat-sample vip"></span> Ghế VIP</div>
                                <div class="legend-item"><span class="seat-sample couple"></span> Ghế đôi</div>
                            </div>

                            <div class="seat-actions">
                                <button class="confirm-btn" onclick="confirmSeat()">Xác nhận</button>
                                <button class="cancel" onclick="closeSeatModal()">Hủy</button>
                            </div>
                        </div>
                    </div>

                    <p><b>Tổng tiền:</b> <span id="totalPrice">0</span></p>

                    <div style="margin-top:15px">
                        <button onclick="buyTicketInModal()">MUA VÉ</button>
                        <button onclick="addToCartInModal()">THÊM VÀO ĐƠN HÀNG</button>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
