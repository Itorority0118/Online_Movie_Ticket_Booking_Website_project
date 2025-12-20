console.log("COMMON JS LOADED");

document.addEventListener("click", e => {

    const logoutLink = e.target.closest("a[href*='action=logout']");
    if (logoutLink) {
        return;
    }

});
