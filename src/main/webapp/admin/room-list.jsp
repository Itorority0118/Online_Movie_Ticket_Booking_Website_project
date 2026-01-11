<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/room/room-form.css?v=5">

    <a href="${pageContext.request.contextPath}/cinema?action=list" class="back-btn">
        <i class="fas fa-arrow-left"></i> Back to Cinema List
    </a>
    
<h1>Room Management for Cinema: ${cinemaName}</h1>
<a href="${pageContext.request.contextPath}/room?action=new&cinemaId=${param.cinemaId}"
   class="add-btn">
    + Add Room
</a>

<div class="cinema-table-container" style="display:flex; gap:20px; align-items:flex-start;">

    <div class="table-container"
         id="roomTableWrapper"
         style="flex:1 1 auto; min-width:700px; overflow-x:auto;">

        <jsp:include page="room-table.jsp"/>

    </div>

    <form method="get"
          action="${pageContext.request.contextPath}/room"
          class="filter-panel"
          style="flex-shrink:0; width:230px;">

        <input type="hidden" name="action" value="list"/>

        <c:if test="${param.cinemaId != null}">
            <input type="hidden" name="cinemaId" value="${param.cinemaId}"/>
        </c:if>

        <div class="form-group">
            <label>Search Room</label>
            <input type="text"
                   name="keyword"
                   placeholder="Room name..."
                   value="${param.keyword != null ? param.keyword : ''}" />
        </div>

        <div class="form-group">
            <label>Room Type</label>
            <select name="roomType">
                <option value="">All</option>
                <option value="2D" ${param.roomType == '2D' ? 'selected' : ''}>2D</option>
                <option value="3D" ${param.roomType == '3D' ? 'selected' : ''}>3D</option>
                <option value="IMAX" ${param.roomType == 'IMAX' ? 'selected' : ''}>IMAX</option>
            </select>
        </div>

        <button type="submit" class="submit-btn">Apply</button>
    </form>
</div>

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

<script>
    window.roomContext = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/room/room-list.js?v=5"></script>
