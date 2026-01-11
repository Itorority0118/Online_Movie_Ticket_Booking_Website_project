<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinema/cinema-form.css?v=10">

<h1>Cinema Management</h1>

<a href="${pageContext.request.contextPath}/cinema?action=new" class="add-btn">+ Add Cinema</a>

<div class="cinema-table-container" style="display:flex; gap:20px; align-items:flex-start;">

    <div id="cinemaTableWrapper" class="table-container"
         style="flex:1 1 auto; min-width:700px; overflow-x:auto;">
        <jsp:include page="cinema-table.jsp"/>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/cinema"
          class="filter-panel" style="flex-shrink:0; width:230px;">
        <input type="hidden" name="action" value="list"/>
        <div class="form-group">
            <label>Search</label>
            <input type="text" name="keyword" placeholder="Search..."
                   value="${param.keyword != null ? param.keyword : ''}"/>
        </div>
        <div class="form-group">
            <label>City</label>
            <select name="city">
                <option value="">All</option>
                <c:forEach var="city" items="${cityList}">
                    <option value="${city}" <c:if test="${param.city == city}">selected</c:if>>
                        ${city}
                    </option>
                </c:forEach>
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
            <button class="btn cancel" type="button">Cancel</button>
            <button id="confirmDeleteBtn" class="btn delete" type="button">Delete</button>
        </div>
    </div>
</div>

<script>
    window.cinemaContext = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/cinema/cinema-list.js?v=14"></script>