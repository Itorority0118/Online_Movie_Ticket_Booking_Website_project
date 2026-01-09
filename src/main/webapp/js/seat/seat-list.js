(function(){
    console.log("SEAT LIST JS LOADED at", new Date().toISOString());

    const base = window.seatContext || '';
    document.body.addEventListener("click", function(e) {

        if (e.target.matches(".delete-seat, .delete-seat *")) {
            e.preventDefault();
            const btn = e.target.closest(".delete-seat");
            if (!btn) return;
            const id = btn.dataset.id;
            const modal = document.getElementById("deleteModal");
            modal.dataset.deleteId = id; 
            modal.style.display = "flex"; 
            return;
        }

        if (e.target.id === "confirmDeleteBtn") {
            e.preventDefault();
            const modal = document.getElementById("deleteModal");
            const id = modal ? modal.dataset.deleteId : null;
            if (!id) return;

            fetch(`${base}/seat`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: "action=delete&id=" + encodeURIComponent(id)
            })
            .then(r => r.json())
            .then(data => {
                if (data.success) {
                    const seatEl = document.querySelector(`.delete-seat[data-id="${id}"]`);
                    seatEl?.closest(".seat")?.remove();
                } else {
                    alert("Không thể xóa vì có vé đang được thao tác trên ghế này");
                }
                if (modal) {
                    modal.style.display = "none";
                    delete modal.dataset.deleteId;
                }
            })
            .catch(err => {
                console.error(err);
                alert("Delete failed (network).");
                const modal = document.getElementById("deleteModal");
                if (modal) {
                    modal.style.display = "none";
                    delete modal.dataset.deleteId;
                }
            });
            return;
        }

        // Click nút cancel
        if (e.target.classList.contains("cancel")) {
            const modal = document.getElementById("deleteModal");
            if (modal) {
                modal.style.display = "none";
                delete modal.dataset.deleteId;
            }
            return;
        }

        const modal = document.getElementById("deleteModal");
        if (modal && e.target === modal) {
            modal.style.display = "none";
            delete modal.dataset.deleteId;
            return;
        }
    });
})();