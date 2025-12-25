(function () {

    const base = window.ticketContext || '';

    /* ===== DELETE ===== */
    const deleteModal = document.getElementById("deleteModal");
    const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");

    /* ===== CANCEL ===== */
    const cancelModal = document.getElementById("cancelModal");
    const confirmCancelBtn = document.getElementById("confirmCancelBtn");

    document.body.addEventListener("click", function (e) {

        /* ================= CANCEL ================= */

        const cancelBtn = e.target.closest(".action.cancel");
        if (cancelBtn) {
            e.preventDefault();
            e.stopPropagation();

            cancelModal.dataset.id = cancelBtn.dataset.id;
            cancelModal.style.display = "flex";
            return;
        }

        if (e.target === confirmCancelBtn) {
            e.preventDefault();
            e.stopPropagation();

            const id = cancelModal.dataset.id;
            if (!id) return;

            fetch(`${base}/ticket?action=cancel&id=${id}`, {
                headers: {
                    "X-Requested-With": "XMLHttpRequest"
                }
            })
            .then(() => {
                const row = document.querySelector(`tr[data-id='${id}']`);
                if (!row) return;

                // update status
                const statusSpan = row.querySelector(".status");
                statusSpan.textContent = "Cancelled";
                statusSpan.className = "status Cancelled";

                // change action -> Delete
                const actionCell = row.querySelector(".col-action");
                actionCell.innerHTML = `
                    <a class="action delete"
                       href="#"
                       data-id="${id}"
                       data-type="ticket">
                        Delete
                    </a>
                `;

                cancelModal.style.display = "none";
                delete cancelModal.dataset.id;
            });

            return;
        }

        /* ================= DELETE (GIỮ NGUYÊN) ================= */

        const deleteBtn = e.target.closest(".action.delete[data-type='ticket']");
        if (deleteBtn) {
            e.preventDefault();
            e.stopPropagation();

            deleteModal.dataset.deleteId = deleteBtn.dataset.id;
            deleteModal.style.display = "flex";
            return;
        }

        if (e.target === confirmDeleteBtn) {
            e.preventDefault();
            e.stopPropagation();

            const id = deleteModal.dataset.deleteId;
            if (!id) return;

            fetch(`${base}/ticket`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: `action=delete&id=${id}`
            })
            .then(r => r.json())
            .then(data => {
                if (data.success) {
                    const row = document.querySelector(`tr[data-id='${id}']`);
                    if (row) row.remove();
                } else {
                    alert("Delete failed");
                }

                deleteModal.style.display = "none";
                delete deleteModal.dataset.deleteId;
            })
            .catch(() => {
                alert("Delete failed (network)");
                deleteModal.style.display = "none";
                delete deleteModal.dataset.deleteId;
            });

            return;
        }

        /* ================= CLOSE MODAL ================= */

        if (
            e.target.classList.contains("cancel") ||
            e.target === deleteModal ||
            e.target === cancelModal
        ) {
            deleteModal.style.display = "none";
            cancelModal.style.display = "none";
            delete deleteModal.dataset.deleteId;
            delete cancelModal.dataset.id;
        }
    });

})();
