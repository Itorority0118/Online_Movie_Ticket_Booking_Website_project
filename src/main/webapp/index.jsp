<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Beta Cinemas -
        <c:choose>
            <c:when test="${param.action == 'now_showing'}">Phim Đang Chiếu</c:when>
            <c:when test="${param.action == 'special_show'}">Suất Chiếu Đặc Biệt</c:when>
            <c:otherwise>Phim Sắp Chiếu</c:otherwise>
        </c:choose>
    </title>

    <!-- Nếu vào trực tiếp index.jsp thì redirect về servlet -->
    <c:if test="${movieList == null}">
        <meta http-equiv="refresh"
              content="0;url=${pageContext.request.contextPath}/movie?action=now_showing">
    </c:if>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>

<body>

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
                <p id="movieDescription"></p>
						
						<!-- ====== THÊM TỪ ĐÂY ====== -->
						<hr>
						
						<div class="booking-section">
						
							<!-- CHỌN THÀNH PHỐ -->
							<label>Chọn thành phố:</label>
							<select id="citySelect" onchange="loadCinemasByCity()">
							    <option value="">-- Chọn thành phố --</option>
							</select>
							
							<br><br>
						
						    <!-- CHỌN RẠP -->
						    <label>Chọn rạp:</label>
						    <select id="cinemaSelect" onchange="loadShowtimesInModal()" display>
						        <option value="">-- Chọn rạp --</option>
						    </select>
						
						    <!-- GIỜ CHIẾU -->
							<div style="margin-top:10px">
							    <strong>Giờ chiếu:</strong>
							    <div id="showtimeList"></div>
							</div>
							
							<!-- ✅ THÔNG TIN VÉ ĐÃ CHỌN -->
							<div id="selectedTicketInfo" style="display:none; margin-top:10px;">
							    <h4>🎟 Vé đã chọn</h4>
							    <p><b>Ghế:</b> <span id="selectedSeatsText"></span></p>
							    <p><b>Tổng tiền:</b> <span id="selectedTotalText"></span></p>
							</div>
						    
							<div id="seatModal" class="seat-modal-overlay">
							  <div class="seat-modal">
							
							    <!-- SCREEN -->
							    <div class="screen-label">MÀN HÌNH</div>
							
							    <!-- SEAT MAP -->
							    <div id="seatMap" class="seat-map"></div>
							
							    <!-- LEGEND -->
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
			
							    <!-- ACTION -->
							    <div id="bookingSummary"></div>
									<div class="seat-actions">
									  <button class="confirm-btn" onclick="confirmSeat()">Xác nhận</button>
									  <button class="cancel" onclick="closeSeatModal()">Hủy</button>
									</div>

							  </div>
							</div>

						    <p><b>Tổng tiền:</b> <span id="totalPrice">0</span> </p>
						    
						
						    <!-- NÚT HÀNH ĐỘNG -->
						    <div style="margin-top:15px">
						        <button onclick="buyTicketInModal()">MUA VÉ</button>
						        <button onclick="addToCartInModal()">THÊM VÀO ĐƠN HÀNG</button>
						    </div>
						
						</div>
						<!-- ====== KẾT THÚC ====== -->
            </div>
        </div>
    </div>
</div>


<!-- ================= MODAL THÔNG TIN CÁ NHÂN ================= -->
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
                <input type="text" name="fullName"
                       value="${sessionScope.user.fullName}" required>
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email"
                       value="${sessionScope.user.email}" disabled>
            </div>

            <div class="form-group">
                <label>Số điện thoại</label>
                <input type="text" name="phone"
                       value="${sessionScope.user.phone}">
            </div>

            <div class="profile-actions">
                <button type="submit" class="btn-save">Lưu</button>
                <button type="button" class="btn-cancel"
                        onclick="closeProfileModal()">Hủy</button>
            </div>
        </form>

    </div>
</div>
</c:if>
<!-- ========================================================== -->

<!-- ================= MODAL ĐƠN HÀNG ================= -->
<div class="modal-overlay" id="orderModal" style="display:none;">
    <div class="profile-modal order-modal">
        <div class="profile-header">
            <h3>Đơn hàng của tôi</h3>
        </div>

        <div id="orderContent">
            <!-- AJAX sẽ đổ HTML vào đây -->
        </div>

        <div class="profile-actions">
            <button type="button" class="btn-cancel"
                    onclick="closeOrderModal()">Đóng</button>
        </div>
    </div>
</div>



<header class="header">
    <div class="logo-container">
        <img src="${pageContext.request.contextPath}/images/movies/action_blast.jpg" class="logo">
        <span class="cinema-location">Beta Thái Nguyên</span>
    </div>

    <nav class="main-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/showtime">PHIM</a></li>
            <li><a href="#">TIN MỚI VÀ ƯU ĐÃI</a></li>
            <li><a href="#">NHƯỢNG QUYỀN</a></li>
            <li><a href="#">THÀNH VIÊN</a></li>
        </ul>
    </nav>

    <div class="user-status">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <div class="user-menu">
                    <span>Xin chào, ${sessionScope.user.fullName}</span>
                    <div class="user-icon" onclick="toggleUserDropdown()">👤</div>

                    <div class="user-dropdown" id="userDropdown">
                        <c:if test="${sessionScope.role == 'CUSTOMER'}">
                            <a href="javascript:void(0)" onclick="openProfileModal()">Thông tin cá nhân</a>
                        </c:if>

                        <a href="javascript:void(0)" onclick="openOrderModal()">Đơn hàng</a>

                        <c:if test="${sessionScope.role == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard">Trang quản trị</a>
                        </c:if>

                        <hr>
                        <a href="${pageContext.request.contextPath}/user?action=logout">Đăng xuất</a>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login.jsp" class="login-btn">Đăng nhập</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>

<!-- =================== TABS =================== -->
<div class="movie-tabs-container">
    <div class="movie-tabs">
        <a href="${pageContext.request.contextPath}/movie?action=coming_soon"
           class="tab-item ${param.action != 'now_showing' && param.action != 'special_show' ? 'active' : ''}">
           PHIM SẮP CHIẾU
        </a>

        <a href="${pageContext.request.contextPath}/movie?action=now_showing"
           class="tab-item ${param.action == 'now_showing' ? 'active' : ''}">
           PHIM ĐANG CHIẾU
        </a>

    </div>
</div>

<main class="main-content">
    <div class="movie-list">

        <c:forEach var="movie" items="${movieList}">
            <div class="movie-card">
			
			    <div class="movie-image-wrapper"
			         onclick="openMovieModal(
			            '${fn:escapeXml(movie.title)}',
			            '${movie.genre}',
			            '${movie.duration}',
			            '${fn:escapeXml(movie.description)}',
			            '${pageContext.request.contextPath}/images/movies/${fn:replace(movie.posterUrl,'/images/','')}',
			            '${movie.trailerUrl}',
			            '${movie.movieId}'
			         )">
			        <img src="${pageContext.request.contextPath}/images/movies/${fn:replace(movie.posterUrl,'/images/','')}"
			             class="movie-poster">
			    </div>
			
			    <h3>${movie.title}</h3>
			    <p>Thể loại: ${movie.genre}</p>
			    <p>Thời lượng: ${movie.duration} phút</p>
			
			    <button class="buy-ticket-btn"
				    onclick="openMovieModal(
				        '${fn:escapeXml(movie.title)}',
				        '${movie.genre}',
				        '${movie.duration}',
				        '${fn:escapeXml(movie.description)}',
				        '${pageContext.request.contextPath}/images/movies/${fn:replace(movie.posterUrl,'/images/','')}',
				        '${movie.trailerUrl}',
				        '${movie.movieId}',
				        true
				    )">
				    MUA VÉ
				</button>

			</div>

        </c:forEach>

        <c:if test="${not empty movieList && movieList.size() == 0}">
            <p style="text-align:center; margin-top:50px;">
                Hiện tại chưa có phim nào trong mục này.
            </p>
        </c:if>

    </div>
</main>

<script>
    window.IS_LOGGED_IN = ${sessionScope.user != null};
</script>
<script>
    window.APP_CONTEXT = "${pageContext.request.contextPath}";
</script>
<script src="<c:url value='/js/common.js'/>"></script>
<script src="<c:url value='/js/movie.js'/>"></script>
<script src="<c:url value='/js/order-modal.js'/>"></script>
</body>
</html>
