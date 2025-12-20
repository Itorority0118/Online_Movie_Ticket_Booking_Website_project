console.log("MOVIE JS LOADED");

document.addEventListener("DOMContentLoaded", () => {

    const genreItems = document.querySelectorAll(".genre-item");
    const genresInput = document.getElementById("genresInput");
    let selectedGenres = [];

    genreItems.forEach(item => {
        item.addEventListener("click", () => {
            const value = item.dataset.value;

            if (selectedGenres.includes(value)) {
                selectedGenres = selectedGenres.filter(g => g !== value);
                item.classList.remove("active");
            } else {
                selectedGenres.push(value);
                item.classList.add("active");
            }

            genresInput.value = selectedGenres.join(",");
        });
    });

});
