package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper {

    public static int insertOrder(int userId, double totalPrice, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (user_id, total_price) VALUES (?, ?) RETURNING order_id";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, totalPrice);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("order_id");
            } else {
                throw new DatabaseException("Failed to insert order. No order ID returned.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting order: " + e.getMessage());
        }
    }

    public static void insertOrderLine(int orderId, int topId, int bottomId, int quantity, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO order_lines (order_id, top_id, bottom_id, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, topId);
            stmt.setInt(3, bottomId);
            stmt.setInt(4, quantity);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting order line: " + e.getMessage());
        }
    }
}
