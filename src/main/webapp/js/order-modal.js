console.log("ORDER MODAL JS LOADED");

const HOLD_TIME = 5 * 60 * 1000;
window.selectedTickets = [];
let countdownIntervals = [];

document.addEventListener("DOMContentLoaded", () => {
    const orderBtn = document.getElementById("orderBtn");
    if (orderBtn) orderBtn.addEventListener("click", openOrderModal);
});

window.openOrderModal = async function () {
    const modal = document.getElementById("orderModal");
    const ticketsBox = document.getElementById("orderTickets");
    if (!modal || !ticketsBox) return;

    if (!window.IS_LOGGED_IN) {
        alert("Vui lòng đăng nhập để xem đơn hàng");
        return;
    }

    modal.style.display = "flex";
    setTimeout(() => modal.classList.add("show"), 10);
    modal.onclick = e => { if (e.target === modal) closeOrderModal(); };

    ticketsBox.innerHTML = "<p>Đang tải...</p>";

    clearAllCountdowns();

    try {
        const res = await fetch(`${APP_CONTEXT}/order?action=ajax`, {
            headers: { "X-Requested-With": "XMLHttpRequest" }
        });

        if (!res.ok) throw new Error("HTTP " + res.status);

        const data = await res.json();

        if (!Array.isArray(data) || data.length === 0) {
            ticketsBox.innerHTML = "<p>Chưa có vé</p>";
            selectedTickets = [];
            renderPayment();
            return;
        }

		selectedTickets = data.map(t => ({
		    id: String(t.id),
		    movie: t.movie || "",
		    seat: t.seat || "",
		    price: Number(t.price),
		    startTime: t.startTime || "",
		    showDate: t.showDate || "",
		    room: t.room || "",
		    cinema: t.cinema || "",
		    holdTime: Number(t.holdTime)
		}));

        renderOrderTickets(selectedTickets);
        renderPayment();

    } catch (err) {
        console.error(err);
        ticketsBox.innerHTML = "<p>Lỗi tải vé</p>";
    }
};

function renderOrderTickets(tickets) {
    const box = document.getElementById("orderTickets");
    if (!box) return;

    box.innerHTML = tickets.map(t => {
        let showtimeStr = '';
        if (t.startTime) {
            const dt = new Date(t.startTime.replace(" ", "T"));
            if (!isNaN(dt)) {
                showtimeStr = dt.toLocaleString('vi-VN', {
                    dateStyle: 'short',
                    timeStyle: 'short'
                });
            }
        }

        return `
            <div class="order-item hold-timer"
                 data-id="${t.id}"
                 data-booking-time="${t.holdTime}">

                <label>
                    <input type="checkbox"
                           checked
                           data-id="${t.id}"
                           data-movie="${t.movie}"
                           data-seat="${t.seat}"
                           data-price="${t.price}"
                           data-room="${t.room}"
                           data-cinema="${t.cinema}"
                           data-showtime="${showtimeStr}"
                           onchange="toggleTicket(this)">
                    🎬 ${t.movie} – ${t.seat}
                </label>

                <span class="order-price">
                    ${t.price.toLocaleString("vi-VN")} đ
                </span>

                <span class="countdown"></span>

                <div class="ticket-actions">
                    <button class="ticket-detail-btn"
                            onclick="viewTicketDetail('${t.id}')">
                        Chi tiết
                    </button>
                    <span class="divider">|</span>
                    <button class="ticket-cancel-btn"
                            onclick="cancelHold(this, '${t.id}')">
                        Hủy
                    </button>
                </div>
            </div>
        `;
    }).join("");

    initHoldCountdown();
}

function initHoldCountdown() {
    document.querySelectorAll(".hold-timer").forEach(timer => {

        const bookingTime = Number(timer.dataset.bookingTime);
        const countdownEl = timer.querySelector(".countdown");
        const ticketId = timer.dataset.id;

        if (!bookingTime || !countdownEl) return;

        const interval = setInterval(() => {
            const diff = HOLD_TIME - (Date.now() - bookingTime);

            if (diff <= 0) {
                clearInterval(interval);
                timer.innerHTML =
                    "<span style='color:red'>❌ Hết thời gian giữ ghế</span>";

                selectedTickets = selectedTickets.filter(t => t.id !== ticketId);
                renderPayment();
                return;
            }

            const m = Math.floor(diff / 60000);
            const s = Math.floor((diff % 60000) / 1000);

            countdownEl.innerText =
                `⏳ ${String(m).padStart(2, "0")}:${String(s).padStart(2, "0")}`;
        }, 1000);

        countdownIntervals.push(interval);
    });
}

function clearAllCountdowns() {
    countdownIntervals.forEach(i => clearInterval(i));
    countdownIntervals = [];
}

window.toggleTicket = function (cb) {
    const id = cb.dataset.id;

    if (cb.checked) {
        if (!selectedTickets.find(t => t.id === id)) {
            selectedTickets.push({
                id,
                movie: cb.dataset.movie,
                seat: cb.dataset.seat,
                price: Number(cb.dataset.price)
            });
        }
    } else {
        selectedTickets = selectedTickets.filter(t => t.id !== id);
    }

    renderPayment();
};

window.renderPayment = function () {
    const totalEl = document.getElementById("orderTotalPrice");
    const payBtn = document.querySelector(".btn-confirm");
    if (!totalEl) return;

    const total = selectedTickets.reduce((s, t) => s + t.price, 0);
    totalEl.innerText = total.toLocaleString("vi-VN") + " đ";

    if (payBtn) payBtn.disabled = selectedTickets.length === 0;
};

window.cancelHold = function (btn, ticketId) {
    if (!confirm("Bạn có chắc muốn bỏ vé này?")) return;

    fetch(`${APP_CONTEXT}/order?action=cancelHold`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `ticketId=${ticketId}`
    })
    .then(r => r.json())
    .then(res => {
        if (res.success) {
            btn.closest(".order-item")?.remove();
            selectedTickets = selectedTickets.filter(t => t.id !== ticketId);
            renderPayment();
        } else {
            alert("Không thể hủy vé");
        }
    });
};

window.checkoutOrder = function () {
    if (!selectedTickets.length) {
        alert("Vui lòng chọn vé trước khi thanh toán");
        return;
    }

    lockPayBtn(true);

    fetch(`${APP_CONTEXT}/order`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
		body: `action=checkout&paymentMethod=Online`
    })
    .then(r => r.json())
    .then(res => {
        if (res.success) {
			closeOrderModal();      
			openOrderSuccessModal(); 
        } else {
            alert("❌ Thanh toán thất bại");
            lockPayBtn(false);
        }
    })
    .catch(() => {
        alert("❌ Lỗi hệ thống");
        lockPayBtn(false);
    });
};

window.closeOrderModal = function () {
    clearAllCountdowns();
    const modal = document.getElementById("orderModal");
    if (!modal) return;
    modal.classList.remove("show");
    setTimeout(() => modal.style.display = "none", 300);
};

window.lockPayBtn = function (lock) {
    const btn = document.querySelector(".btn-confirm");
    if (!btn) return;
    btn.disabled = lock;
    btn.innerText = lock ? "Đang xử lý..." : "Thanh toán";
};

//
window.openOrderSuccessModal = function () {
    const modal = document.getElementById("orderSuccessModal");
    if (!modal) return;

    modal.style.display = "flex";
    setTimeout(() => modal.classList.add("show"), 10);

    modal.onclick = e => {
        if (e.target === modal) closeOrderSuccessModal();
    };
};

window.closeOrderSuccessModal = function () {
    const modal = document.getElementById("orderSuccessModal");
    if (!modal) return;

    modal.classList.remove("show");
    setTimeout(() => modal.style.display = "none", 300);
};

window.goToMyTickets = function () {
    window.location.href = `${APP_CONTEXT}/user/tickets`;
};

window.viewTicketDetail = function(ticketId) {
    const t = selectedTickets.find(x => x.id === ticketId);
    if (!t) return;

    let showtimeStr = '';
    if (t.startTime) {
        const dt = new Date(t.startTime.replace(" ", "T"));
        if (!isNaN(dt)) {
            showtimeStr = dt.toLocaleString('vi-VN', {
                dateStyle: 'short',
                timeStyle: 'short'
            });
        }
    }

    const content = `
        <p>🎬 <b>Phim:</b> ${t.movie}</p>
        <p>🎟 <b>Ghế:</b> ${t.seat}</p>
        <p>🏢 <b>Rạp:</b> ${t.cinema}</p>
        <p>🎥 <b>Phòng:</b> ${t.room}</p>
        <p>📅 <b>Ngày giờ:</b> ${showtimeStr}</p>
        <p>💰 <b>Giá:</b> ${t.price.toLocaleString("vi-VN")} đ</p>
    `;

    const modal = document.getElementById("ticketDetailModal");
    const contentBox = document.getElementById("ticketDetailContent");
    contentBox.innerHTML = content;

    modal.classList.add("show");

    modal.onclick = e => {
        if (e.target === modal) closeTicketDetailModal();
    };
};

window.closeTicketDetailModal = function() {
    const modal = document.getElementById("ticketDetailModal");
    modal.classList.remove("show");
};