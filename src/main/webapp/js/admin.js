console.log("ADMIN JS LOADED at", new Date().toISOString());

let deleteUserId = null;
const contentArea = document.getElementById("content-area");
const contextPath = "/" + window.location.pathname.split("/")[1];

// -------------------- CLICK EVENTS --------------------
document.addEventListener("click", e => {
    // Sidebar link
    const link = e.target.closest("a[data-page]");
    if (link) {
        e.preventDefault();
        const url = link.getAttribute("data-page");
        highlightActive(url);
        loadPage(url);
        return;
    }

    // Delete button
    const btn = e.target.closest("a.action.delete");
    if (btn) {
        e.preventDefault();
        deleteUserId = btn.getAttribute("data-id");
        showModal();
    }
});

// -------------------- HIGHLIGHT SIDEBAR --------------------
function highlightActive(url) {
    document.querySelectorAll(".sidebar ul li a[data-page]").forEach(link => {
        if (url.endsWith(link.getAttribute("data-page").replace(contextPath, ""))) {
            link.classList.add("active");
        } else {
            link.classList.remove("active");
        }
    });
}

// -------------------- AJAX LOAD --------------------
function loadPage(url) {
    if (!contentArea) return;
    contentArea.innerHTML = `<div class="loading">Loading...</div>`;

    fetch(url, { headers: { "X-Requested-With": "XMLHttpRequest" } })
        .then(res => res.text())
        .then(html => {
            const doc = new DOMParser().parseFromString(html, "text/html");
            const newContent = doc.querySelector("#content-area");
            contentArea.innerHTML = newContent ? newContent.innerHTML : html;
            loadDeleteConfirm();
        })
        .catch(() => contentArea.innerHTML = `<p style="color:red;">Error loading page</p>`);
}

// -------------------- DELETE MODAL --------------------
function loadDeleteConfirm() {
    const btn = document.getElementById("confirmDeleteBtn");
    if (btn) btn.onclick = () => {
        window.location = contextPath + "/user?action=delete&id=" + deleteUserId;
    };
}

function showModal() { document.getElementById("deleteModal").style.display = "flex"; }
function closeModal() { document.getElementById("deleteModal").style.display = "none"; }

// -------------------- DOM CONTENT LOADED --------------------
document.addEventListener("DOMContentLoaded", () => {
    const url = window.location.pathname + window.location.search;
    highlightActive(url);

    const searchInput = document.getElementById('searchInput');
    const extraFilters = document.getElementById('extraFilters');
    const applyFilters = document.getElementById('applyFilters');

    // ---------------- APPLY FILTER ----------------
    if (applyFilters && searchInput && extraFilters) {
        applyFilters.addEventListener('click', () => {
            const roles = Array.from(extraFilters.querySelectorAll('input[name="role"]:checked')).map(el => el.value);
            const statuses = Array.from(extraFilters.querySelectorAll('input[name="status"]:checked')).map(el => el.value);
            const search = searchInput.value.trim();

            let params = new URLSearchParams();
            if (roles.length) params.append('role', roles.join(','));
            if (statuses.length) params.append('status', statuses.join(','));
            if (search) params.append('search', search);

            window.location.href = window.location.pathname + '?' + params.toString();
        });
    }
});
