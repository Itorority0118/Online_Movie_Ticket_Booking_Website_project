<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="user-table payment-table">
    <thead>
        <tr>
            <th class="col-id">ID</th>
            <th class="col-ticket">Ticket</th>
            <th class="col-method">Method</th>
            <th class="col-amount">Amount</th>
            <th class="col-time">Payment Time</th>
            <th class="col-status">Status</th>
            <th class="col-action">Action</th>
        </tr>
    </thead>

    <tbody>
        <c:forEach var="p" items="${payments}">
            <tr data-id="${p.paymentId}">

                <td class="col-id">${p.paymentId}</td>

                <td class="col-ticket">
                    #${p.ticketId}
                </td>

                <td class="col-method">
                    ${p.paymentMethod}
                </td>

                <td class="col-amount">
                    ${p.amount}
                </td>

                <td class="col-time">
                    ${p.paymentDate}
                </td>

                <td class="col-status">
                    <span class="status ${p.status}">
                        ${p.status}
                    </span>
                </td>

                <td class="col-action">

				    <c:if test="${p.status eq 'Pending'}">
				        <a class="action confirm"
				           href="#"
				           data-id="${p.paymentId}"
				           data-status="Success">
				            Confirm
				        </a>|
				
				        <a class="action cancel"
				           href="#"
				           data-id="${p.paymentId}"
				           data-status="Failed">
				            Cancel
				        </a>
				    </c:if>

                    <c:if test="${p.status eq 'Failed'}">
                        <a class="action delete"
                           href="#"
                           data-id="${p.paymentId}"
                           data-type="payment">
                            Delete
                        </a>
                    </c:if>

                    <c:if test="${p.status eq 'Success'}">
                        <span style="color:#999;">—</span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty payments}">
            <tr>
                <td colspan="7" style="text-align:center;">
                    No payments found
                </td>
            </tr>
        </c:if>
    </tbody>
</table>