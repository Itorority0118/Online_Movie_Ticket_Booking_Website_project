<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Beta Cinemas - Tin mới & Ưu đãi</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css?v=13">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css?v=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/news.css">

</head>
<body>
<jsp:include page="/header.jsp"/>

<div class="movie-tabs-container">
    <div class="movie-tabs">
        <a href="${pageContext.request.contextPath}/movie?action=coming_soon"
           class="tab-item ${param.action != 'now_showing' && param.action != 'special_show' ? '' : ''}">
           PHIM SẮP CHIẾU
        </a>

        <a href="${pageContext.request.contextPath}/movie?action=now_showing"
           class="tab-item ${param.action == 'now_showing'  && param.action != 'special_show' ? '' : ''}">
           PHIM ĐANG CHIẾU
        </a>

    </div>
</div>

<main class="main-content">
    <div class="movie-list">
	
	        <!-- 8 -->
        <a href="${pageContext.request.contextPath}/news-detail?id=1" class="news-link">
        <div class="movie-card">
        	<span class="ribbon ribbon-hot">HOT</span>
            <img src="${pageContext.request.contextPath}/images/image_npp/image1.jpg" class="movie-poster">
            <h3>🎁  Giảm giá sốc khi thay toán bằng VPPAY</h3>
            <p>
				Giảm giá lên đến 10% cho mỗi đơn hàng
            </p>
        </div>
		</a>	
		
        <!-- 1 -->
        <a href="${pageContext.request.contextPath}/news-detail?id=2" class="news-link">
	        <div class="movie-card">
	        	 <span class="ribbon ribbon-movie">PHIM</span>
	        	
	            <img src="${pageContext.request.contextPath}/images/image_npp/image2.jpg" class="movie-poster">
	            <h3>Into the Future chính thức ra rạp</h3>
	            <p>
	                Bom tấn khoa học viễn tưởng được mong chờ nhất năm 2025...
	            </p>
	        </div>
        </a>
        

        <!-- 2 -->
        <a href="${pageContext.request.contextPath}/news-detail?id=3" class="news-link">
        <div class="movie-card">
        	 <span class="ribbon ribbon-movie">PHIM</span>
            <img src="${pageContext.request.contextPath}/images/image_npp/image3.jpg" class="movie-poster">
            <h3>Action Blast gây sốt phòng vé tuần đầu</h3>
            <p>
                Phim hành động Action Blast đạt doanh thu kỷ lục
                ngay trong tuần công chiếu đầu tiên.
            </p>
        </div>
        </a>

        <!-- 3 -->
        <a href="${pageContext.request.contextPath}/news-detail?id=4" class="news-link">
        <div class="movie-card">
        	 <span class="ribbon ribbon-movie">PHIM</span>
            <img src="${pageContext.request.contextPath}/images/image_npp/image4.jpg" class="movie-poster">
            <h3>Mystery Island – Bí ẩn chưa lời giải</h3>
            <p>
                Bộ phim trinh thám – tâm lý đang khiến cộng đồng mạng
                tranh luận sôi nổi với cái kết đầy bất ngờ.
            </p>
        </div>
        </a>

        <!-- 4 -->
        <a href="${pageContext.request.contextPath}/news-detail?id=5" class="news-link">
        <div class="movie-card">
        	<span class="ribbon ribbon-promo">ƯU ĐÃI</span>
            <img src="${pageContext.request.contextPath}/images/image_npp/image5.jpg" class="movie-poster">
            <h3>🎉 Ưu đãi học sinh – sinh viên</h3>
            <p>
                Giảm ngay 20% giá vé khi xuất trình thẻ HSSV
                từ thứ 2 đến thứ 5 hàng tuần.
            </p>
        </div>
        </a>

        <!-- 5 -->
        <a href="${pageContext.request.contextPath}/news-detail?id=6" class="news-link">
        <div class="movie-card">
        	<span class="ribbon ribbon-promo">ƯU ĐÃI</span>
            <img src="${pageContext.request.contextPath}/images/image_npp/image6.png" class="movie-poster">
            <h3>🎟 Thành viên Beta – Nhận ưu đãi đặc biệt</h3>
            <p>
                Tích điểm đổi quà, nhận ưu đãi độc quyền
                dành riêng cho thành viên Beta Cinemas.
            </p>
        </div>
        </a>

        <!-- 6 -->
        <a href="${pageContext.request.contextPath}/news-detail?id=7" class="news-link">
        <div class="movie-card">
       	 	 <span class="ribbon ribbon-movie">PHIM</span>	
            <img src="${pageContext.request.contextPath}/images/image_npp/image7.jpg" class="movie-poster">
            <h3>Summer Love – Phim tình cảm đáng xem mùa hè</h3>
            <p>
                Câu chuyện tình yêu nhẹ nhàng, lãng mạn
                phù hợp cho những buổi hẹn hò cuối tuần.
            </p>
        </div>
        </a>
	
        <!-- 7 -->
        <a href="${pageContext.request.contextPath}/news-detail?id=8" class="news-link">
        <div class="movie-card">
        	 <span class="ribbon ribbon-movie">PHIM</span>
            <img src="${pageContext.request.contextPath}/images/image_npp/image8.jpg" class="movie-poster">
            <h3>Comedy Nights mang tiếng cười trở lại</h3>
            <p>
                Phim hài Comedy Nights hứa hẹn mang đến
                những phút giây thư giãn cho mọi lứa tuổi.
            </p>
        </div>
        </a>
        
        <!-- 9 -->
		<a href="${pageContext.request.contextPath}/news-detail?id=9" class="news-link">
		    <div class="movie-card">
		        <span class="ribbon ribbon-hot">HOT</span>
		        <img src="${pageContext.request.contextPath}/images/image_npp/image9.jpg" class="movie-poster">
		        <h3>🎬 Tuần lễ phim bom tấn cuối năm 2025</h3>
		        <p>
		            Hàng loạt phim bom tấn Hollywood đồng loạt ra mắt
		            trong tuần lễ điện ảnh đặc biệt tại Beta Cinemas.
		        </p>
		    </div>
		</a>     
        
    </div>
</main>
<jsp:include page="order-success-modal.jsp"/>
<jsp:include page="profile-modal.jsp"/>
<jsp:include page="order-modal.jsp"/>
<jsp:include page="movie-modal.jsp"/>
<jsp:include page="footer.jsp" />
<script>
    window.IS_LOGGED_IN = ${sessionScope.user != null};
</script>
<script>
    window.APP_CONTEXT = "${pageContext.request.contextPath}";
</script>
<script src="<c:url value='/js/common.js?v=8'/>"></script>
<script src="<c:url value='/js/movie.js'/>"></script>
<script src="<c:url value='/js/order-modal.js?v=11'/>"></script>
</body>
</html>
