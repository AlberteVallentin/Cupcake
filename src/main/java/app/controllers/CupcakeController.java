package app.controllers;

import app.entities.Bottom;
import app.entities.Top;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CupcakeController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> index(ctx, connectionPool));
        app.post("/addtocart", ctx -> addToCart(ctx, connectionPool));
    }

    private static void addToCart(Context ctx, ConnectionPool connectionPool) {
            User user = ctx.sessionAttribute("currentUser");
            try {
                int topId = Integer.parseInt(ctx.formParam("topId"));
                String name = ctx.formParam("name");
                int price = Integer.parseInt(ctx.formParam("price"));
                List<Top> topList = CupcakeMapper.getAllTops(connectionPool);
                ctx.attribute("topList", topList);
                ctx.render("index.html");


            } catch (NumberFormatException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("index.html");
            }
        }
        private static void index(Context ctx, ConnectionPool connectionPool) {
            User user = ctx.sessionAttribute("currentUser");
            try {
                List<Top> topList = CupcakeMapper.getAllTops(connectionPool);
                List<Bottom> bottomList = CupcakeMapper.getAllBottoms(connectionPool);
                ctx.attribute("topList", topList);
                ctx.attribute("bottomList", bottomList);
                ctx.render("index.html");


            } catch (NumberFormatException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("index.html");
            }


    }


}
