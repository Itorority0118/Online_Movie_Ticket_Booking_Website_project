console.log("ADMIN JS LOADED at", new Date().toISOString());

const contentArea = document.getElementById("content-area");

let deleteId = null;
let deleteType = null;

// -------------------- EVENT DELEGATION --------------------
document.addEventListener("click", e => {

    // Sidebar link click
    const link = e.target.closest("a[data-page]");
    if (link) {
        e.preventDefault();
        const url = link.getAttribute("data-page");
        highlightActive(url);
        loadPage(url);
        return;
    }

    // Delete button click (generic - delegation)
    const btn = e.target.closest("a.action.delete");
    if (btn) {
        e.preventDefault();
        deleteId = btn.getAttribute("data-id");
        deleteType = btn.getAttribute("data-type") || "user";
        showModal();
        return;
    }

    // Confirm Delete click
    if (e.target && e.target.id === "confirmDeleteBtn") {
        // allow movie to be handled by movie-list.js if it sets type "movie"
        if (deleteType === "movie") return;
        if (!deleteId || !deleteType) return;
        window.location.href = `${contextPath}/${deleteType}?action=delete&id=${deleteId}`;
    }

    // Cancel
    if (e.target && e.target.classList.contains("cancel")) {
        closeModal();
    }
});

// -------------------- HIGHLIGHT SIDEBAR --------------------
function highlightActive(url) {
    document.querySelectorAll(".sidebar ul li a[data-page]").forEach(link => {
        if (url === link.getAttribute("data-page")) {
            link.classList.add("active");
        } else {
            link.classList.remove("active");
        }
    });
}

// -------------------- AJAX LOAD CONTENT --------------------
function loadPage(url) {
    if (!contentArea) return;

    history.pushState(null, "", url);
    contentArea.innerHTML = `<div class="loading">Loading...</div>`;

    fetch(url, { headers: { "X-Requested-With": "XMLHttpRequest" } })
        .then(res => res.text())
        .then(html => {
            // parse returned HTML and extract content-area + scripts
            const doc = new DOMParser().parseFromString(html, "text/html");
            const newContent = doc.querySelector("#content-area");
            contentArea.innerHTML = newContent ? newContent.innerHTML : html;

            // execute scripts found in the returned fragment (both external and inline)
            executeScriptsFromDocument(doc, url);
        })
        .catch(() => contentArea.innerHTML = `<p style="color:red;">Error loading page</p>`);
}

// Execute scripts contained in parsed document
function executeScriptsFromDocument(doc, baseUrl) {
    // First: run inline scripts that are inside the #content-area (if any)
    const newContent = doc.querySelector("#content-area");
    if (newContent) {
        const inlineScripts = newContent.querySelectorAll("script:not([src])");
        inlineScripts.forEach(s => {
            try {
                const code = s.textContent;
                // use Function to run in global scope
                (new Function(code))();
            } catch (err) {
                console.error("Error executing inline script:", err);
            }
        });
    }

    // Then: load external scripts found in the document (including ones referenced in fragment)
    const externalScripts = Array.from(doc.querySelectorAll("script[src]"))
        .map(s => s.src)
        // normalize relative src if necessary (some servers return relative paths)
        .map(src => {
            try {
                return new URL(src, window.location.origin + baseUrl).href;
            } catch (e) {
                return src;
            }
        });

    // Append them sequentially to ensure order (optional)
    loadExternalScriptsSequentially(externalScripts);
}

function loadExternalScriptsSequentially(sources) {
    if (!sources || !sources.length) return;
    const loadNext = (i) => {
        if (i >= sources.length) return;
        // avoid re-adding same script (by src) if already present with same src
        const existing = document.querySelector(`script[src="${sources[i]}"]`);
        if (existing) {
            // already present — skip to next (but might be cached)
            loadNext(i + 1);
            return;
        }
        const s = document.createElement("script");
        s.src = sources[i] + (sources[i].includes("?") ? "&" : "?") + "v=" + Date.now();
        s.onload = () => loadNext(i + 1);
        s.onerror = () => {
            console.error("Failed to load script:", sources[i]);
            loadNext(i + 1);
        };
        document.body.appendChild(s);
    };
    loadNext(0);
}

// -------------------- DELETE MODAL --------------------
function showModal() {
    const modal = document.getElementById("deleteModal");
    if (modal) modal.style.display = "flex";
}

function closeModal() {
    const modal = document.getElementById("deleteModal");
    if (modal) modal.style.display = "none";
    deleteId = null;
    deleteType = null;
}

// -------------------- DOM READY --------------------
document.addEventListener("DOMContentLoaded", () => {
    const url = window.location.pathname + window.location.search;
    highlightActive(url);

    // handle browser back/forward to re-load content via AJAX
    window.addEventListener("popstate", () => {
        loadPage(location.pathname + location.search);
    });
});
