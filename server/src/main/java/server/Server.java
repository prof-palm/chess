package server;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import service.*;


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
    private void register(Context ctx) {
        Gson serializer = new Gson();
        RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        try {
            CheckRequest(request);
            registerHelper(ctx, request, serializer);
        } catch (BadRequestException bde) {
            ctx.status(400);
            ExceptionMessage message = new ExceptionMessage("Error: bad request");
            String json = serializer.toJson(message);
            ctx.result(json);
        }
    }


    public void registerHelper(Context ctx, RegisterRequest request, Gson serializer){
        try{
        RegisterResult javaObject = service.registerService(request);

            ctx.status(200);
            String result = serializer.toJson(javaObject);
            ctx.result(result);
        }
        catch (AlreadyTakenException ex){
            ctx.status(403);
            ExceptionMessage message = new ExceptionMessage("Error: already taken");
            String json = serializer.toJson(message);
            ctx.result(json);

        }

        catch(Exception ex){
            ctx.status(500);
            ExceptionMessage message = new ExceptionMessage("Error: (description of error)");
            String json = serializer.toJson(message);
            ctx.result(json);


        }
    }




    public void CheckRequest(RegisterRequest request) throws BadRequestException{
        if(request.username() == null || request.password() == null || request.email() == null){
            throw new BadRequestException();
        }

    }

    private void login(Context ctx){
        Gson serializer = new Gson();
        LoginRequest request = serializer.fromJson(ctx.body(), LoginRequest.class);
        try{
            CheckRequest(request);
            loginHelper(ctx, request, serializer);

        }
        catch(BadRequestException bde){
            ctx.status(400);
            ExceptionMessage message = new ExceptionMessage("Error: bad request");
            String json = serializer.toJson(message);
            ctx.result(json);

        }


    }
    public void CheckRequest(LoginRequest request) throws BadRequestException{
        if(request.username() == null || request.password() == null){
            throw new BadRequestException();
        }

    }

    public void loginHelper(Context ctx, LoginRequest request, Gson serializer){
        try{
            LoginResult javaObject = service.loginService(request);

            ctx.status(200);
            String result = serializer.toJson(javaObject);
            ctx.result(result);
        }
        catch (InvalidLoginException ex){
            ctx.status(401);
            ExceptionMessage message = new ExceptionMessage("Error: Unauthorized");
            String json = serializer.toJson(message);
            ctx.result(json);

        }
        catch(BadRequestException ex){
            ctx.status(400);
            ExceptionMessage message = new ExceptionMessage("Error: Unauthorized");
            String json = serializer.toJson(message);
            ctx.result(json);

        }

        catch(Exception ex){
            ctx.status(500);
            ExceptionMessage message = new ExceptionMessage("Error: (description of error)");
            String json = serializer.toJson(message);
            ctx.result(json);


        }


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
