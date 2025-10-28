package model;
public class Showtime {
    
    private int showtimeId;
    private int movieId;        // Foreign key to Movie
    private int roomId;         // Foreign key to Room
    private String startTime;   // Example: "2025-10-20 19:30"
    private String endTime;     // Example: "2025-10-20 21:45"
    private double ticketPrice; // Base price per seat

    // Empty constructor
    public Showtime() {}

    // Constructor
    public Showtime(int showtimeId, int movieId, int roomId, String startTime, 
                    String endTime, double ticketPrice) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
    }

    // Getters
    public int getShowtimeId() { return showtimeId; }
    public int getMovieId() { return movieId; }
    public int getRoomId() { return roomId; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public double getTicketPrice() { return ticketPrice; }

    // Setters
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
}