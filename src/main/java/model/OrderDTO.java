package model;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class OrderDTO {
    private int ticketId;
    private String movieTitle;
    private Timestamp showtime;
    private int price;
    private Timestamp bookingTime;
    private String status;
    private String seatLabel;
	public OrderDTO(int ticketId, String movieTitle, Timestamp showtime, int price, Timestamp bookingTime,
			String status, String seatLabel) {
		super();
		this.ticketId = ticketId;
		this.movieTitle = movieTitle;
		this.showtime = showtime;
		this.price = price;
		this.bookingTime = bookingTime;
		this.status = status;
		this.seatLabel = seatLabel;
	}
	public OrderDTO() {
		// TODO Auto-generated constructor stub
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
    
}
