<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/room/room-form.css?v=8">

<a href="${pageContext.request.contextPath}/room?action=list&cinemaId=${cinemaId}" class="back-btn">
    <i class="fas fa-arrow-left"></i> Back to Room List
</a>

<h1>Seat Management for Room: ${roomName}</h1>

<a href="${pageContext.request.contextPath}/seat?action=generate&roomId=${param.roomId}"
   class="add-btn">
    + Generate Seat Layout
</a>

<div class="cinema-table-container">

    <div class="table-container" id="seatTableWrapper">
        <c:import url="seat-table.jsp"/>
    </div>

</div>

<script>
    window.seatContext = '${pageContext.request.contextPath}';
</script>

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
<script src="${pageContext.request.contextPath}/js/seat/seat-list.js?v=6" defer></script>