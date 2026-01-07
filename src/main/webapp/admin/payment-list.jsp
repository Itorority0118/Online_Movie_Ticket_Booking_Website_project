<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/room/room-form.css?v=7">
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/payment/payment-form.css?v=2">

<h1>Payment Management</h1>

<div class="cinema-table-container"
     style="display:flex; gap:20px; align-items:flex-start;">

    <!-- TABLE -->
    <div class="table-container"
         id="paymentTableWrapper"
         style="flex:1 1 auto; min-width:700px; overflow-x:auto;">

        <jsp:include page="payment-table.jsp"/>

    </div>

    <!-- FILTER -->
    <form method="get"
          action="${pageContext.request.contextPath}/payment"
          class="filter-panel"
          style="flex-shrink:0; width:230px;">

        <input type="hidden" name="action" value="list"/>

        <!-- STATUS -->
        <div class="form-group">
            <label>Status</label>
            <select name="status">
                <option value="">All</option>
                <option value="PENDING"
                    ${param.status == 'PENDING' ? 'selected' : ''}>
                    Pending
                </option>
                <option value="SUCCESS"
                    ${param.status == 'SUCCESS' ? 'selected' : ''}>
                    Success
                </option>
                <option value="FAILED"
                    ${param.status == 'FAILED' ? 'selected' : ''}>
                    Failed
                </option>
            </select>
        </div>

        <!-- TICKET -->
        <div class="form-group">
            <label>Ticket ID</label>
            <input type="text"
                   name="ticketId"
                   placeholder="Ticket ID"
                   value="${param.ticketId != null ? param.ticketId : ''}"/>
        </div>

        <!-- PAYMENT METHOD -->
        <div class="form-group">
            <label>Payment Method</label>
            <input type="text"
                   name="method"
                   placeholder="Credit Card / ZaloPay / Momo"
                   value="${param.method != null ? param.method : ''}"/>
        </div>

        <button type="submit" class="submit-btn">Apply</button>
    </form>
</div>

<!-- CONFIRM MODAL -->
<div id="confirmModal" class="modal">
    <div class="modal-content">
        <h3>Confirm Payment?</h3>
        <p>This payment will be marked as <b>SUCCESS</b>.</p>
        <div class="modal-buttons">
            <button class="btn cancel">Cancel</button>
            <button id="confirmPaymentBtn" class="btn confirm">Confirm</button>
        </div>
    </div>
</div>

<div id="deleteModal" class="modal">
    <div class="modal-content">
        <h3>Are you sure?</h3>
        <p>This payment will be removed permanently.</p>
        <div class="modal-buttons">
            <button class="btn cancel">Cancel</button>
            <button id="confirmDeleteBtn" class="btn delete">Delete</button>
        </div>
    </div>
</div>

<script>
    window.paymentContext = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/payment/payment-list.js?v=3"></script>