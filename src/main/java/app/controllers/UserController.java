package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class UserController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.post("login", ctx -> adminLoginPage(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.get("opretbruger", ctx -> ctx.render("opretbruger.html"));
        app.post("opretbruger", ctx -> opretBruger(ctx, connectionPool));

        app.post("adminLogin", ctx -> adminLogin(ctx,connectionPool));
        app.get("/admin", ctx -> adminLoginPage(ctx,connectionPool));

    }

    private static void adminLoginPage(Context ctx, ConnectionPool connectionPool) {
        ctx.render("adminlogin.html");
    }

    private static void opretBruger(Context ctx, ConnectionPool connectionPool)
    {
        // Hent form parametre
        String username = ctx.formParam("email");
        String password1 = ctx.formParam("password");
        String password2 = ctx.formParam("password2");

        if (password1.equals(password2))
        {
            try
            {
                UserMapper.opretBruger(username, password1, connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med brugernavn: " + username +
                        ". Nu skal du logge på.");
                ctx.render("index.html");
            }

            catch (DatabaseException e)
            {
                ctx.attribute("message", "Dit brugernavn findes allerede. Prøv igen, eller log ind");
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


    public static void adminLogin(Context ctx, ConnectionPool connectionPool)
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
            ctx.render("adminlogin.html");
        }
    }
}