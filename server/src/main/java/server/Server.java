package server;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import service.AlreadyTaken;
import service.AlreadyTakenException;
import service.Service;


public class Server {

    private final Javalin javalin;

    private final Service service = new Service();

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
        //handler
        Gson serializer = new Gson();
        RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        //service

        try{
        RegisterResult javaObject = service.registerService(request);

            ctx.status(200);
            String result = serializer.toJson(javaObject);
            ctx.result(result);
        }
        catch (AlreadyTakenException ex){
            ctx.status(403);
            AlreadyTaken message = new AlreadyTaken("Error username already taken");
            String json = serializer.toJson(message);
            ctx.result(json);

        }





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
