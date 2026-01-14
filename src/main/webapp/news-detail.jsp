<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${news.title}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/news.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css?v=16">
</head>

<body>

<!-- HEADER -->
<jsp:include page="/header.jsp"/>

<!-- TAB (GIỮ ĐỒNG BỘ GIAO DIỆN) -->
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

<!-- NỘI DUNG CHI TIẾT TIN -->
<main class="main-content">
    <div class="news-detail news-layout">

        <!-- NÚT QUAY LẠI -->
        <a href="${pageContext.request.contextPath}/news" class="back-btn">
            ← Quay lại Tin mới & Ưu đãi
        </a>

        <div class="news-body">

            <!-- CỘT TRÁI: ẢNH -->
            <div class="news-image">
                <img src="${pageContext.request.contextPath}/images/image_npp/${news.image}"
                     alt="${news.title}">
            </div>

            <!-- CỘT PHẢI: NỘI DUNG -->
            <div class="news-text">
                <h1>${news.title}</h1>
                <div class="news-date">🗓 ${news.date}</div>

                <div class="news-content">
                    ${news.content}
                </div>
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

<script src="<c:url value='/js/common.js?v=8'/>"></script>
<script src="<c:url value='/js/member.js'/>"></script>
<script src="<c:url value='/js/order-modal.js?v=10'/>"></script>
</body>
</html>
