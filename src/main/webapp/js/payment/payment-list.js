(function () {

    const base = window.paymentContext || '';

    const confirmModal = document.getElementById("confirmModal");
    const confirmActionBtn = document.getElementById("confirmPaymentBtn");
    const confirmTitle = confirmModal.querySelector("h3");
    const confirmText = confirmModal.querySelector("p");

    const deleteModal = document.getElementById("deleteModal");
    const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");

    document.body.addEventListener("click", function (e) {

        const statusBtn = e.target.closest(".action.confirm, .action.cancel");
        if (statusBtn) {
            e.preventDefault();
            e.stopPropagation();

            confirmModal.dataset.id = statusBtn.dataset.id;
            confirmModal.dataset.status = statusBtn.dataset.status;

            if (statusBtn.classList.contains("confirm")) {
                confirmTitle.textContent = "Confirm Payment?";
                confirmText.innerHTML = "Payment will be marked as <b>Success</b>.";
            } else {
                confirmTitle.textContent = "Cancel Payment?";
                confirmText.innerHTML = "Payment will be marked as <b>Failed</b>.";
            }

            confirmModal.style.display = "flex";
            return;
        }

        const confirmBtn = e.target.closest("#confirmPaymentBtn");
        if (confirmBtn) {
            e.preventDefault();
            e.stopPropagation();

            const id = confirmModal.dataset.id;
            const status = confirmModal.dataset.status;
            if (!id || !status) return;

            fetch(`${base}/payment`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: `action=updateStatus&id=${id}&status=${status}`
            })
            .then(res => res.json())
            .then(data => {
                if (!data.success) {
                    alert("Update failed");
                    return;
                }

                const row = document.querySelector(`tr[data-id='${id}']`);
                if (!row) return;

                const statusSpan = row.querySelector(".status");
                statusSpan.textContent = status;
                statusSpan.className = `status ${status}`;

                const actionCell = row.querySelector(".col-action");
                if (status === "Failed") {
                    actionCell.innerHTML = `
                        <a class="action delete"
                           href="#"
                           data-id="${id}"
                           data-type="payment">
                            Delete
                        </a>
                    `;
                } else {
                    actionCell.innerHTML = `<span style="color:#999;">—</span>`;
                }

                confirmModal.style.display = "none";
                delete confirmModal.dataset.id;
                delete confirmModal.dataset.status;
            });

            return;
        }

        const deleteBtn = e.target.closest(".action.delete[data-type='payment']");
        if (deleteBtn) {
            e.preventDefault();
            e.stopPropagation();

            deleteModal.dataset.id = deleteBtn.dataset.id;
            deleteModal.style.display = "flex";
            return;
        }

        const deleteConfirmBtn = e.target.closest("#confirmDeleteBtn");
        if (deleteConfirmBtn) {
            e.preventDefault();
            e.stopPropagation();

            const id = deleteModal.dataset.id;
            if (!id) return;

            fetch(`${base}/payment`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: `action=delete&id=${id}`
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    const row = document.querySelector(`tr[data-id='${id}']`);
                    if (row) row.remove();
                } else {
                    alert("Delete failed");
                }

                deleteModal.style.display = "none";
                delete deleteModal.dataset.id;
            })
            .catch(() => {
                alert("Delete failed (network)");
                deleteModal.style.display = "none";
                delete deleteModal.dataset.id;
            });

            return;
        }

        if (
            e.target.classList.contains("cancel") ||
            e.target === confirmModal ||
            e.target === deleteModal
        ) {
            confirmModal.style.display = "none";
            deleteModal.style.display = "none";
            delete confirmModal.dataset.id;
            delete confirmModal.dataset.status;
            delete deleteModal.dataset.id;
        }
    });

})();