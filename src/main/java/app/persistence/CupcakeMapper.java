package app.persistence;

import app.entities.Bottom;
import app.entities.Top;
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


}
