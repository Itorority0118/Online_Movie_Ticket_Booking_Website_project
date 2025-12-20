console.log("ADMIN JS LOADED at", new Date().toISOString());

const contentArea = document.getElementById("content-area");

let deleteId = null;
let deleteType = null;

document.addEventListener("click", e => {

    const link = e.target.closest("a[data-page]");
    if (link) {
        e.preventDefault();
        const url = link.getAttribute("data-page");
        highlightActive(url);
        loadPage(url);
        return;
    }

    const btn = e.target.closest("a.action.delete");
    if (btn) {
        e.preventDefault();
        deleteId = btn.getAttribute("data-id");
        deleteType = btn.getAttribute("data-type") || "user";
        showModal();
        return;
    }

    if (e.target && e.target.id === "confirmDeleteBtn") {
        if (deleteType === "movie") return;
        if (!deleteId || !deleteType) return;
        window.location.href = `${contextPath}/${deleteType}?action=delete&id=${deleteId}`;
    }

    if (e.target && e.target.classList.contains("cancel")) {
        closeModal();
    }
});

function highlightActive(url) {
    const urlObj = new URL(url, window.location.origin);
    const path = urlObj.pathname;
    const params = new URLSearchParams(urlObj.search);

    document.querySelectorAll(".sidebar ul li a[data-page]").forEach(link => {
        const linkUrl = new URL(link.getAttribute("data-page"), window.location.origin);
        const linkPath = linkUrl.pathname;

        if (path === linkPath && params.get("action") === new URLSearchParams(linkUrl.search).get("action")) {
            link.classList.add("active");
        } else {
            link.classList.remove("active");
        }
    });
}

function loadPage(url) {
    if (!contentArea) return;
	if (url.includes("cinema?action=list")) {
	    window.location.href = url;
	    return;
	}
    history.pushState(null, "", url);
    contentArea.innerHTML = `<div class="loading">Loading...</div>`;

    fetch(url, { headers: { "X-Requested-With": "XMLHttpRequest" } })
        .then(res => res.text())
        .then(html => {
            const doc = new DOMParser().parseFromString(html, "text/html");
            const newContent = doc.querySelector("#content-area");
            contentArea.innerHTML = newContent ? newContent.innerHTML : html;

            executeScriptsFromDocument(doc, url);
        })
        .catch(() => contentArea.innerHTML = `<p style="color:red;">Error loading page</p>`);
}

function executeScriptsFromDocument(doc, baseUrl) {
    const newContent = doc.querySelector("#content-area");
    if (newContent) {
        const inlineScripts = newContent.querySelectorAll("script:not([src])");
        inlineScripts.forEach(s => {
            try {
                const code = s.textContent;
                (new Function(code))();
            } catch (err) {
                console.error("Error executing inline script:", err);
            }
        });
    }

    const externalScripts = Array.from(doc.querySelectorAll("script[src]"))
        .map(s => s.src)
        .map(src => {
            try {
                return new URL(src, window.location.origin + baseUrl).href;
            } catch (e) {
                return src;
            }
        });

    loadExternalScriptsSequentially(externalScripts);
}

function loadExternalScriptsSequentially(sources) {
    if (!sources || !sources.length) return;
    const loadNext = (i) => {
        if (i >= sources.length) return;
        const existing = document.querySelector(`script[src="${sources[i]}"]`);
        if (existing) {
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

document.addEventListener("DOMContentLoaded", () => {
    const url = window.location.pathname + window.location.search;
    highlightActive(url);

    window.addEventListener("popstate", () => {
        loadPage(location.pathname + location.search);
    });
});
