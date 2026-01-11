console.log("✅ member.js loaded");

function openMemberModal(type, price) {
    const modal = document.getElementById("memberModal");
    const typeEl = document.getElementById("memberType");
    const priceEl = document.getElementById("memberPrice");

    if (!modal || !typeEl || !priceEl) {
        console.error("❌ Member modal elements not found");
        return;
    }

    typeEl.innerText = type;
    priceEl.innerText = price;

    modal.style.display = "flex";
}

function closeMemberModal() {
    const modal = document.getElementById("memberModal");
    if (modal) modal.style.display = "none";
}

function confirmMember() {
    alert("🎉 Chức năng chưa khả dụng.");
    closeMemberModal();
}
