package app.persistence;

import app.entities.Bottom;
import app.entities.Order;
import app.entities.Top;
import app.entities.User;
import app.exceptions.DatabaseException;
import io.javalin.Javalin;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CupcakeMapper {
    public static List<User> getAllUsers(ConnectionPool connectionPool) {
        String sql = "SELECT * FROM users";
        List<User> userList = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String email = rs.getString("email");
                boolean admin = rs.getBoolean("admin");
                double balance = rs.getDouble("balance");

                User user = new User(userId, name, password, email, admin, balance);
                userList.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public static List<Top> getAllTops(ConnectionPool connectionPool) {
        String sql = "SELECT * FROM top";
        List<Top> topList = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int topId = rs.getInt("top_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");

                Top top = new Top(topId, name, price);
                topList.add(top);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topList;
    }

    public static Top getTopById(int id, ConnectionPool connectionPool) {
        String sql = "SELECT * FROM top WHERE top_id = ?";
        Top top = null;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int topId = rs.getInt("top_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                top = new Top(topId, name, price);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return top;
    }


    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) {
        String sql = "SELECT * FROM bottom";
        List<Bottom> bottomList = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int bottomId = rs.getInt("bottom_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");

                Bottom bottom = new Bottom(bottomId, name, price);
                bottomList.add(bottom);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bottomList;
    }

    public static Bottom getBottomById(int id, ConnectionPool connectionPool) {
        String sql = "SELECT * FROM bottom WHERE bottom_id = ?";
       Bottom bottom = null;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int bottomId = rs.getInt("bottom_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                bottom = new Bottom(bottomId, name, price);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bottom;
    }

    public static void withdrawFromBalance(User user, double totalPrice, ConnectionPool connectionPool) {

        String sql = "SELECT * from users where user_id=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, user.getUserId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getInt("balance");
                double newBalance = currentBalance - totalPrice;
                if (newBalance < totalPrice) {
                    System.out.println("Insufficient funds!");

                }
                String updatesql = "update users set balance=? where user_id=?";
                PreparedStatement ps02 = connection.prepareStatement(updatesql);
                ps02.setDouble(1, newBalance);
                ps02.setInt(2, user.getUserId());
                ps02.executeUpdate();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


     /*
    public static void delete(int orderId, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "delete from orders where order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl i opdatering af en order");
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl ved sletning af en order", e.getMessage());
        }
    }


    public static List<Order> getAllOrdersPerUser(int userId, ConnectionPool connectionPool)throws DatabaseException{

        List<Order> orderList = new ArrayList<>();
        String sql = "select * from orders where costumer_id=? order by order_id  ";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("costumer_id");
                int orderId = rs.getInt("order_id");
                orderList.add(new Order(id, orderId, userId));
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl!!!!", e.getMessage());
        }
        return orderList;

    }

     */

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
