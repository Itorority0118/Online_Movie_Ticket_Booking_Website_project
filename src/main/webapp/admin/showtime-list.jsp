<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/room/room-form.css?v=6">

<a href="${pageContext.request.contextPath}/room?action=list&cinemaId=${cinemaId}"
   class="back-btn">
    <i class="fas fa-arrow-left"></i> Back to Room List
</a>

<h1>
    <c:choose>
        <c:when test="${not empty roomId}">
            Showtime Management for Room: ${roomId}
        </c:when>
        <c:otherwise>
            All Showtimes
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${param.role == 'admin'}">
    <a href="${pageContext.request.contextPath}/showtime?role=admin&action=new&roomId=${roomId}" class="add-btn">
        + Add Showtime
    </a>
</c:if>

<div class="cinema-table-container" style="display:flex; gap:20px; align-items:flex-start; margin-top:20px;">

    <!-- Table showtime -->
    <div class="table-container"
         id="showtimeTableWrapper"
         style="flex:1 1 auto; min-width:700px; overflow-x:auto;">
        <jsp:include page="showtime-table.jsp"/>
    </div>

    <!-- Filter Panel -->
	<form method="get"
      action="${pageContext.request.contextPath}/showtime"
      class="filter-panel">

    <input type="hidden" name="action" value="list"/>
    <input type="hidden" name="role" value="admin"/>

    <c:if test="${not empty roomId}">
        <input type="hidden" name="roomId" value="${roomId}" />
    </c:if>

    <div class="form-group">
        <label>Movie name</label>
        <input type="text" name="movieName" value="${param.movieName}" />
    </div>

    <div class="form-group">
        <label>Start time</label>
        <input type="datetime-local" name="startTime" value="${param.startTime}" />
    </div>

    <div class="form-group">
        <label>End time</label>
        <input type="datetime-local" name="endTime" value="${param.endTime}" />
    </div>

        <button type="submit" class="submit-btn">Apply</button>
</form>
<div id="deleteModal" class="modal">
    <div class="modal-content">
		<h3>Bạn có chắc chắn?</h3>
		<p>Hành động này không thể hoàn tác.</p>
        <div class="modal-buttons">
            <button class="btn cancel">Cancel</button>
            <button id="confirmDeleteBtn" class="btn delete">Delete</button>
        </div>
    </div>
</div>
</div>

<script>
    window.showtimeContext = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/showtime/showtime-list.js?v=1"></script>
