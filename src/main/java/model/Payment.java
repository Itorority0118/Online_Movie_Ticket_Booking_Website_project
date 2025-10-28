package model;

public class Payment {
    
    private int paymentId;
    private int ticketId;         // Foreign key to Ticket
    private String paymentMethod; // Example: "Credit Card", "Momo", "ZaloPay"
    private double amount;        // Total payment amount
    private String paymentDate;   // Example: "2025-10-20 19:00"
    private String status;        // "Success", "Failed", "Pending"

    // Empty constructor
    public Payment() {}

    // Constructor
    public Payment(int paymentId, int ticketId, String paymentMethod, 
                   double amount, String paymentDate, String status) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // Getters
    public int getPaymentId() { return paymentId; }
    public int getTicketId() { return ticketId; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getAmount() { return amount; }
    public String getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }

    // Setters
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public void setStatus(String status) { this.status = status; }
}