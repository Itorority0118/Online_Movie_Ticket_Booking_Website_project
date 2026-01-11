package model;

public class Seat {

    private int seatId;
    private int roomId;

    private String seatNumber; 
    private String seatRow;   
    private int seatCol;     

    private String seatType;  
    private String status;   

    public Seat() {}

    public Seat(int seatId, int roomId, String seatNumber,
                String seatRow, int seatCol,
                String seatType, String status) {
        this.seatId = seatId;
        this.roomId = roomId;
        this.seatNumber = seatNumber;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.seatType = seatType;
        this.status = status;
    }

    public int getSeatId() { return seatId; }
    public int getRoomId() { return roomId; }

    public String getSeatNumber() { return seatNumber; }
    public String getSeatRow() { return seatRow; }
    public int getSeatCol() { return seatCol; }

    public String getSeatType() { return seatType; }
    public String getStatus() { return status; }

    public void setSeatId(int seatId) { this.seatId = seatId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setSeatRow(String seatRow) { this.seatRow = seatRow; }
    public void setSeatCol(int seatCol) { this.seatCol = seatCol; }

    public void setSeatType(String seatType) { this.seatType = seatType; }
    public void setStatus(String status) { this.status = status; }


    public boolean isDouble() {
        return "DOUBLE".equalsIgnoreCase(seatType);
    }

    public boolean isVIP() {
        return "VIP".equalsIgnoreCase(seatType);
    }

    public boolean isBooked() {
        return "Booked".equalsIgnoreCase(status);
    }

}
