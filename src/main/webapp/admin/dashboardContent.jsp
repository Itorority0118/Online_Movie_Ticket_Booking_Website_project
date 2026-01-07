<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
/* ===== BODY & HEADINGS ===== */
body { 
    background-color: #0d1117; 
    color: #fff; 
    font-family: 'Segoe UI', sans-serif; 
    margin:0; padding:0; 
}
h1 { 
    text-align:center; 
    font-size:28px;
    color:#ff4d4d; 
    margin-bottom:30px; 
}
.cards-section h2 { 
    color:#ff4d4d; 
    margin-left:15px; 
    margin-bottom:8px; 
}

/* ===== CARDS LAYOUT ===== */
.cards-section { margin-bottom:30px; }
.cards { 
    display:flex; 
    flex-wrap:wrap; 
    justify-content:center; 
    gap:12px; 
    margin-bottom:15px; 
}
.card { 
    background:#161b22; 
    border-radius:10px; 
    padding:15px 10px; 
    text-align:center; 
    min-width:110px; 
    flex:1 1 100px; 
    font-size:18px; 
    font-weight:bold; 
    color:#fff; 
    transition: all 0.3s ease; 
    border:1px solid #30363d; 
    box-shadow:0 3px 10px rgba(0,0,0,.4);
}
.card:hover { 
    background:#ff0000; 
    transform:translateY(-2px); 
}
.card .label { 
    display:block; 
    margin-top:6px; 
    font-size:11px; 
    color:#8b949e; 
}

/* ===== MAIN CHART CARDS ===== */
.charts-container { 
    display:flex; 
    flex-wrap:wrap; 
    justify-content:center;  
    gap:12px; 
    margin:0 auto; 
    max-width:900px; 
}
.chart-card { 
    background:#161b22; 
    border-radius:10px; 
    padding:8px 10px; 
    flex: 0 0 220px; 
    box-shadow:0 2px 8px rgba(0,0,0,.4); 
    margin-bottom:12px; 
    text-align:center; 
}
.chart-card h3 { 
    margin-bottom:8px; 
    font-size:14px; 
    color:#ff4d4d; 
}
canvas { 
    width:100% !important; 
    height:130px !important;  /* giảm chiều cao để gọn hơn */
    min-height:130px !important;
}

/* ===== RESPONSIVE ===== */
@media(max-width:768px) { 
    .chart-card { flex:1 1 100%; }
    canvas { height:130px !important; min-height:130px; } 
}
</style>
</head>
<body>

<h1>Admin Dashboard</h1>

<!-- ===== USER SECTION ===== -->
<div class="cards-section">
    <h2>User</h2>
    <div class="cards">
        <div class="card"><c:out value="${totalUsers}" default="0"/><span class="label">Users</span></div>
        <div class="card"><c:out value="${totalAdmins}" default="0"/><span class="label">Admins</span></div>
        <div class="card"><c:out value="${totalActive}" default="0"/><span class="label">Active</span></div>
        <div class="card"><c:out value="${totalInactive}" default="0"/><span class="label">Inactive</span></div>
    </div>
    <div class="chart-card">
        <h3>User Distribution</h3>
        <canvas id="userChart"></canvas>
    </div>
</div>

<!-- ===== CINEMA SECTION ===== -->
<div class="cards-section">
    <h2>Cinema</h2>
    <div class="cards">
        <div class="card"><c:out value="${totalMovies}" default="0"/><span class="label">Movies</span></div>
        <div class="card"><c:out value="${totalTickets}" default="0"/><span class="label">Total Tickets</span></div>
        <div class="card"><c:out value="${totalUsed}" default="0"/><span class="label">Used</span></div>
        <div class="card"><c:out value="${totalBooked}" default="0"/><span class="label">Booked</span></div>
        <div class="card"><c:out value="${totalHold}" default="0"/><span class="label">HOLD</span></div>
        <div class="card"><c:out value="${totalCancelled}" default="0"/><span class="label">Cancelled</span></div>
    </div>
    <div class="chart-card">
        <h3>Tickets Status Distribution</h3>
        <canvas id="ticketsStatusChart"></canvas>
    </div>
</div>

<!-- ===== REVENUE SECTION ===== -->
<div class="cards-section">
    <h2>Revenue</h2>
    <div class="cards">
        <div class="card"><c:out value="${totalRevenue}" default="0"/><span class="label">Total Revenue</span></div>
    </div>
    <div class="chart-card">
        <h3>Revenue Breakdown</h3>
        <canvas id="revenueChart"></canvas>
    </div>
</div>

<!-- ===== CHARTS JS ===== -->
<script>
// ===== DATA =====
const totalUsers = Number('<c:out value="${totalUsers}" default="0"/>');
const totalAdmins = Number('<c:out value="${totalAdmins}" default="0"/>');
const totalActive = Number('<c:out value="${totalActive}" default="0"/>');
const totalInactive = Number('<c:out value="${totalInactive}" default="0"/>');

const totalUsed = Number('<c:out value="${totalUsed}" default="0"/>');
const totalBooked = Number('<c:out value="${totalBooked}" default="0"/>');
const totalHold = Number('<c:out value="${totalHold}" default="0"/>');
const totalCancelled = Number('<c:out value="${totalCancelled}" default="0"/>');

const revenueSuccess = Number('<c:out value="${totalRevenueSuccess}" default="0"/>');
const revenueFailed = Number('<c:out value="${totalRevenueFailed}" default="0"/>');
const revenuePending = Number('<c:out value="${totalRevenuePending}" default="0"/>');

// ===== MAIN CHARTS =====

// --- USER CHART ---
if(totalActive + totalInactive + totalAdmins > 0){
    new Chart(document.getElementById('userChart').getContext('2d'), {
        type:'pie',
        data:{
            labels:['Active','Inactive','Admins'],
            datasets:[{ data:[totalActive,totalInactive,totalAdmins], backgroundColor:['#4ecdc4','#ff6b6b','#ffd700'] }]
        },
        options:{ 
            plugins:{ legend:{ position:'bottom', labels:{ color:'#fff', boxWidth:12, padding:10 } } },
            responsive:true,
            maintainAspectRatio:false
        }
    });
}else{
    document.getElementById('userChart').parentElement.innerHTML = '<p style="color:#fff; text-align:center;">No user data</p>';
}

// --- TICKETS CHART ---
const ticketsData = [totalUsed,totalBooked,totalHold,totalCancelled];
if(ticketsData.reduce((a,b)=>a+b,0) > 0){
    new Chart(document.getElementById('ticketsStatusChart').getContext('2d'), {
        type:'pie',
        data:{
            labels:['Used','Booked','HOLD','Cancelled'],
            datasets:[{ data:ticketsData, backgroundColor:['#4CAF50','#4ecdc4','#ffd700','#ff4d4d'] }]
        },
        options:{ 
            plugins:{ legend:{ position:'bottom', labels:{ color:'#fff', boxWidth:12, padding:10 } } },
            responsive:true,
            maintainAspectRatio:false
        }
    });
}else{
    document.getElementById('ticketsStatusChart').parentElement.innerHTML = '<p style="color:#fff; text-align:center;">No ticket data</p>';
}

// --- REVENUE CHART ---
const revenueData = [revenueSuccess,revenueFailed,revenuePending];
if(revenueData.reduce((a,b)=>a+b,0) > 0){
    new Chart(document.getElementById('revenueChart').getContext('2d'), {
        type:'pie',
        data:{
            labels:['Success','Failed','Pending'],
            datasets:[{ data:revenueData, backgroundColor:['#4CAF50','#ff4d4d','#ffa500'] }]
        },
        options:{ 
            plugins:{ legend:{ position:'bottom', labels:{ color:'#fff', boxWidth:12, padding:10 } } },
            responsive:true,
            maintainAspectRatio:false
        }
    });
}else{
    document.getElementById('revenueChart').parentElement.innerHTML = '<p style="color:#fff; text-align:center;">No revenue data</p>';
}
</script>

</body>
</html>