console.log("COMMON JS LOADED");

document.addEventListener("click", e => {

    const logoutLink = e.target.closest("a[href*='action=logout']");
    if (logoutLink) {
        return;
    }

});

function toggleUserDropdown() {
    const dropdown = document.getElementById("userDropdown");
    dropdown.style.display =
        dropdown.style.display === "block" ? "none" : "block";
}

function openProfileModal() {
    document.getElementById("profileModal").style.display = "block";
    document.getElementById("userDropdown").style.display = "none";
}

function closeProfileModal() {
    document.getElementById("profileModal").style.display = "none";
}

function openOrderModal() {
    const modal = document.getElementById("orderModal");
    const content = document.getElementById("orderContent");

    modal.style.display = "flex";
    content.innerHTML = "Đang tải...";

    fetch(window.APP_CONTEXT + "/order?action=ajax")
        .then(res => {
            if (res.status === 401) {
                content.innerHTML = "Vui lòng đăng nhập";
                return;
            }
            return res.text();
        })
		.then(html => {
		    if (html) {
		        content.innerHTML = html;

		        // 🔥 BẮT BUỘC PHẢI CÓ
		        if (window.initOrderModal) {
		            initOrderModal();
		        }

		        // nếu bạn có countdown giữ ghế
		        if (window.initHoldCountdown) {
		            initHoldCountdown();
		        }
		    }
		})
        .catch(err => {
            content.innerHTML = "Lỗi tải đơn hàng";
            console.error(err);
        });
}


function closeOrderModal() {
    document.getElementById("orderModal").style.display = "none";
}

function openMovieModal(
    title,
    genre,
    duration,
    description,
    poster,
    trailer,
    movieId,
    autoOpenBooking = false
) {
    resetBookingState();

    modalMovieId = movieId;

    document.getElementById("modalTitle").innerText = title;
    document.getElementById("modalGenre").innerText = genre;
    document.getElementById("modalDuration").innerText = duration;
    document.getElementById("modalDescription").innerText = description;
    document.getElementById("modalPoster").src = poster;
    document.getElementById("modalTrailer").href = trailer;

    document.getElementById("movieModal").style.display = "flex";

    // 🔥 LOAD THÀNH PHỐ (THAY VÌ LOAD RẠP)
    loadCitiesInModal();

    if (autoOpenBooking) {
        setTimeout(() => {
            document
                .querySelector(".booking-section")
                .scrollIntoView({ behavior: "smooth" });
        }, 200);
    }
}


function closeMovieModal() {
    resetBookingState();
    document.getElementById("movieModal").style.display = "none";
}

function loadCitiesInModal() {
    fetch(`${APP_CONTEXT}/cinema?action=citiesByMovie&movieId=${modalMovieId}`)
        .then(r => r.json())
        .then(data => {
            const citySelect = document.getElementById("citySelect");
            citySelect.innerHTML = `<option value="">-- Chọn thành phố --</option>`;

            data.forEach(city => {
                citySelect.innerHTML +=
                    `<option value="${city}">${city}</option>`;
            });

            // reset rạp
            const cinemaSelect = document.getElementById("cinemaSelect");
            cinemaSelect.innerHTML = `<option value="">-- Chọn rạp --</option>`;
            cinemaSelect.disabled = true;
        });
}


function loadCinemasByCity() {
    const city = document.getElementById("citySelect").value;
    const cinemaSelect = document.getElementById("cinemaSelect");

    if (!city) {
        cinemaSelect.innerHTML = `<option value="">-- Chọn rạp --</option>`;
        cinemaSelect.disabled = true;
        return;
    }

    fetch(`${APP_CONTEXT}/cinema?action=byMovieCity&movieId=${modalMovieId}&city=${encodeURIComponent(city)}`)
        .then(r => r.json())
        .then(data => {
            cinemaSelect.innerHTML = `<option value="">-- Chọn rạp --</option>`;

            if (data.length === 0) {
                cinemaSelect.innerHTML += `<option disabled>Không có rạp</option>`;
                cinemaSelect.disabled = true;
                return;
            }

            data.forEach(c => {
                cinemaSelect.innerHTML +=
                    `<option value="${c.cinemaId}">${c.name}</option>`;
            });

            cinemaSelect.disabled = false;
        });
}

function loadCinemasInModal() {
	fetch(`${APP_CONTEXT}/cinema?action=byMovie&movieId=${modalMovieId}`)
	    .then(r => {
	        if (!r.ok) {
	            throw new Error("HTTP error " + r.status);
	        }
	        return r.json();
	    })
	    .then(data => {
	        const select = document.getElementById("cinemaSelect");
	        select.innerHTML = `<option value="">-- Chọn rạp --</option>`;

	        if (data.length === 0) {
	            select.innerHTML += `<option disabled>Không có rạp chiếu</option>`;
	        }

	        data.forEach(c => {
	            select.innerHTML +=
	                `<option value="${c.cinemaId}">${c.name}</option>`;
	        });
	    })
	    .catch(err => {
	        console.error("LOAD CINEMA ERROR:", err);
	    });

}

function formatTimeOnly(datetimeStr) {
    const date = new Date(datetimeStr);
    return date.toLocaleTimeString('vi-VN', {
        hour: '2-digit',
        minute: '2-digit'
    });
}


function loadShowtimesInModal() {
    const cinemaId = document.getElementById("cinemaSelect").value;
    if (!cinemaId) return;

    fetch(`${APP_CONTEXT}/showtime?action=byMovieCinema&movieId=${modalMovieId}&cinemaId=${cinemaId}`)
        .then(r => r.json())
        .then(data => {
            const box = document.getElementById("showtimeList");
            box.innerHTML = "";

			data.forEach(s => {
			    box.innerHTML += `
			        <button 
			            data-showtime-id="${s.showtimeId}"
			            data-ticket-price="${s.ticketPrice}"
			            onclick="selectShowtimeInModal(${s.showtimeId}, this)">
			            ${formatTimeOnly(s.startTime)}
			        </button>
			    `;
			});
        });
}

function selectShowtimeInModal(id, btn) {

    if (!window.IS_LOGGED_IN) {
        window.location.href =
          `${APP_CONTEXT}/login.jsp?redirect=${encodeURIComponent(location.href)}`;
        return;
    }

    // 🔥 NẾU CHỌN KHUNG GIỜ KHÁC → RESET VÉ CŨ
    if (modalShowtimeId && modalShowtimeId !== id) {
        resetSeatForNewShowtime();
    }

    modalShowtimeId = id;
	showtimeBasePrice = Number(btn.dataset.ticketPrice);
	updateTotal(); // tính lại tiền ngay khi đổi gi

    document.querySelectorAll("#showtimeList button")
        .forEach(b => b.classList.remove("active"));

    btn.classList.add("active");

    document.getElementById("seatModal").style.display = "flex";
    loadSeats();
}


function buyTicketInModal() {
    if (!modalShowtimeId || selectedSeats.length === 0) {
        alert("Vui lòng chọn ghế");
        return;
    }

    const seatIds = selectedSeats.map(s => s.seatId).join(",");

    fetch(`${APP_CONTEXT}/ticket`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body:
            `action=book`
            + `&showtimeId=${modalShowtimeId}`
            + `&seatIds=${seatIds}`
    })
    .then(res => res.json())
    .then(data => {
        if (!data.success) throw new Error();
        alert("🎉 Mua vé thành công");
        closeMovieModal();
    })
    .catch(() => {
        alert("❌ Mua vé thất bại");
    });
}

function addToCartInModal() {
    if (!modalShowtimeId || selectedSeats.length === 0) {
        alert("Vui lòng chọn ghế");
        return;
    }

    const seatIds = selectedSeats.map(s => s.seatId).join(",");

    fetch(`${APP_CONTEXT}/order?action=addToCart`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body:
            `showtimeId=${modalShowtimeId}`
            + `&seatIds=${seatIds}`
    })
	.then(() => {
	    alert("🛒 Ghế đã được giữ trong đơn hàng");

	    // reload ghế → ghế xám ngay
	    loadSeats();

	    // ẩn modal ghế
	    document.getElementById("seatModal").style.display = "none";
	});

}

function loadSeats() {
  fetch(`${APP_CONTEXT}/seat?action=byShowtime&showtimeId=${modalShowtimeId}`)
    .then(r => r.json())
    .then(data => {

      const map = document.getElementById("seatMap");
      map.innerHTML = "";

      let currentRow = "";
      let rowDiv = null;

      data.forEach(seat => {

        // tạo hàng mới
        if (seat.seatRow !== currentRow) {
          currentRow = seat.seatRow;

          rowDiv = document.createElement("div");
          rowDiv.className = "seat-row";

          rowDiv.innerHTML = `
            <div class="row-label">${currentRow}</div>
          `;

          map.appendChild(rowDiv);
        }

        /* ===== LỐI ĐI GIỮA (nếu rạp bạn có) =====
           ví dụ sau ghế số 5 */
        if (seat.seatCol === 6) {
          const walk = document.createElement("div");
          walk.className = "walkway";
          rowDiv.appendChild(walk);
        }

		let cls = "seat normal";

		if (seat.status === "Booked") cls = "seat booked";
		else if (seat.status === "HOLD") cls = "seat hold";
		else if (seat.seatType === "VIP") cls = "seat vip";
		else if (seat.seatType === "DOUBLE") cls = "seat couple";

        const btn = document.createElement("button");
        btn.className = cls;
        btn.innerText = seat.seatRow + seat.seatCol;
		btn.dataset.seatLabel = seat.seatRow + seat.seatCol;
        btn.dataset.seatId = seat.seatId;
        btn.dataset.seatType = seat.seatType;

		if (seat.status === "Booked" || seat.status === "HOLD") {
		    btn.disabled = true;
		}
        btn.onclick = () => toggleSeat(btn);

        rowDiv.appendChild(btn);
      });
    });
}



function toggleSeat(btn) {
    if (btn.classList.contains("booked")) return;

    const seatId = btn.dataset.seatId;
    const seatType = btn.dataset.seatType;
    const seatLabel = btn.innerText; // A1, B5...

    if (btn.classList.contains("selected")) {
        btn.classList.remove("selected");
        selectedSeats = selectedSeats.filter(s => s.seatId != seatId);
    } else {
        btn.classList.add("selected");
        selectedSeats.push({
            seatId,
            seatType,
            seatLabel
        });
    }

    updateTotal();
}

function updateTotal() {
    if (!showtimeBasePrice || selectedSeats.length === 0) {
        document.getElementById("totalPrice").innerText = "0 đ";
        return;
    }

    let total = 0;

    selectedSeats.forEach(s => {
        switch (s.seatType) {
            case "VIP":
                total += showtimeBasePrice + 30000;
                break;
            case "DOUBLE":
                total += showtimeBasePrice + 50000;
                break;
            default:
                total += showtimeBasePrice;
        }
    });

    document.getElementById("totalPrice").innerText =
        total.toLocaleString("vi-VN") + " đ";
}



function calculateTotal(basePrice) {
    let total = 0;

    selectedSeats.forEach(s => {
        if (s.seatType === "VIP") total += basePrice * 1.5;
        else total += basePrice;
    });

    return total;
}


function confirmSeat() {
    if (selectedSeats.length === 0) {
        alert("Vui lòng chọn ghế");
        return;
    }

    // CHỈ HIỂN THỊ THÔNG TIN – CHƯA ĐẶT GHẾ
    const seatText = selectedSeats.map(s => s.seatLabel).join(", ");
    const totalText = document.getElementById("totalPrice").innerText;

    document.getElementById("selectedSeatsText").innerText = seatText;
    document.getElementById("selectedTotalText").innerText = totalText;
    document.getElementById("selectedTicketInfo").style.display = "block";

    // Đóng modal ghế
    document.getElementById("seatModal").style.display = "none";
}


function closeSeatModal() {
    document.getElementById("seatModal").style.display = "none";
}

function resetSeatSelection() {
    selectedSeats = [];
    modalShowtimeId = null;

    document.getElementById("seatMap").innerHTML = "";
}

function resetBookingState() {
    modalShowtimeId = null;
    selectedSeats = [];

    document.getElementById("cinemaSelect").value = "";
    document.getElementById("showtimeList").innerHTML = "";
    document.getElementById("seatMap").innerHTML = "";
    document.getElementById("totalPrice").innerText = "0 đ";

    // ✅ Ẩn vé đã chọn
    document.getElementById("selectedTicketInfo").style.display = "none";
}

function showBookingSuccess() {

    const seatText = selectedSeats.map(s => s.seatLabel).join(", ");
    const totalText = document.getElementById("totalPrice").innerText;

    document.getElementById("selectedSeatsText").innerText = seatText;
    document.getElementById("selectedTotalText").innerText = totalText;
    document.getElementById("selectedTicketInfo").style.display = "block";

    document.getElementById("seatModal").style.display = "none";
}


function resetSeatForNewShowtime() {
    // reset ghế đã chọn
    selectedSeats = [];

    // reset UI
    document.getElementById("seatMap").innerHTML = "";
    document.getElementById("totalPrice").innerText = "0 đ";

    // Ẩn vé đã chọn
    const info = document.getElementById("selectedTicketInfo");
    if (info) info.style.display = "none";

    // HIỆN LẠI NÚT XÁC NHẬN
    const btn = document.querySelector(".confirm-btn");
    if (btn) btn.style.display = "inline-block";
}

function openAddSeat(showtimeId) {
    modalShowtimeId = showtimeId;
    document.getElementById("seatModal").style.display = "flex";
    loadSeats();
}

function initHoldCountdown() {

    const HOLD_TIME = 5 * 60 * 1000; // 5 phút

    document.querySelectorAll(".hold-timer").forEach(timer => {

        const bookingTimeMs = Number(timer.dataset.bookingTime);
        const countdownEl = timer.querySelector(".countdown");

        if (!bookingTimeMs || !countdownEl) return;

        function update() {
            const diff = HOLD_TIME - (Date.now() - bookingTimeMs);

            if (diff <= 0) {
                timer.innerHTML =
                    "<span style='color:red'>❌ Hết thời gian giữ ghế</span>";
                return;
            }

            const m = Math.floor(diff / 60000);
            const s = Math.floor((diff % 60000) / 1000);

            countdownEl.innerText =
                String(m).padStart(2, "0") + ":" +
                String(s).padStart(2, "0");
        }

        update();                 // chạy ngay
        setInterval(update, 1000);
    });
}

function cancelHold(btn, ticketId) {
    if (!confirm("Bạn có chắc muốn bỏ vé này?")) return;

    fetch(APP_CONTEXT + "/order?action=cancelHold", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "ticketId=" + ticketId
    })
    .then(r => r.json())
    .then(data => {
        if (data.success) {
            // ❌ XÓA NGAY KHỎI GIAO DIỆN
            btn.closest(".order-item").remove();
        } else {
            alert("Không thể xóa vé");
        }
    });
}


window.closeOrderModal = function () {
    const modal = document.getElementById("orderModal");
    if (modal) {
        modal.style.display = "none";
    }
};


let selectedMovieId = null;
let selectedShowtimeId = null;

let modalMovieId = null;
let modalShowtimeId = null;

let selectedSeats = [];

let showtimeBasePrice = 0;
