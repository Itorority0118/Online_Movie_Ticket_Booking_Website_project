<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinema/cinema-form.css">

<div class="cinema-form-page centered">

    <a href="${pageContext.request.contextPath}/cinema?action=list" class="back-btn">
        <i class="fas fa-arrow-left"></i> Back to Cinema List
    </a>

    <h1 class="cinema-form-title">
        <c:choose>
            <c:when test="${cinema != null}">Edit Cinema</c:when>
            <c:otherwise>Add Cinema</c:otherwise>
        </c:choose>
    </h1>

    <form method="post" action="${pageContext.request.contextPath}/cinema">

        <c:if test="${cinema != null}">
            <input type="hidden" name="id" value="${cinema.cinemaId}">
        </c:if>

        <div class="form-group">
            <label>Cinema Name</label>
            <input type="text" name="name"
                   value="${cinema != null ? cinema.name : ''}" required>
        </div>

        <div class="form-group">
            <label>Address</label>
            <input type="text" name="address"
                   value="${cinema != null ? cinema.address : ''}" required>
        </div>

        <div class="form-group">
            <label>City</label>
            <select name="city" required>
                <option value="">-- Select City --</option>
                <c:set var="vietnamCities">
                    <c:out value="Hà Nội,Hồ Chí Minh,Đà Nẵng,Hải Phòng,Cần Thơ,Đắk Lắk,Khánh Hòa,Quảng Ninh,Bình Dương,Đồng Nai,Bình Định,Thừa Thiên Huế,Nghệ An,Thanh Hóa,An Giang,Tiền Giang,Bến Tre,Vũng Tàu,Long An,Lâm Đồng,Hải Dương,Phú Yên,Kon Tum,Quảng Nam,Quảng Ngãi,Hậu Giang,Bạc Liêu,Cà Mau,Trà Vinh,Kiên Giang,Sóc Trăng,Bình Phước,Bình Thuận,Ninh Bình,Hà Nam,Hà Tĩnh,Quảng Trị,Quảng Bình,Bắc Ninh,Bắc Giang,Lào Cai,Yên Bái,Sơn La,Hòa Bình,Tuyên Quang,Bắc Kạn,Điện Biên,Lai Châu,Phú Thọ,Thái Nguyên,Vĩnh Phúc,Bắc Giang,Hưng Yên,Hải Dương,Hải Phòng" />
                </c:set>
                <c:forEach var="city" items="${fn:split(vietnamCities, ',')}">
                    <option value="${city}" 
                        <c:if test="${cinema != null && cinema.city == city}">selected</c:if>>
                        ${city}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Phone Number</label>
            <input type="text" name="phone"
                   value="${cinema != null ? cinema.phone : ''}">
        </div>

        <button type="submit" class="submit-btn">
            <c:choose>
                <c:when test="${cinema != null}">Save Changes</c:when>
                <c:otherwise>Create Cinema</c:otherwise>
            </c:choose>
        </button>

    </form>
</div>
