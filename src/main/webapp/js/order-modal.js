console.log("ORDER MODAL JS LOADED");

window.selectedTickets = [];

// ===== CHECKBOX =====
window.toggleTicket = function (cb) {
    const ticket = {
        id: cb.dataset.id,
        movie: cb.dataset.movie,
        seat: cb.dataset.seat,
        price: Number(cb.dataset.price)
    };

    if (cb.checked) {
        // tránh trùng
        if (!selectedTickets.find(t => t.id === ticket.id)) {
            selectedTickets.push(ticket);
        }
    } else {
        selectedTickets =
            selectedTickets.filter(t => t.id !== ticket.id);
    }

    renderPayment();
};

// ===== RENDER =====
window.renderPayment = function () {
    const box = document.getElementById("selectedTickets");
    const totalEl = document.getElementById("orderTotalPrice");
    const payBtn = document.querySelector(".pay-all-btn");

    if (!box || !totalEl) return;

    if (selectedTickets.length === 0) {
        box.innerHTML = "<p>Chưa chọn vé</p>";
        totalEl.innerText = "0 đ";
        payBtn.disabled = true;
        return;
    }

    let html = "";
    let total = 0;

    selectedTickets.forEach(t => {
        total += t.price;
        html += `<p>🎬 ${t.movie} – ${t.seat}</p>`;
    });

    box.innerHTML = html;
    totalEl.innerText = total.toLocaleString("vi-VN") + " đ";
    payBtn.disabled = false;
};

window.initOrderModal = function () {

    console.log("🔄 INIT ORDER MODAL");

    // reset state
    selectedTickets = [];

    document
        .querySelectorAll(".order-check input[type='checkbox']:checked")
        .forEach(cb => {
            selectedTickets.push({
                id: cb.dataset.id,
                movie: cb.dataset.movie,
                seat: cb.dataset.seat,
                price: Number(cb.dataset.price)
            });
        });

    renderPayment();
};
function openPaymentModal() {

    if (!window.selectedTickets || selectedTickets.length === 0) {
        alert("Vui lòng chọn vé trước khi thanh toán");
        return;
    }

    fetch(APP_CONTEXT + "/payment?showtimeId=" + CURRENT_SHOWTIME_ID)
        .then(res => {
            if (!res.ok) throw new Error("Không mở được payment");
            return res.text();
        })
        .then(html => {
            document.body.insertAdjacentHTML("beforeend", html);
        })
        .catch(err => {
            console.error(err);
            alert("Không thể mở cửa sổ thanh toán");
        });
}

function closePaymentModal() {
    const modal = document.getElementById("paymentModal");
    if (modal) modal.remove();
}

function confirmPayment() {

    lockPayBtn(true);

    fetch(APP_CONTEXT + "/order", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body:
            "action=checkout" +
            "&showtimeId=" + CURRENT_SHOWTIME_ID +
            "&paymentMethod=ONLINE"
    })
    .then(r => r.json())
    .then(res => {
        if (res.success) {
            alert("🎉 Thanh toán thành công!");
            location.reload();
        } else {
            alert("❌ Thanh toán thất bại");
            lockPayBtn(false);
        }
    })
    .catch(() => {
        alert("❌ Lỗi hệ thống");
        lockPayBtn(false);
    });
}

function lockPayBtn(lock) {
    const btn = document.getElementById("confirmPayBtn");
    if (!btn) return;

    btn.disabled = lock;
    btn.innerText = lock ? "Đang xử lý..." : "Xác nhận thanh toán";
}
