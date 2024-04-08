package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    // public static User login(String userName, String password, ConnectionPool connectionPool) throws DatabaseException

    public static User adminLoginCheck(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users where email=? and password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                boolean admin = rs.getBoolean("admin");
                double balance = rs.getDouble("balance");

                return new User(userId, name, password, email, admin, balance);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static void opretBruger(String name, String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into users (name, email, password) values (?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "E-mailen findes allerede. Vælg et andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
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
                if (newBalance < 0) {
                    throw new DatabaseException("Der er ikke nok penge på kontoen til at fuldføre transaktionen.");
                }

                String updatesql = "update users set balance=? where user_id=?";
                PreparedStatement ps02 = connection.prepareStatement(updatesql);
                ps02.setDouble(1, newBalance);
                ps02.setInt(2, user.getUserId());
                ps02.executeUpdate();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }


    }

    public static void depositToBalance(int userId, double depositAmount, ConnectionPool connectionPool) {

        String sql = "SELECT * from users where user_id=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getInt("balance");
                double newBalance = currentBalance + depositAmount;
                if (0 > depositAmount) {
                    System.out.println("Du kan ikke trække penge fra kunden");

                }
                String updatesql = "update users set balance=? where user_id=?";
                PreparedStatement ps02 = connection.prepareStatement(updatesql);
                ps02.setDouble(1, newBalance);
                ps02.setInt(2, userId);
                ps02.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}