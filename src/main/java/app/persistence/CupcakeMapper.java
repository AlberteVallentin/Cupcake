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


}
