package server;

import io.javalin.Javalin;
import io.javalin.http.Context;


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("user",this::register);
        javalin.post("session", this::login);
        javalin.delete("session", this::logout);
        javalin.get("game", this::listGames);
        javalin.post("game", this::createGame);
        javalin.put("game", this::joinGame);
        javalin.delete("db", this::clear);


    }
    private void register(Context ctx){
        ctx.status(200);
        Gson serializer = new Gson();
        RegisterRequest request = serialzer.fromJson(ctx.body, RegisterRequest.class)
        RegisterResult(request);
        ctx.result("{\"username\": \"bob\"\n, \"authToken\": \"string\"}");


    }

    private void login(Context ctx){
        ctx.status(200);
        ctx.result("{}");
    }

    private void logout(Context ctx){
        ctx.status(200);
        ctx.result("{}");

    }
    private void listGames(Context ctx){
        ctx.status(200);
        ctx.result("{}");

    }
    private void createGame(Context ctx){
        ctx.status(200);
        ctx.result("{}");
    }

    private void joinGame(Context ctx){
        ctx.status(200);
        ctx.result("{}");
    }
    private void clear(Context ctx){
        ctx.status(200);
        ctx.result("{}");
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
