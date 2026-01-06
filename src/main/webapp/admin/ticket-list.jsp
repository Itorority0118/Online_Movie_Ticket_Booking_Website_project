<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/room/room-form.css?v=7">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ticket/ticket-form.css?v=1">
<h1>Ticket Management</h1>

<div class="cinema-table-container"
     style="display:flex; gap:20px; align-items:flex-start;">

    <div class="table-container"
         id="ticketTableWrapper"
         style="flex:1 1 auto; min-width:700px; overflow-x:auto;">

        <jsp:include page="ticket-table.jsp"/>

    </div>

	<form method="get"
	      action="${pageContext.request.contextPath}/ticket"
	      class="filter-panel"
	      style="flex-shrink:0; width:230px;">

        <input type="hidden" name="action" value="list"/>

        <div class="form-group">
            <label>Status</label>
            <select name="status">
                <option value="">All</option>
                <option value="Booked"
                    ${param.status == 'Booked' ? 'selected' : ''}>
                    Booked
                </option>
                <option value="Cancelled"
                    ${param.status == 'Cancelled' ? 'selected' : ''}>
                    Cancelled
                </option>
                <option value="Used"
                    ${param.status == 'Used' ? 'selected' : ''}>
                    Used
                </option>
            </select>
        </div>

        <!-- USER -->
        <div class="form-group">
            <label>User ID</label>
            <input type="text"
                   name="userId"
                   placeholder="User ID"
                   value="${param.userId != null ? param.userId : ''}"/>
        </div>

        <!-- SHOWTIME -->
        <div class="form-group">
            <label>Showtime ID</label>
            <input type="text"
                   name="showtimeId"
                   placeholder="Showtime ID"
                   value="${param.showtimeId != null ? param.showtimeId : ''}"/>
        </div>

        <button type="submit" class="submit-btn">Apply</button>
    </form>
</div>

<div id="cancelModal" class="modal">
    <div class="modal-content">
        <h3>Cancel Ticket?</h3>
        <p>This ticket will be marked as <b>Cancelled</b>.</p>
        <div class="modal-buttons">
            <button class="btn cancel">Cancel</button>
            <button id="confirmCancelBtn" class="btn delete">Confirm</button>
        </div>
    </div>
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
    window.ticketContext = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/ticket/ticket-list.js?v=2"></script>