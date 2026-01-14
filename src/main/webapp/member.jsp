<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Beta Cinemas - Thành viên</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css?v=16">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/news.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">
</head>

<body>

<!-- HEADER -->
<jsp:include page="header.jsp"/>

<div class="movie-tabs-container">
    <div class="movie-tabs">
        <a href="${pageContext.request.contextPath}/movie?action=coming_soon" class="tab-item">
            PHIM SẮP CHIẾU
        </a>
        <a href="${pageContext.request.contextPath}/movie?action=now_showing" class="tab-item">
            PHIM ĐANG CHIẾU
        </a>
    </div>
</div>

<main class="main-content">
    <div class="member-page">

        <h1>🎟 CHƯƠNG TRÌNH THÀNH VIÊN BETA CINEMAS</h1>
        <p class="member-desc">
            Trở thành thành viên Beta để nhận nhiều ưu đãi hấp dẫn
            khi xem phim và mua combo.
        </p>

        <div class="member-grid">

            <!-- ===== SILVER ===== -->
			<div class="member-card silver">
			    <h2>🥉 SILVER</h2>
			    <p class="level-desc">Dành cho khách hàng mới</p>
			
			    <ul>
			        <li>🎫 Giảm 5% giá vé</li>
			        <li>🍿 Giảm 5% combo bắp nước</li>
			        <li>⭐ Tích 1 điểm / 10.000đ</li>
			        <li>📩 Nhận thông tin phim mới</li>
			    </ul>
			
			    <span class="condition">Điều kiện: Đăng ký miễn phí</span>
			
			    <c:choose>
			        <c:when test="${sessionScope.user == null}">
			            <button class="member-btn"
			                    onclick="location.href='${pageContext.request.contextPath}/login.jsp'">
			                🔐 Đăng nhập để tham gia
			            </button>
			        </c:when>
			
			        <c:when test="${sessionScope.user != null and sessionScope.user.role eq 'ADMIN'}">
			            <button class="member-btn disabled">
			                🚫 Admin không được đăng ký
			            </button>
			        </c:when>
			
			        <c:otherwise>
			            <button class="member-btn buy"
			                    onclick="openMemberModal('SILVER', '0₫')">
			                🎟 Mua vé Silver
			            </button>
			        </c:otherwise>
			    </c:choose>
			</div>


            <!-- ===== GOLD ===== -->
            <div class="member-card gold">
                <h2>🥈 GOLD</h2>
                <p class="level-desc">Khách hàng thân thiết</p>

                <ul>
                    <li>🎫 Giảm 10% giá vé</li>
                    <li>🍿 Giảm 10% combo</li>
                    <li>⭐ Tích 1.5 điểm / 10.000đ</li>
                    <li>🎁 Quà sinh nhật</li>
                </ul>

                <span class="condition">Điều kiện: 5.000 điểm / năm</span>

                <c:choose>

				    <c:when test="${sessionScope.user == null}">
				        <button class="member-btn disabled">🔒 Cần đăng nhập</button>
				    </c:when>
				
				    <c:when test="${sessionScope.user != null and sessionScope.user.role eq 'ADMIN'}">
				        <button class="member-btn disabled">
				            🚫 Admin không được đăng ký
				        </button>
				    </c:when>
				
				    <c:otherwise>
				        <button class="member-btn upgrade"
				                onclick="openMemberModal('GOLD', '299.000₫')">
				            ⬆ Nâng cấp GOLD
				        </button>
				    </c:otherwise>
				
				</c:choose>

            </div>

            <!-- ===== PLATINUM ===== -->
            <div class="member-card platinum">
                <h2>🥇 PLATINUM</h2>
                <p class="level-desc">Khách VIP</p>

                <ul>
                    <li>🎫 Giảm 15% giá vé</li>
                    <li>🍿 Giảm 20% combo</li>
                    <li>⭐ Tích 2 điểm / 10.000đ</li>
                    <li>👑 Ưu tiên đặt vé sớm</li>
                    <li>🎟 Vé miễn phí mỗi tháng</li>
                </ul>

                <span class="condition">Điều kiện: 15.000 điểm / năm</span>

                <c:choose>

				    <c:when test="${sessionScope.user == null}">
				        <button class="member-btn disabled">🔒 VIP Only</button>
				    </c:when>
				
				    <c:when test="${sessionScope.user != null and sessionScope.user.role eq 'ADMIN'}">
				        <button class="member-btn disabled">
				            🚫 Admin không được đăng ký
				        </button>
				    </c:when>
				
				    <c:otherwise>
				        <button class="member-btn vip"
				                onclick="openMemberModal('PLATINUM', '599.000₫')">
				            👑 Trở thành PLATINUM
				        </button>
				    </c:otherwise>
				
				</c:choose>

            </div>

        </div>
    </div>
</main>

<!-- FOOTER & MODAL -->
<jsp:include page="order-success-modal.jsp"/>
<jsp:include page="profile-modal.jsp"/>
<jsp:include page="order-modal.jsp"/>
<jsp:include page="movie-modal.jsp"/>
<jsp:include page="member-payment-modal.jsp"/>
<jsp:include page="footer.jsp"/>

<script>
    window.IS_LOGGED_IN = ${sessionScope.user != null};
</script>
<script>
    window.APP_CONTEXT = "${pageContext.request.contextPath}";
</script>

<script src="<c:url value='/js/common.js?v=11'/>"></script>
<script src="<c:url value='/js/member.js'/>"></script>
<script src="<c:url value='/js/order-modal.js?v=11'/>"></script>

</body>
</html>
