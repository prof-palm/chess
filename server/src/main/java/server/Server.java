package server;

import io.javalin.Javalin;
import io.javalin.http.Context;


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("user",this::register);


    }
    private void register(Context ctx){
        ctx.status(200);
        ctx.result("{\"user\": \"bob\"}\n");
        return;

    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
