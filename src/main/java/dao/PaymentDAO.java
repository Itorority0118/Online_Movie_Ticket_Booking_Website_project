package dao;

import java.sql.*;
import java.util.*;

import model.Payment;
import utils.DBConnection;

public class PaymentDAO {
	
	public int createPayment(
	        Payment p,
	        Connection conn
	) throws SQLException {

	    String sql = """
	        INSERT INTO Payment
	        (TicketId, PaymentMethod, Amount, PaymentDate, Status)
	        VALUES (?, ?, ?, ?, ?)
	    """;

	    try (PreparedStatement ps =
	             conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setInt(1, p.getTicketId());
	        ps.setString(2, p.getPaymentMethod());
	        ps.setBigDecimal(3, p.getAmount());
	        ps.setTimestamp(4,
	            new Timestamp(p.getPaymentDate().getTime()));
	        ps.setString(5, p.getStatus());

	        ps.executeUpdate();

	        ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()) return rs.getInt(1);
	    }

	    return -1;
	}


    public Payment getPaymentById(int id) {

        String sql = "SELECT * FROM Payment WHERE PaymentId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapPayment(rs);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching payment by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Payment> getAllPayments() {

        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment ORDER BY PaymentDate ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(mapPayment(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching all payments: " + e.getMessage());
        }

        return payments;
    }

    public boolean updatePaymentStatus(int paymentId, String status) {

        String sql = "UPDATE Payment SET Status = ? WHERE PaymentId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, paymentId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error updating payment status: " + e.getMessage());
            return false;
        }
    }

    public int countPayments() {

        String sql = "SELECT COUNT(*) FROM Payment";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error counting payments: " + e.getMessage());
        }
        return 0;
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {

        Payment p = new Payment();
        p.setPaymentId(rs.getInt("PaymentId"));
        p.setTicketId(rs.getInt("TicketId"));
        p.setPaymentMethod(rs.getString("PaymentMethod"));
        p.setAmount(rs.getBigDecimal("Amount"));
        p.setPaymentDate(rs.getTimestamp("PaymentDate"));
        p.setStatus(rs.getString("Status"));
        return p;
    }
    
    public List<Payment> filterPayments(
            Integer ticketId, String status, String method) {

        List<Payment> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM Payment WHERE 1=1"
        );

        if (ticketId != null) sql.append(" AND TicketId = ?");
        if (status != null && !status.isEmpty()) sql.append(" AND Status = ?");
        if (method != null && !method.isEmpty())
            sql.append(" AND PaymentMethod LIKE ?");

        sql.append(" ORDER BY PaymentDate ASC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (ticketId != null) ps.setInt(idx++, ticketId);
            if (status != null && !status.isEmpty())
                ps.setString(idx++, status);
            if (method != null && !method.isEmpty())
                ps.setString(idx++, "%" + method + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapPayment(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean deletePayment(int id) {

        String sql = "DELETE FROM Payment WHERE PaymentId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting payment: " + e.getMessage());
            return false;
        }
    }
    
    public double getTotalRevenue() {
        String sql = "SELECT SUM(Amount) FROM Payment WHERE Status = 'SUCCESS'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Error calculating total revenue: " + e.getMessage());
        }
        return 0;
    }
    
    public double getRevenueByStatus(String status) {
        String sql = "SELECT SUM(Amount) FROM Payment WHERE Status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Error calculating revenue for status '" + status + "': " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}