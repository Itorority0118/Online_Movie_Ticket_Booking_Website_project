(function(){
    console.log("CINEMA LIST JS LOADED at", new Date().toISOString());

    const base = window.cinemaContext || '';

    const modal = document.getElementById("deleteModal");
    const confirmBtn = document.getElementById("confirmDeleteBtn");

    document.body.addEventListener("click", function(e) {

        if (e.target.matches(".action.delete, .action.delete *")) {
            e.preventDefault();
            const btn = e.target.closest(".action.delete");
            if (!btn) return;

            const id = btn.dataset.id;
            if (!modal) return;

            modal.dataset.deleteId = id;
            modal.style.display = "flex";
            return;
        }

        if (e.target === confirmBtn) {
            e.preventDefault();
            if (!modal) return;

            const id = modal.dataset.deleteId;
            if (!id) return;

            fetch(`${base}/cinema?action=delete&id=${id}`, {
                method: "POST",
                headers: { "X-Requested-With": "XMLHttpRequest" }
            })
            .then(r => r.json())
            .then(data => {
                if (data.success) {
                    const row = document.querySelector(`tr[data-id='${id}']`);
                    if (row) row.remove();

                    highlightSidebar();
                } else {
                    alert(data.message || "Delete failed");
                }
                modal.style.display = "none";
                delete modal.dataset.deleteId;
            })
            .catch(err => {
                console.error(err);
                alert("Delete failed (network).");
                modal.style.display = "none";
                delete modal.dataset.deleteId;
            });
            return;
        }

        if (e.target.classList.contains("cancel") && modal) {
            modal.style.display = "none";
            delete modal.dataset.deleteId;
            return;
        }

        if (modal && e.target === modal) {
            modal.style.display = "none";
            delete modal.dataset.deleteId;
            return;
        }
    });

    function highlightSidebar() {
        const currentPath = window.location.pathname;
        const currentAction = new URLSearchParams(window.location.search).get("action");

        document.querySelectorAll(".sidebar ul li a[data-page]").forEach(link => {
            const linkUrl = new URL(link.getAttribute("data-page"), window.location.origin);
            const linkPath = linkUrl.pathname;
            const linkAction = new URLSearchParams(linkUrl.search).get("action");

            if (linkPath === currentPath && linkAction === currentAction) {
                link.classList.add("active");
            } else {
                link.classList.remove("active");
            }
        });
    }

    highlightSidebar();

})();
