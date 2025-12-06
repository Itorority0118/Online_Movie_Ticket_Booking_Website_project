(function(){
    console.log("MOVIE LIST JS LOADED at", new Date().toISOString());

    // movieContext MUST be set in JSP: <script>window.movieContext='${pageContext.request.contextPath}';</script>
    const base = window.movieContext || '';

    // Use delegation on document body so it works regardless when this script is loaded
    document.body.addEventListener("click", function(e) {

        // Open delete modal
        if (e.target.matches(".action.delete, .action.delete *")) {
            e.preventDefault();
            const btn = e.target.closest(".action.delete");
            if (!btn) return;
            // store id on the modal element so both admin.js and this script can see it
            const id = btn.dataset.id;
            const modal = document.getElementById("deleteModal");
            modal.dataset.deleteId = id;
            modal.style.display = "flex";
            return;
        }

        // Confirm delete inside modal
        if (e.target.id === "confirmDeleteBtn") {
            e.preventDefault();
            const modal = document.getElementById("deleteModal");
            const id = modal ? modal.dataset.deleteId : null;
            if (!id) return;

            const movieWrapper = document.getElementById("movieCardsWrapper");
            fetch(`${base}/movie?action=delete&id=${id}`, {
                method: "POST",
                headers: { "X-Requested-With": "XMLHttpRequest" }
            })
            .then(r => r.json())
            .then(data => {
                if (data.success) {
                    const card = document.querySelector(`.movie-card[data-id="${id}"]`);
                    if (card) card.remove();
                } else {
                    alert(data.message || "Delete failed");
                }
                if (modal) {
                    modal.style.display = "none";
                    delete modal.dataset.deleteId;
                }
            })
            .catch(err => {
                console.error(err);
                alert("Delete failed (network).");
            });
            return;
        }

        // Cancel modal
        if (e.target.classList.contains("cancel")) {
            const modal = document.getElementById("deleteModal");
            if (modal) {
                modal.style.display = "none";
                delete modal.dataset.deleteId;
            }
            return;
        }

        // Click outside modal
        const modal = document.getElementById("deleteModal");
        if (modal && e.target === modal) {
            modal.style.display = "none";
            delete modal.dataset.deleteId;
            return;
        }
    });

})();
