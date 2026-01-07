package model;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
    private int paymentId;
    private int ticketId;
    private String paymentMethod;
    private BigDecimal amount;
    private Date paymentDate;
    private String status;

    public Payment() {}

    public Payment(int ticketId, String paymentMethod,
                   BigDecimal amount, Date paymentDate, String status) {
        this.ticketId = ticketId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // getters & setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}