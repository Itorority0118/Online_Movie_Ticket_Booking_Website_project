(function(){
    console.log("MOVIE LIST JS LOADED at", new Date().toISOString());

    const base = window.movieContext || '';
    document.body.addEventListener("click", function(e) {

        if (e.target.matches(".action.delete, .action.delete *")) {
            e.preventDefault();
            const btn = e.target.closest(".action.delete");
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