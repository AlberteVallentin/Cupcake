package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class UserController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {

        app.get("opretbruger", ctx -> ctx.render("opretbruger.html"));
        app.post("opretbruger", ctx -> opretBruger(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));

        app.post("/login", ctx -> login(ctx,connectionPool));
        app.get("/loginpage", ctx -> loginPage(ctx,connectionPool));
        app.get("/adminpage", ctx -> adminPage(ctx,connectionPool));

        app.post("/addMoney", ctx -> addMoney(ctx,connectionPool));

    }

    private static void addMoney(Context ctx, ConnectionPool connectionPool) {
        List<User> userList = CupcakeMapper.getAllUsers(connectionPool);

        int userId = Integer.parseInt(ctx.formParam("userList"));

        double amount = Double.parseDouble(ctx.formParam("amount"));

        CupcakeMapper.depositToBalance(userId, amount, connectionPool);

        userList = CupcakeMapper.getAllUsers(connectionPool);

        ctx.attribute("userList", userList);
        ctx.render("adminpage.html");

    }

    private static void adminPage(Context ctx, ConnectionPool connectionPool) {
        ctx.render("adminpage.html");
    }

    private static void loginPage (Context ctx, ConnectionPool connectionPool) {

        ctx.render("login.html");
    }


    private static void opretBruger(Context ctx, ConnectionPool connectionPool)
    {
        // Hent form parametre
        String name = ctx.formParam("name");
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        if (password1.equals(password2))
        {
            try
            {
                UserMapper.opretBruger(name, email, password1, connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med e-mailen: " + email +
                        ". Nu skal du logge på.");
                ctx.render("login.html");
            }

            catch (DatabaseException e)
            {
                ctx.attribute("message", "Din e-mail findes allerede. Prøv igen, eller log ind");
                ctx.render("opretbruger.html");
            }
        } else
        {
            ctx.attribute("message", "Dine to passwords matcher ikke! Prøv igen");
            ctx.render("opretbruger.html");
        }

    }

    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }


    public static void login(Context ctx, ConnectionPool connectionPool)
    {
        // Hent form parametre
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        // Check om bruger findes i DB med de angivne username + password
        try
        {
            User user = UserMapper.adminLoginCheck(email, password,connectionPool);
            ctx.sessionAttribute("currentUser", user);
            // Hvis ja, send videre til admin page
            if(user.getAdmin()) {
                List<User> userList = CupcakeMapper.getAllUsers(connectionPool);
                ctx.attribute("userList", userList);
                ctx.render("adminpage.html");
            }
            else{
                ctx.render("kurv.html");
            }
        }
        catch (DatabaseException e)
        {
            // Hvis nej, send tilbage til login side med fejl besked
            ctx.attribute("message","Fejl i enten email eller kode");
            ctx.render("login.html");
        }
    }
}