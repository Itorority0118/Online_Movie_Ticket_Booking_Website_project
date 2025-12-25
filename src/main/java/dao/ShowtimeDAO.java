package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Showtime;
import model.Movie; // Phải import Movie
import utils.DBConnection;
import utils.MovieMapper; // Phải import MovieMapper

public class ShowtimeDAO {

    // Thêm suất chiếu mới
    public boolean addShowtime(Showtime showtime) {
        String query = "INSERT INTO Showtime (MovieId, RoomId, StartTime, EndTime, TicketPrice) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, showtime.getMovieId());
            ps.setInt(2, showtime.getRoomId());
            ps.setString(3, showtime.getStartTime());
            ps.setString(4, showtime.getEndTime());
            ps.setDouble(5, showtime.getTicketPrice());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateShowtime(Showtime showtime) {
        String query = "UPDATE Showtime SET MovieId = ?, RoomId = ?, StartTime = ?, EndTime = ?, TicketPrice = ? WHERE ShowtimeId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, showtime.getMovieId());
            ps.setInt(2, showtime.getRoomId());
            ps.setString(3, showtime.getStartTime());
            ps.setString(4, showtime.getEndTime());
            ps.setDouble(5, showtime.getTicketPrice());
            ps.setInt(6, showtime.getShowtimeId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteShowtime(int showtimeId) {
        String query = "DELETE FROM Showtime WHERE ShowtimeId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, showtimeId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String query = "SELECT * FROM Showtime";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Showtime showtime = new Showtime(
                    rs.getInt("ShowtimeId"),
                    rs.getInt("MovieId"),
                    rs.getInt("RoomId"),
                    rs.getString("StartTime"),
                    rs.getString("EndTime"),
                    rs.getDouble("TicketPrice")
                );
                showtimes.add(showtime);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return showtimes;
    }

    public boolean existsByMovieId(int movieId) {
        String query = "SELECT 1 FROM Showtime WHERE MovieId = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    

    public List<Movie> getShowtimesByFilter(
            int cinemaId, String date, String genre, String ageRating) {
            
        List<Movie> results = new ArrayList<>();
        
        StringBuilder query = new StringBuilder("SELECT s.*, m.* FROM Showtime s ");
        query.append("JOIN Movie m ON s.movie_id = m.MovieId ");
        query.append("JOIN Room r ON s.room_id = r.RoomId "); 
        
        query.append("WHERE r.CinemaId = ? AND CAST(s.StartTim AS DATE) = ?");

        if (genre != null && !genre.isEmpty()) {
            query.append(" AND m.Genre = ?");
        }
        if (ageRating != null && !ageRating.isEmpty()) {
            query.append(" AND m.AgeRating = ?");
        }

        query.append(" ORDER BY m.Title, s.StartTime");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            int index = 1;
            ps.setInt(index++, cinemaId);
            ps.setString(index++, date); 

            if (genre != null && !genre.isEmpty()) {
                ps.setString(index++, genre);
            }
            if (ageRating != null && !ageRating.isEmpty()) {
                ps.setString(index++, ageRating);
            }

            Map<Integer, Movie> movieMap = new LinkedHashMap<>();

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Showtime showtime = new Showtime(
                        rs.getInt("ShowtimeId"),
                        rs.getInt("MovieId"),
                        rs.getInt("RoomId"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime"),
                        rs.getDouble("TicketPrice")
                    );

                    int movieId = showtime.getMovieId();

                    if (!movieMap.containsKey(movieId)) {
                        Movie movie = MovieMapper.mapMovie(rs);
                        movie.setShowtimes(new ArrayList<>()); 
                        movieMap.put(movieId, movie);
                    }

                    movieMap.get(movieId).getShowtimes().add(showtime);
                }
            }

            results.addAll(movieMap.values());

        } catch (SQLException e) {
            System.out.println("Error fetching showtimes by filter: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }
    
    public List<Showtime> getShowtimesByRoom(int roomId) {
        List<Showtime> showtimes = new ArrayList<>();
        String query = "SELECT * FROM Showtime WHERE RoomId = ? ORDER BY StartTime";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Showtime showtime = new Showtime(
                        rs.getInt("ShowtimeId"),
                        rs.getInt("MovieId"),
                        rs.getInt("RoomId"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime"),
                        rs.getDouble("TicketPrice")
                    );
                    showtimes.add(showtime);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return showtimes;
    }
    
    public Showtime getShowtimeById(int showtimeId) {
        String query = "SELECT * FROM Showtime WHERE ShowtimeId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, showtimeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Showtime(
                        rs.getInt("ShowtimeId"),
                        rs.getInt("MovieId"),
                        rs.getInt("RoomId"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime"),
                        rs.getDouble("TicketPrice")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public List<Showtime> searchByAdmin(
            String movieName,
            String startTime,
            String endTime) {

        List<Showtime> list = new ArrayList<>();

        String sql =
            "SELECT s.* FROM Showtime s " +
            "JOIN Movie m ON s.MovieId = m.MovieId " +
            "WHERE 1=1 ";

        if (movieName != null && !movieName.isEmpty()) {
            sql += " AND m.Title LIKE ? ";
        }
        if (startTime != null && !startTime.isEmpty()) {
            sql += " AND s.StartTime >= ? ";
        }
        if (endTime != null && !endTime.isEmpty()) {
            sql += " AND s.EndTime <= ? ";
        }

        sql += " ORDER BY s.StartTime";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;
            if (movieName != null && !movieName.isEmpty()) {
                ps.setString(i++, "%" + movieName + "%");
            }
            if (startTime != null && !startTime.isEmpty()) {
                ps.setString(i++, startTime.replace("T", " "));
            }
            if (endTime != null && !endTime.isEmpty()) {
                ps.setString(i++, endTime.replace("T", " "));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Showtime(
                    rs.getInt("ShowtimeId"),
                    rs.getInt("MovieId"),
                    rs.getInt("RoomId"),
                    rs.getString("StartTime"),
                    rs.getString("EndTime"),
                    rs.getDouble("TicketPrice")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public List<Showtime> searchByAdminInRoom(
            int roomId,
            String movieName,
            String startTime,
            String endTime) {

        List<Showtime> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT s.* FROM Showtime s " +
            "JOIN Movie m ON s.MovieId = m.MovieId " +
            "WHERE s.RoomId = ? "
        );

        if (movieName != null && !movieName.isEmpty()) {
            sql.append(" AND m.Title LIKE ? ");
        }
        if (startTime != null && !startTime.isEmpty()) {
            sql.append(" AND s.StartTime >= ? ");
        }
        if (endTime != null && !endTime.isEmpty()) {
            sql.append(" AND s.EndTime <= ? ");
        }

        sql.append(" ORDER BY s.StartTime");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;
            ps.setInt(i++, roomId);

            if (movieName != null && !movieName.isEmpty()) {
                ps.setString(i++, "%" + movieName + "%");
            }
            if (startTime != null && !startTime.isEmpty()) {
                ps.setString(i++, startTime.replace("T", " "));
            }
            if (endTime != null && !endTime.isEmpty()) {
                ps.setString(i++, endTime.replace("T", " "));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Showtime(
                    rs.getInt("ShowtimeId"),
                    rs.getInt("MovieId"),
                    rs.getInt("RoomId"),
                    rs.getString("StartTime"),
                    rs.getString("EndTime"),
                    rs.getDouble("TicketPrice")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}