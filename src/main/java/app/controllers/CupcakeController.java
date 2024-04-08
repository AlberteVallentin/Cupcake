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
        app.post("/addtocart", ctx -> addToCart(ctx, connectionPool));
        app.get("/cart", ctx -> cart(ctx, connectionPool));
        app.post("/receipt", ctx -> showReceipt(ctx, connectionPool));
        app.get("/receipt", ctx -> cart(ctx, connectionPool));

        //  app.post("deleteorder", ctx -> deleteorder(ctx,false,connectionPool));

    }


    private static void showReceipt(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        Cart cart = ctx.sessionAttribute("cart");

        if (user != null && cart != null) {
            try {
                // Calculate total price of the cart
                double totalPrice = cart.getTotal();

                // Insert order into the orders table
                int orderId = CupcakeMapper.insertOrder(user.getUserId(), totalPrice, connectionPool);

                // Insert order lines into the order_lines table
                for (CartLine item : cart.getCartLines()) {
                    CupcakeMapper.insertOrderLine(orderId, item.getTop().getId(), item.getBottom().getId(), item.getQuantity(), connectionPool);
                }

                //Withdraw from balance
                CupcakeMapper.withdrawFromBalance(user, totalPrice, connectionPool);
            } catch (DatabaseException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("index.html");
                return;
            }
        }

        // Clear the cart after saving the items
        ctx.sessionAttribute("cart", null);

        // Render the receipt template
        ctx.render("receipt.html");
    }



/*
    private static void deleteorder(Context ctx, boolean b, ConnectionPool connectionPool) {

        User user = ctx.sessionAttribute("currentUser");
        try {

            int taskId = Integer.parseInt(ctx.formParam("taskId"));
            CupcakeMapper.delete(taskId,connectionPool);
            List<Order> orderlist = CupcakeMapper.getAllOrdersPerUser(user.getUserId(), connectionPool);
            ctx.attribute("orderlist", orderlist);
            ctx.render("kurv.html");

        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

 */

    private static void cart(Context ctx, ConnectionPool connectionPool) {
        ctx.render("kurv.html");
    }




    private static void addToCart(Context ctx, ConnectionPool connectionPool) {

        User user = ctx.sessionAttribute("currentUser");
        try {
            int topId = Integer.parseInt(ctx.formParam("top"));
            int bottomId = Integer.parseInt(ctx.formParam("bottom"));
            int quantity = Integer.parseInt(ctx.formParam("quantity"));

            Top top = CupcakeMapper.getTopById(topId, connectionPool);
            Bottom bottom = CupcakeMapper.getBottomById(bottomId, connectionPool);

            Cart cart = ctx.sessionAttribute("cart");
            if (cart == null) {
                cart = new Cart();
            }

            cart.add(top, bottom, quantity);
            ctx.sessionAttribute("cart", cart);
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





