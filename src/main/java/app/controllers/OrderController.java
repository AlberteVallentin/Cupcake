package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/cart", ctx -> cart(ctx, connectionPool));
        app.post("/receipt", ctx -> showReceipt(ctx, connectionPool));
        app.post("/deletecartline", ctx -> deletecartline(ctx, connectionPool));
        app.get("/receipt", ctx -> cart(ctx, connectionPool));

    }

    private static void showReceipt(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        Cart cart = ctx.sessionAttribute("cart");

        if (user != null && cart != null) {
            try {
                // Calculate total price of the cart
                double totalPrice = cart.getTotal();

                // Check if user has enough balance to make a purchase
                if (user.getBalance() < totalPrice) {
                    ctx.attribute("message", "Du har ikke nok penge på din konto til at gennemføre købet.");
                    ctx.render("kurv.html");
                    return;
                }

                // Insert order into the orders table
                int orderId = OrderMapper.insertOrder(user.getUserId(), totalPrice, connectionPool);

                // Insert order lines into the order_lines table
                for (CartLine item : cart.getCartLines()) {
                    OrderMapper.insertOrderLine(orderId, item.getTop().getId(), item.getBottom().getId(), item.getQuantity(), connectionPool);
                }

                // Withdraw from user's balance
                UserMapper.withdrawFromBalance(user, totalPrice, connectionPool);

                // Clear the cart after saving the items
                ctx.sessionAttribute("cart", null);

                // Render the receipt template
                ctx.render("receipt.html");

            } catch (RuntimeException | DatabaseException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("kurv.html");
            }
        } else {
            // Handle the case where there cart is null
            ctx.attribute("message", "Du skal have varer i din kurv for at se en kvittering.");
            ctx.render("kurv.html");
        }
    }




    private static void deletecartline(Context ctx, ConnectionPool connectionPool) {

        User user = ctx.sessionAttribute("currentUser");
        try {

            int cartIndex = Integer.parseInt(ctx.formParam("cartIndex"));
            Cart cart = ctx.sessionAttribute("cart");
            cart.getCartLines().remove(cartIndex);
            ctx.sessionAttribute("cart", cart);
            ctx.render("kurv.html");
        } catch (NumberFormatException e) {}
    }



    private static void cart(Context ctx, ConnectionPool connectionPool) {

        ctx.render("kurv.html");
    }





}
