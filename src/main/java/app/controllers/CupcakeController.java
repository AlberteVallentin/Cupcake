package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;


import static app.controllers.UserController.adminLogin;

public class CupcakeController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> index(ctx, connectionPool));
        app.post("/addtocart", ctx -> addToCart(ctx, connectionPool));
        app.get("/cart", ctx -> cart(ctx, connectionPool));
        app.post("/createordre", ctx -> createordre(ctx,connectionPool));
      //  app.post("deleteorder", ctx -> deleteorder(ctx,false,connectionPool));

    }


    private static void createordre(Context ctx, ConnectionPool connectionPool) {

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
                if (cart == null)
                {
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





