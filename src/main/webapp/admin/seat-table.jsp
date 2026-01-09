<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${empty seatMap}">
	<p style="text-align: center;">No seats found</p>
</c:if>

<div class="seat-map">
	<c:forEach var="entry" items="${seatMap}">

		<div class="seat-row">

			<!-- Row label -->
			<span class="row-label">${entry.key}</span>

			<!-- Seats in row -->
			<div class="seat-row-seats">
				<c:forEach var="s" items="${entry.value}">
					<div
						class="seat
                        ${s.seatType.toLowerCase()}
                        ${s.status.toLowerCase()}"
						title="Seat ${s.seatNumber}">

						<span class="seat-number">${s.seatCol}</span>

						<div class="seat-actions">
							<a
								href="${pageContext.request.contextPath}/seat?action=edit&id=${s.seatId}">✏</a>
							<a href="#" class="delete-seat" data-id="${s.seatId}">🗑</a>
						</div>
					</div>
				</c:forEach>
			</div>

		</div>

	</c:forEach>
</div>