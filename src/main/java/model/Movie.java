package model;

public class Movie {
    
    private int movieId;
    private String title;
    private String genre;
    private String director;
    private String cast;
    private String description;
    private int duration;          // in minutes
    private String language;
    private String releaseDate;
    private String posterUrl;      // image path
    private String trailerUrl;     // YouTube link or video
    private String status;         // "Now Showing" / "Coming Soon"
    
    // Empty constructor
    public Movie() {}

    // Constructor
    public Movie(int movieId, String title, String genre, String director, String cast, 
                 String description, int duration, String language, String releaseDate, 
                 String posterUrl, String trailerUrl, String status) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.cast = cast;
        this.description = description;
        this.duration = duration;
        this.language = language;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.trailerUrl = trailerUrl;
        this.status = status;
    }

    // Getters
    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDirector() { return director; }
    public String getCast() { return cast; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public String getLanguage() { return language; }
    public String getReleaseDate() { return releaseDate; }
    public String getPosterUrl() { return posterUrl; }
    public String getTrailerUrl() { return trailerUrl; }
    public String getStatus() { return status; }

    // Setters
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDirector(String director) { this.director = director; }
    public void setCast(String cast) { this.cast = cast; }
    public void setDescription(String description) { this.description = description; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setLanguage(String language) { this.language = language; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
    public void setStatus(String status) { this.status = status; }
}
