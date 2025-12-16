<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinema/cinema-form.css?v=2">

<h1>Cinema Management</h1>

<a href="${pageContext.request.contextPath}/cinema?action=new" class="add-btn">+ Add Cinema</a>

<div class="cinema-table-container" style="display: flex; gap: 20px; align-items: flex-start;">

    <div class="table-container" style="flex:1 1 auto; min-width:700px; overflow-x:auto;">
        <table class="user-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Cinema Name</th>
                    <th>City</th>
                    <th>Address</th>
                    <th>Phone</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${cinemas}">
                    <tr data-id="${c.cinemaId}">
                        <td>${c.cinemaId}</td>
                        <td>${c.name}</td>
                        <td>${c.city}</td>
                        <td>${c.address}</td>
                        <td>${c.phone}</td>
                        <td>
                            <a class="action edit" href="${pageContext.request.contextPath}/cinema?action=edit&id=${c.cinemaId}">Edit</a> |
                            <a class="action delete" href="#" data-id="${c.cinemaId}" data-type="cinema">Delete</a> |
                            <a class="action" href="https://maps.google.com/?q=${c.address}" target="_blank">Map</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/cinema" class="filter-panel" style="flex-shrink:0; width:230px;">
        <input type="hidden" name="action" value="list"/>
	
        <div class="form-group">
            <label>Search</label>
            <input type="text" name="keyword" placeholder="Search..." value="${param.keyword != null ? param.keyword : ''}" />
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
        <h3>Are you sure?</h3>
        <p>This action cannot be undone.</p>
        <div class="modal-buttons">
            <button class="btn cancel">Cancel</button>
            <button id="confirmDeleteBtn" class="btn delete">Delete</button>
        </div>
    </div>
</div>

<script>
    window.cinemaContext = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/cinema/cinema-list.js?v=7"></script>