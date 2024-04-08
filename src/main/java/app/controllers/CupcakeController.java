package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;


import static app.controllers.UserController.login;

public class CupcakeController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> index(ctx, connectionPool));
        app.post("/", ctx -> index(ctx, connectionPool));
        app.post("/addtocart", ctx -> addToCart(ctx, connectionPool));
    }


    private static void addToCart(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        List<Top> topList = CupcakeMapper.getAllTops(connectionPool);
        List<Bottom> bottomList = CupcakeMapper.getAllBottoms(connectionPool);
        ctx.attribute("topList", topList);
        ctx.attribute("bottomList", bottomList);

        String topIdStr = ctx.formParam("top");
        String bottomIdStr = ctx.formParam("bottom");
        String quantityStr = ctx.formParam("quantity");

        if (topIdStr == null || topIdStr.isEmpty() || bottomIdStr == null || bottomIdStr.isEmpty() || quantityStr == null || quantityStr.isEmpty()) {
            ctx.attribute("message", "Du skal vælge både top, bund og antal.");
            ctx.render("index.html");
            return;
        }

        try {
            int topId = Integer.parseInt(topIdStr);
            int bottomId = Integer.parseInt(bottomIdStr);
            int quantity = Integer.parseInt(quantityStr);

            Top top = CupcakeMapper.getTopById(topId, connectionPool);
            Bottom bottom = CupcakeMapper.getBottomById(bottomId, connectionPool);

            Cart cart = ctx.sessionAttribute("cart");
            if (cart == null) {
                cart = new Cart();
            }

            cart.add(top, bottom, quantity);
            ctx.sessionAttribute("cart", cart);
        } catch (NumberFormatException e) {
            ctx.attribute("message", "Ugyldigt format for top, bund eller antal.");
        }

        ctx.render("index.html");
    }


    private static void index(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        Cart cart = ctx.sessionAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            ctx.sessionAttribute("cart", cart);
        }
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





