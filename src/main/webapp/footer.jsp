<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<footer class="site-footer">
    <div class="footer-top">

        <!-- INTRO + ABOUT -->
        <div class="footer-intro">
            <div class="footer-logo">
                <img src="${pageContext.request.contextPath}/images/movies/action_blast.jpg"
                     alt="Logo"
                     class="footer-logo-img">
                <span>Beta Cinemas - Thái Nguyên</span>
            </div>

            <div class="footer-section">
                <h4>Về chúng tôi</h4>
                <p>
                    Beta Cinemas mang đến trải nghiệm điện ảnh hiện đại
                    với hệ thống phòng chiếu chất lượng cao, âm thanh sống động
                    và dịch vụ chuyên nghiệp.
                </p>

                <p>
                    📍 Địa chỉ: TP. Thái Nguyên, Việt Nam<br>
                    📞 Hotline: 1900 1234<br>
                    ✉ Email: highteckcinema@gmail.com
                </p>
            </div>
        </div>

        <!-- QUICK LINKS -->
        <div class="footer-section">
            <h4>Liên kết nhanh</h4>
            <ul>
			    <li>
			        <a href="${pageContext.request.contextPath}/movie?action=now_showing"
			           class="${param.action == 'now_showing' ? 'active' : ''}">
			            Phim đang chiếu
			        </a>
			    </li>
			
			    <li>
			        <a href="${pageContext.request.contextPath}/movie?action=coming_soon"
			           class="${param.action == 'coming_soon' ? 'active' : ''}">
			            Phim sắp chiếu
			        </a>
			    </li>
				<li>
				    <a href="${pageContext.request.contextPath}/news.jsp"
				       class="${fn:endsWith(pageContext.request.requestURI, '/news.jsp') ? 'active' : ''}">
				        Khuyến mãi
				    </a>
				</li>
				<li>
				    <a href="${pageContext.request.contextPath}/member.jsp"
				       class="${fn:endsWith(pageContext.request.requestURI, '/member.jsp') ? 'active' : ''}">
				        Thành viên
				    </a>
				</li>

            </ul>
        </div>

        <!-- SOCIAL -->
        <div class="footer-section">
            <h4>Kết nối với chúng tôi</h4>
            <ul class="social-links">
                <li><a href="#" target="_blank">Facebook</a></li>
                <li><a href="#" target="_blank">Instagram</a></li>
                <li><a href="#" target="_blank">Twitter</a></li>
                <li><a href="#" target="_blank">YouTube</a></li>
            </ul>
        </div>

    </div>

    <!-- BOTTOM -->
    <div class="footer-bottom">
        © 2026 Beta Cinemas. All rights reserved.
    </div>
</footer>