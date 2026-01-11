(function () {

    const base = window.cinemaContext || '';
    const modal = document.getElementById("deleteModal");
    const confirmBtn = document.getElementById("confirmDeleteBtn");

    document.body.addEventListener("click", function (e) {

        const deleteBtn = e.target.closest(".action.delete[data-type='cinema']");
        if (deleteBtn) {
            e.preventDefault();
            e.stopPropagation();

            modal.dataset.deleteId = deleteBtn.dataset.id;
            modal.style.display = "flex";
            return;
        }

        if (e.target === confirmBtn) {
            e.preventDefault();
            e.stopPropagation();

            const id = modal.dataset.deleteId;
            if (!id) return;

            fetch(`${base}/cinema`, {
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
                    alert("Không thể xóa rạp này vì đang còn phòng.");
                }

                modal.style.display = "none";
                delete modal.dataset.deleteId;
            })
            .catch(() => {
                alert("Delete failed (network)");
                modal.style.display = "none";
                delete modal.dataset.deleteId;
            });

            return;
        }

        if (e.target.classList.contains("cancel") || e.target === modal) {
            modal.style.display = "none";
            delete modal.dataset.deleteId;
        }
    });

})();