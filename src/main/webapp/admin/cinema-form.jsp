<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinema/cinema-form.css?v=11">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
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
		    <label>Cinema Name <span class="required">*</span></label>
		    <input type="text" name="name"
		           class="${errors.name != null ? 'error-input' : ''}"
		           value="${cinema != null ? cinema.name : ''}">
		    <c:if test="${errors.name != null}">
		        <span class="error">${errors.name}</span>
		    </c:if>
		</div>
		
		<div class="form-group">
		    <label>Address <span class="required">*</span></label>
		    <input type="text" name="address"
		           class="${errors.address != null ? 'error-input' : ''}"
		           value="${cinema != null ? cinema.address : ''}">
		    <c:if test="${errors.address != null}">
		        <span class="error">${errors.address}</span>
		    </c:if>
		</div>
		
		<div class="form-group">
		    <label>City <span class="required">*</span></label>
		    <select name="city" class="${errors.city != null ? 'error-input' : ''}" required>
		        <option value="">-- Select City --</option>
		        <c:forEach var="cityOption" items="${cityList}">
		            <option value="${cityOption}" 
		                <c:if test="${cinema != null && cinema.city == cityOption}">selected</c:if>>
		                ${cityOption}
		            </option>
		        </c:forEach>
		    </select>
		    <c:if test="${errors.city != null}">
		        <span class="error">${errors.city}</span>
		    </c:if>
		</div>
		
		<div class="form-group">
		    <label>Phone Number</label>
		    <input type="text" name="phone"
		           class="${errors.phone != null ? 'error-input' : ''}"
		           value="${cinema != null ? cinema.phone : ''}">
		    <c:if test="${errors.phone != null}">
		        <span class="error">${errors.phone}</span>
		    </c:if>
		</div>


        <button type="submit" class="submit-btn">
            <c:choose>
                <c:when test="${cinema != null}">Save Changes</c:when>
                <c:otherwise>Create Cinema</c:otherwise>
            </c:choose>
        </button>
    </form>
</div>
