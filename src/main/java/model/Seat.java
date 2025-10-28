package model;

public class Seat {
    
    private int seatId;
    private int roomId;         // Foreign key to Room
    private String seatNumber;  // Example: "A1", "B10"
    private String seatType;    // Example: "Regular", "VIP"
    private String status;      // "Available", "Booked", "Unavailable"

    // Empty constructor
    public Seat() {}

    // Constructor
    public Seat(int seatId, int roomId, String seatNumber, String seatType, String status) {
        this.seatId = seatId;
        this.roomId = roomId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.status = status;
    }

    // Getters
    public int getSeatId() { return seatId; }
    public int getRoomId() { return roomId; }
    public String getSeatNumber() { return seatNumber; }
    public String getSeatType() { return seatType; }
    public String getStatus() { return status; }

    // Setters
    public void setSeatId(int seatId) { this.seatId = seatId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setSeatType(String seatType) { this.seatType = seatType; }
    public void setStatus(String status) { this.status = status; }
}