package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Payment;

public class PaymentMapper {

    public static Payment mapPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("PaymentId"));
        p.setTicketId(rs.getInt("TicketId"));
        p.setPaymentMethod(rs.getString("PaymentMethod"));
        p.setAmount(rs.getBigDecimal("Amount"));
        p.setPaymentDate(rs.getTimestamp("PaymentDate"));
        p.setStatus(rs.getString("Status"));
        return p;
    }
}
