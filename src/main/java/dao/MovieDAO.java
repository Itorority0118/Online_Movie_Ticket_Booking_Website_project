package dao;

import java.sql.*;
import java.util.*;
import model.Movie;
import utils.DBConnection;
import utils.MovieMapper;

public class MovieDAO {

	// Create new movie
    public boolean addMovie(Movie movie) {
        String sql = "INSERT INTO Movie (Title, Genre, Director, Cast, Description, Duration, Language, ReleaseDate, PosterUrl, TrailerUrl, Status, CreatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setString(3, movie.getDirector());
            stmt.setString(4, movie.getCast());
            stmt.setString(5, movie.getDescription());
            stmt.setInt(6, movie.getDuration());
            stmt.setString(7, movie.getLanguage());
            stmt.setString(8, movie.getReleaseDate());
            stmt.setString(9, movie.getPosterUrl());
            stmt.setString(10, movie.getTrailerUrl());
            stmt.setString(11, movie.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding movie: " + e.getMessage());
            return false;
        }
    }

    // Get movie by ID
    public Movie getMovieById(int id) {
        String sql = "SELECT * FROM Movie WHERE MovieId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return MovieMapper.mapMovie(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching movie by ID: " + e.getMessage());
        }
        return null;
    }

    // Get all movies
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM Movie ORDER BY CreatedAt DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movies.add(MovieMapper.mapMovie(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching all movies: " + e.getMessage());
        }

        return movies;
    }

    // Get movies by status ("Now Showing" / "Coming Soon")
    public List<Movie> getMoviesByStatus(String status) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM Movie WHERE Status = ? ORDER BY ReleaseDate DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(MovieMapper.mapMovie(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching movies by status: " + e.getMessage());
        }

        return movies;
    }

    // Update movie
    public boolean updateMovie(Movie movie) {
        String sql = "UPDATE Movie SET Title=?, Genre=?, Director=?, Cast=?, Description=?, Duration=?, Language=?, " +
                     "ReleaseDate=?, PosterUrl=?, TrailerUrl=?, Status=? WHERE MovieId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setString(3, movie.getDirector());
            stmt.setString(4, movie.getCast());
            stmt.setString(5, movie.getDescription());
            stmt.setInt(6, movie.getDuration());
            stmt.setString(7, movie.getLanguage());
            stmt.setString(8, movie.getReleaseDate());
            stmt.setString(9, movie.getPosterUrl());
            stmt.setString(10, movie.getTrailerUrl());
            stmt.setString(11, movie.getStatus());
            stmt.setInt(12, movie.getMovieId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error updating movie: " + e.getMessage());
            return false;
        }
    }

    // Delete movie
    public boolean deleteMovie(int id) {
        String sql = "DELETE FROM Movie WHERE MovieId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error deleting movie: " + e.getMessage());
            return false;
        }
    }
    
    public List<Movie> findByTitle(String keyword) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM Movie WHERE Title LIKE ? ORDER BY CreatedAt DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movies.add(MovieMapper.mapMovie(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searching movies: " + e.getMessage());
        }
        return movies;
    }

    public int countMovies() {
        String sql = "SELECT COUNT(*) FROM Movie"; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error counting movies: " + e.getMessage());
        }
        return 0;
    }
    
    public List<String> getDistinctGenres() {
        List<String> genres = new ArrayList<>();
        
        String sql = "SELECT DISTINCT TRIM(Genre) as Genre FROM Movie WHERE Genre IS NOT NULL AND TRIM(Genre) <> '' ORDER BY Genre ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String genre = rs.getString("Genre");
                if (genre != null && !genre.trim().isEmpty()) {
                    genres.add(genre.trim());
                }
            }
            
            System.out.println("DEBUG (MovieDAO): Found " + genres.size() + " distinct genres."); 

        } catch (SQLException e) {
            System.out.println("❌ ERROR fetching distinct genres: " + e.getMessage());
            e.printStackTrace();
        }
        return genres;
    }

    public List<String> getAgeRatingList() {

        List<String> ratings = new ArrayList<>();
        String sql = "SELECT DISTINCT AgeRating FROM Movie WHERE AgeRating IS NOT NULL AND AgeRating != '' ORDER BY AgeRating ASC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            while (rs.next()) {
                ratings.add(rs.getString("AgeRating"));
            }
        } catch (SQLException e) {
             System.out.println("Error fetching distinct age ratings: " + e.getMessage());
        }
        return ratings;

    }
}