<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
h1 {
    text-align: center;
    font-size: 32px;
    color: #ff4d4d;
    margin-bottom: 40px;
}

.cards {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 15px;
    margin-bottom: 40px;
}

.card {
    background: #161b22;
    border-radius: 12px;
    padding: 20px 15px;
    text-align: center;
    min-width: 130px;
    flex: 1 1 120px;
    font-size: 20px;
    font-weight: bold;
    color: #fff;
    transition: all 0.3s ease;
    border: 1px solid #30363d;
    box-shadow: 0 4px 12px rgba(0,0,0,.4);
}

.card:hover {
    background: #ff0000;
    transform: translateY(-3px);
}

.card .label {
    display: block;
    margin-top: 8px;
    font-size: 12px;
    color: #8b949e;
}

.charts-container {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
    justify-content: center;
    max-width: 900px;
    margin: 0 auto;
}

.chart-card {
    background: #161b22;
    border-radius: 12px;
    padding: 15px;
    flex: 1 1 400px;
    max-width: 48%;
    box-shadow: 0 4px 15px rgba(0,0,0,.4);
}

.chart-card h3 {
    text-align: center;
    margin-bottom: 15px;
    font-size: 18px;
    color: #ff4d4d;
}

canvas {
    width: 100% !important;
    height: auto !important;
    min-height: 300px;
}
@media(max-width:768px) {
    .charts-container canvas {
        height: 280px !important;
        min-height: 280px;
    }
}
</style>
</head>
<body>
<h1>Admin Dashboard</h1>

<div class="cards">
    <div class="card">${totalUsers}<span class="label">Users</span></div>
    <div class="card">${totalAdmins}<span class="label">Admins</span></div>
    <div class="card">${totalActive}<span class="label">Active</span></div>
    <div class="card">${totalInactive}<span class="label">Inactive</span></div>
	<div class="card"><span id="onlineUsers">${onlineUsers}</span><span class="label">Online Users</span></div>
    <div class="card">${totalMovies}<span class="label">Movies</span></div>
    <div class="card">${totalTickets}<span class="label">Tickets</span></div>
</div>

<!-- Charts Section -->
<div class="charts-container">
    <div class="chart-card">
        <h3>User Distribution</h3>
        <canvas id="userChart"></canvas>
    </div>
    <div class="chart-card">
        <h3>Movies & Tickets</h3>
        <canvas id="salesChart"></canvas>
    </div>
</div>

<script>
const ctx1 = document.getElementById('userChart').getContext('2d');
const userChart = new Chart(ctx1, {
    type: 'pie',
    data: {
        labels: ['Active', 'Inactive', 'Admins', 'Online'],
        datasets: [{
            data: [${totalActive}, ${totalInactive}, ${totalAdmins}, ${onlineUsers}],
            backgroundColor: ['#4ecdc4', '#ff6b6b', '#ffd700', '#ff4d4d'],
        }]
    },
    options: {
        plugins: {
            legend: { position: 'bottom', labels: { color: '#fff' } },
        }
    }
});

const ctx2 = document.getElementById('salesChart').getContext('2d');
const salesChart = new Chart(ctx2, {
    type: 'bar',
    data: {
        labels: ['Movies', 'Tickets Sold'],
        datasets: [{
            label: 'Count',
            data: [${totalMovies}, ${totalTickets}],
            backgroundColor: ['#ff4d4d', '#4ecdc4']
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: { display: false },
        },
        scales: {
            x: { ticks: { color: '#fff' }, grid: { color: '#30363d' } },
            y: { ticks: { color: '#fff' }, grid: { color: '#30363d' }, beginAtZero: true }
        }
    }
});
function updateOnlineUsers() {
    fetch('${pageContext.request.contextPath}/online-users')
        .then(response => response.json())
        .then(data => {
            document.getElementById('onlineUsers').textContent = data.onlineUsers;

            userChart.data.datasets[0].data[3] = data.onlineUsers;
            userChart.update();
        })
        .catch(err => console.error('Error fetching online users:', err));
}
updateOnlineUsers();
setInterval(updateOnlineUsers, 5000);
</script>
</body>
</html>