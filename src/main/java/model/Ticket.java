package model;

public class Ticket {
    
    private int ticketId;
    private int userId;          // Foreign key to User
    private int showtimeId;      // Foreign key to Showtime
    private int seatId;          // Foreign key to Seat
    private double price;        // Final ticket price
    private String bookingTime;  // Example: "2025-10-20 18:45"
    private String status;       // "Booked", "Cancelled", "Used"

    // Empty constructor
    public Ticket() {}

    // Constructor
    public Ticket(int ticketId, int userId, int showtimeId, int seatId, 
                  double price, String bookingTime, String status) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.seatId = seatId;
        this.price = price;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    // Getters
    public int getTicketId() { return ticketId; }
    public int getUserId() { return userId; }
    public int getShowtimeId() { return showtimeId; }
    public int getSeatId() { return seatId; }
    public double getPrice() { return price; }
    public String getBookingTime() { return bookingTime; }
    public String getStatus() { return status; }

    // Setters
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }
    public void setPrice(double price) { this.price = price; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }
    public void setStatus(String status) { this.status = status; }
}
