package model;

import java.sql.Timestamp;

public class OrderDTO {

    private int ticketId;
    private String movieTitle;

    // Thời gian chiếu
    private Timestamp showtime;

    // Thông tin rạp
    private String cinemaName;
    private String roomName;

    private int price;
    private Timestamp bookingTime;
    private String status;
    private String seatLabel;

    /* ================= CONSTRUCTORS ================= */

    // Constructor đầy đủ (dùng khi map SQL)
    public OrderDTO(
            int ticketId,
            String movieTitle,
            Timestamp showtime,
            String cinemaName,
            String roomName,
            int price,
            Timestamp bookingTime,
            String status,
            String seatLabel
    ) {
        this.ticketId = ticketId;
        this.movieTitle = movieTitle;
        this.showtime = showtime;
        this.cinemaName = cinemaName;
        this.roomName = roomName;
        this.price = price;
        this.bookingTime = bookingTime;
        this.status = status;
        this.seatLabel = seatLabel;
    }

    public OrderDTO() {
    }


    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Timestamp getShowtime() {
        return showtime;
    }

    public void setShowtime(Timestamp showtime) {
        this.showtime = showtime;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Timestamp bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    public void setSeatLabel(String seatLabel) {
        this.seatLabel = seatLabel;
    }
    
    public String getShowtimeFormatted() {
        if (showtime == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(showtime);
    }
}