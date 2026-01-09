console.log("COMMON JS LOADED");

document.addEventListener("click", e => {

    const logoutLink = e.target.closest("a[href*='action=logout']");
    if (logoutLink) {
        return;
    }

});

function toggleUserDropdown() {
    const menu = document.querySelector(".user-menu");
    menu.classList.toggle("active");

    document.addEventListener("click", function handler(e) {
        if (!menu.contains(e.target)) {
            menu.classList.remove("active");
            document.removeEventListener("click", handler);
        }
    });
}

function openProfileModal() {
    const modal = document.getElementById("profileModal");
    const dropdown = document.getElementById("userDropdown");
    if (!modal) return;

    if (dropdown) dropdown.style.display = "none";

    modal.style.display = "flex"; 
    setTimeout(() => modal.classList.add("show"), 10);

    modal.onclick = (e) => {
        if (e.target === modal) closeProfileModal();
    };
}

function closeProfileModal() {
    const modal = document.getElementById("profileModal");
    if (!modal) return;

    modal.classList.remove("show");
    setTimeout(() => modal.style.display = "none", 300);
}

function openMovieModal(
    title, genre, duration, description,
    poster, trailer, movieId, autoOpenBooking = false
) {
    resetBookingState();
    modalMovieId = movieId;

    document.getElementById("modalTitle").innerText = title;
    document.getElementById("modalGenre").innerText = genre;
    document.getElementById("modalDuration").innerText = duration;
    document.getElementById("modalDescription").innerText = description;
    document.getElementById("modalPoster").src = poster;
    document.getElementById("modalTrailer").href = trailer;

    const modal = document.getElementById("movieModal");
    modal.style.display = "flex";
    setTimeout(() => modal.classList.add("show"), 10);

    modal.onclick = (e) => {
        if (e.target === modal) closeMovieModal();
    };

    loadCitiesInModal();

    if (autoOpenBooking) {
        setTimeout(() => {
            document.querySelector(".booking-section").scrollIntoView({ behavior: "smooth" });
        }, 200);
    }
}

function closeMovieModal() {
    resetBookingState();
    const modal = document.getElementById("movieModal");
    modal.classList.remove("show");
    setTimeout(() => modal.style.display = "none", 300);
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

            modalShowtimeId = null;

            data.forEach(s => {
                const btn = document.createElement("button");
                btn.dataset.showtimeId = s.showtimeId;
                btn.dataset.ticketPrice = s.ticketPrice;
                btn.innerText = formatTimeOnly(s.startTime);
                btn.onclick = () => selectShowtimeInModal(s.showtimeId, btn);
                box.appendChild(btn);
            });
        });
}

function selectShowtimeInModal(id, btn) {
    if (!window.IS_LOGGED_IN) {
        window.location.href =
          `${APP_CONTEXT}/login.jsp?redirect=${encodeURIComponent(location.href)}`;
        return;
    }

    if (modalShowtimeId && modalShowtimeId !== id) {
        resetSeatForNewShowtime();
    }

    modalShowtimeId = id;
    showtimeBasePrice = Number(btn.dataset.ticketPrice);
    updateTotal();

    document.querySelectorAll("#showtimeList button")
        .forEach(b => b.classList.remove("active"));
    btn.classList.add("active");

    const seatModal = document.getElementById("seatModal");
    seatModal.style.display = "flex";
    setTimeout(() => seatModal.classList.add("show"), 10);
	seatModal.querySelector(".seat-modal").onclick = (e) => {
	    e.stopPropagation();
	};
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
		resetBookingState();
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
	    document.getElementById("seatModal").style.display = "none";

	    const tickets = selectedSeats.map(s => ({
	        id: s.seatId,
	        movie: document.getElementById("modalTitle").innerText,
	        seat: s.seatLabel
	    }));
	    openOrderModal(tickets);
		resetBookingState();
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
            if (seat.seatRow !== currentRow) {
                currentRow = seat.seatRow;
                rowDiv = document.createElement("div");
                rowDiv.className = "seat-row";
                rowDiv.innerHTML = `<div class="row-label">${currentRow}</div>`;
                map.appendChild(rowDiv);
            }

            if (seat.seatCol === 6) {
                const walk = document.createElement("div");
                walk.className = "walkway";
                rowDiv.appendChild(walk);
            }

            let cls = "seat normal";
            if (seat.status === "Booked" || seat.status === "HOLD") cls = "seat booked";
            else if (seat.seatType === "VIP") cls = "seat vip";
            else if (seat.seatType === "Double" || seat.seatType === "COUPLE") cls = "seat couple";

            const btn = document.createElement("button");
            btn.className = cls;
            btn.innerText = seat.seatRow + seat.seatCol;
            btn.dataset.seatLabel = seat.seatRow + seat.seatCol;
            btn.dataset.seatId = seat.seatId;
            btn.dataset.seatType = seat.seatType;

            if (seat.status === "Booked" || seat.status === "HOLD") btn.disabled = true;

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

    const seatText = selectedSeats.map(s => s.seatLabel).join(", ");
    const totalText = document.getElementById("totalPrice").innerText;

    document.getElementById("selectedSeatsText").innerText = seatText;
    document.getElementById("selectedTotalText").innerText = totalText;
    document.getElementById("selectedTicketInfo").style.display = "block";

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
    selectedSeats = [];

    // reset UI
    document.getElementById("seatMap").innerHTML = "";
    document.getElementById("totalPrice").innerText = "0 đ";

    const info = document.getElementById("selectedTicketInfo");
    if (info) info.style.display = "none";

    const btn = document.querySelector(".confirm-btn");
    if (btn) btn.style.display = "inline-block";
}

function openAddSeat(showtimeId) {
    modalShowtimeId = showtimeId;
    document.getElementById("seatModal").style.display = "flex";
    loadSeats();
}

function initHoldCountdown() {

    const HOLD_TIME = 5 * 60 * 1000;
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

        update();                
        setInterval(update, 1000);
    });
}

let selectedMovieId = null;
let selectedShowtimeId = null;

let modalMovieId = null;
let modalShowtimeId = null;

let selectedSeats = [];

let showtimeBasePrice = 0;

function saveProfile() {
    const form = document.getElementById('profileForm');
    const fullName = form.fullName.value.trim();
    const phone = form.phone.value.trim();
    const password = form.password.value.trim();

    const body = `action=updateProfile&fullName=${encodeURIComponent(fullName)}&phone=${encodeURIComponent(phone)}&password=${encodeURIComponent(password)}`;

    fetch(`${window.APP_CONTEXT}/user`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: body
    })
    .then(res => res.json())
    .then(data => {
        const errorsDiv = document.getElementById('profileErrors');
        errorsDiv.innerHTML = '';

        if (data.success) {
            errorsDiv.style.color = 'green';
            errorsDiv.innerText = 'Cập nhật thành công!';
            document.querySelector('.user-name').innerText = data.user.fullName;
        } else {
            errorsDiv.style.color = 'red';
            if (data.errors) {
                for (const key in data.errors) {
                    errorsDiv.innerHTML += data.errors[key] + '<br>';
                }
            } else {
                errorsDiv.innerText = 'Có lỗi xảy ra. Vui lòng thử lại.';
            }
        }
    })
    .catch(err => {
        console.error(err);
        alert('Có lỗi xảy ra. Vui lòng thử lại.');
    });
}