package model;

public class Room {
    
    private int roomId;
    private int cinemaId;       // Foreign key to Cinema
    private String roomName;
    private int seatCount;
    private String roomType;    // Example: "2D", "3D", "IMAX"

    // Empty constructor
    public Room() {}

    // Constructor
    public Room(int roomId, int cinemaId, String roomName, int seatCount, String roomType) {
        this.roomId = roomId;
        this.cinemaId = cinemaId;
        this.roomName = roomName;
        this.seatCount = seatCount;
        this.roomType = roomType;
    }

    // Getters
    public int getRoomId() { return roomId; }
    public int getCinemaId() { return cinemaId; }
    public String getRoomName() { return roomName; }
    public int getSeatCount() { return seatCount; }
    public String getRoomType() { return roomType; }

    // Setters
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
}