package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Movie;

public class MovieMapper {
    public static Movie mapMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();

        movie.setMovieId(rs.getInt("MovieId"));
        movie.setTitle(rs.getString("Title"));
        movie.setGenre(rs.getString("Genre"));
        movie.setDirector(rs.getString("Director"));
        movie.setCast(rs.getString("Cast"));
        movie.setDescription(rs.getString("Description"));
        movie.setDuration(rs.getInt("Duration"));
        movie.setLanguage(rs.getString("Language"));
        movie.setReleaseDate(rs.getString("ReleaseDate"));
        movie.setPosterUrl(rs.getString("PosterUrl"));
        movie.setTrailerUrl(rs.getString("TrailerUrl"));
        movie.setStatus(rs.getString("Status"));

        return movie;
    }
}
