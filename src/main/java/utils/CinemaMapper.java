package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Cinema;

public class CinemaMapper {
    public static Cinema map(ResultSet rs) throws SQLException {
        Cinema cinema = new Cinema();
        cinema.setCinemaId(rs.getInt("CinemaId"));
        cinema.setName(rs.getString("Name"));
        cinema.setAddress(rs.getString("Address"));
        cinema.setCity(rs.getString("City"));
        cinema.setPhone(rs.getString("Phone"));
        return cinema;
    }
}
