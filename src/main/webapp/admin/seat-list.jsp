<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/room/room-form.css?v=1">

<a href="${pageContext.request.contextPath}/room?action=list&cinemaId=${cinemaId}" class="back-btn">
    <i class="fas fa-arrow-left"></i> Back to Room List
</a>

<h1>Seat Management for Room: ${roomName}</h1>

<a href="${pageContext.request.contextPath}/seat?action=new&roomId=${param.roomId}" class="add-btn">
    + Add Seat
</a>

<div class="cinema-table-container" style="display:flex; gap:20px; align-items:flex-start;">

    <div class="table-container"
         id="seatTableWrapper"
         style="flex:1 1 auto; min-width:700px; overflow-x:auto;">

        <jsp:include page="seat-table.jsp"/>
    </div>

    <form method="get"
          action="${pageContext.request.contextPath}/seat"
          class="filter-panel"
          style="flex-shrink:0; width:230px;">

        <input type="hidden" name="action" value="list"/>

        <c:if test="${param.roomId != null}">
            <input type="hidden" name="roomId" value="${param.roomId}"/>
        </c:if>

        <div class="form-group">
            <label>Search Seat</label>
            <input type="text"
                   name="keyword"
                   placeholder="Seat number..."
                   value="${param.keyword != null ? param.keyword : ''}" />
        </div>
        
        <div class="form-group">
    		<label>Seat Type</label>
		    <select name="seatType">
		        <option value="">All</option>
		        <option value="Regular" ${param.seatType == 'Regular' ? 'selected' : ''}>Regular</option>
		        <option value="VIP" ${param.seatType == 'VIP' ? 'selected' : ''}>VIP</option>
		        <option value="Double" ${param.seatType == 'Double' ? 'selected' : ''}>Double</option>
		    </select>
		</div>
        

        <div class="form-group">
            <label>Status</label>
            <select name="status">
                <option value="">All</option>
                <option value="Available" ${param.status == 'Available' ? 'selected' : ''}>Available</option>
                <option value="Booked" ${param.status == 'Booked' ? 'selected' : ''}>Booked</option>
                <option value="Unavailable" ${param.status == 'Unavailable' ? 'selected' : ''}>Unavailable</option>
            </select>
        </div>

        <button type="submit" class="submit-btn">Apply</button>
    </form>
</div>

<div id="deleteModal" class="modal">
    <div class="modal-content">
        <h3>Are you sure?</h3>
        <p>This action cannot be undone.</p>
        <div class="modal-buttons">
            <button class="btn cancel">Cancel</button>
            <button id="confirmDeleteBtn" class="btn delete">Delete</button>
        </div>
    </div>
</div>

<script>
    window.seatContext = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/seat/seat-list.js?v=1"></script>
