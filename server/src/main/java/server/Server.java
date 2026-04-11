package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.*;
import service.*;

import java.util.Collection;


public class Server {

    private final Javalin javalin;
    private final Service service;

    public Server(){
        this(new Service());
    }

    public Server(Service service) {

        this.service = service;


        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("user",this::register);
        javalin.post("session", this::login);
        javalin.delete("session", this::logout);
        javalin.get("game", this::listGames);
        javalin.post("game", this::createGame);
        javalin.put("game", this::joinGame);
        javalin.delete("db", this::clear);
        javalin.ws("/ws", ws -> {
            ws.onConnect(ctx->{
                ctx.enableAutomaticPings();
                System.out.println("Websocket connected");
            });
            ws.onMessage(ctx -> ctx.send("Websocket response" + ctx.message()));
            ws.onClose(_ -> System.out.println("Websocket closed"));

        }).start(8080);


    }
    private void register(Context ctx) {
        Gson serializer = new Gson();
        RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        try {
            checkRequest(request);
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

        catch(DataAccessException ex){
            ctx.status(500);
            ExceptionMessage message = new ExceptionMessage("Error: (description of error)");
            String json = serializer.toJson(message);
            ctx.result(json);


        }
    }




    public void checkRequest(RegisterRequest request) throws BadRequestException{
        if(request.username() == null || request.password() == null || request.email() == null){
            throw new BadRequestException();
        }

    }

    private void login(Context ctx){
        Gson serializer = new Gson();
        LoginRequest request = serializer.fromJson(ctx.body(), LoginRequest.class);
        try{
            checkRequest(request);
            loginHelper(ctx, request, serializer);

        }
        catch(BadRequestException bde){
            ctx.status(400);
            ExceptionMessage message = new ExceptionMessage("Error: bad request");
            String json = serializer.toJson(message);
            ctx.result(json);

        }


    }
    public void checkRequest(LoginRequest request) throws BadRequestException{
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
        catch (UnAuthorizedException ex){
            ctx.status(401);
            ExceptionMessage message = new ExceptionMessage("Error: Unauthorized");
            String json = serializer.toJson(message);
            ctx.result(json);

        }


        catch(DataAccessException ex){
            ctx.status(500);
            ExceptionMessage message = new ExceptionMessage("Error: (description of error)");
            String json = serializer.toJson(message);
            ctx.result(json);


        }


    }

    private void logout(Context ctx){
        String authToken = ctx.header("authorization");
        try{
            service.logoutService(authToken);
            ctx.status(200);

        }
        catch(UnAuthorizedException ex){
            ctx.status(401);
            Gson serializer = new Gson();
            ExceptionMessage message = new ExceptionMessage("Error: unauthorized");
            String json = serializer.toJson(message);
            ctx.result(json);

        }
        catch(DataAccessException ex){
            ctx.status(500);
            Gson serializer = new Gson();
            ExceptionMessage message = new ExceptionMessage("Error: Failed to connect to databse");
            String json = serializer.toJson(message);
            ctx.result(json);
        }


    }
    private void listGames(Context ctx){
        String authToken = ctx.header("authorization");
        Gson serializer = new Gson();
        try{
            ctx.status(200);
            Collection<GameData> listGames = service.listGamesService(authToken);
            ListGamesResult result = new ListGamesResult(listGames);
            String json = serializer.toJson(result);
            ctx.result(json);


        }
        catch(UnAuthorizedException ex){
            ctx.status(401);
            ExceptionMessage message = new ExceptionMessage("Error: unauthorized");
            String json = serializer.toJson(message);
            ctx.result(json);

        }
        catch(DataAccessException ex){
            ctx.status(500);
            ExceptionMessage message = new ExceptionMessage("Error: Failed to connect to database");
            String json = serializer.toJson(message);
            ctx.result(json);
        }


    }

    private void createGame(Context ctx){
        String authToken = ctx.header("authorization");
        Gson serializer = new Gson();
        CreateGameRequest request = serializer.fromJson(ctx.body(), CreateGameRequest.class);
        try {
            checkRequest(request);
            createGameHelper(ctx, request, serializer, authToken);
        } catch (BadRequestException bde) {
            ctx.status(400);
            ExceptionMessage message = new ExceptionMessage("Error: bad request");
            String json = serializer.toJson(message);
            ctx.result(json);
        }
    }

    public void createGameHelper(Context ctx, CreateGameRequest request, Gson serializer, String authToken){
        try{
            ctx.status(200);
            CreateGameResult result = service.createGameService(authToken, request);
            String json = serializer.toJson(result);
            ctx.result(json);


        }
        catch(UnAuthorizedException ex){
            ctx.status(401);
            ExceptionMessage message = new ExceptionMessage("Error: unauthorized");
            String json = serializer.toJson(message);
            ctx.result(json);

        }
        catch(DataAccessException ex){
            ctx.status(500);
            ExceptionMessage message = new ExceptionMessage("Error: Failed to connect to database");
            String json = serializer.toJson(message);
            ctx.result(json);

        }

    }
    public void checkRequest(CreateGameRequest request) throws BadRequestException{
        if(request.gameName() == null){
            throw new BadRequestException();
        }

    }

    private void joinGame(Context ctx){
        String authToken = ctx.header("authorization");
        Gson serializer = new Gson();
        JoinGameRequest request = serializer.fromJson(ctx.body(), JoinGameRequest.class);
        try {
            checkRequest(request);
            joinGameHelper(ctx, request, serializer, authToken);
        } catch (BadRequestException bde) {
            ctx.status(400);
            ExceptionMessage message = new ExceptionMessage("Error: bad request");
            String json = serializer.toJson(message);
            ctx.result(json);
        }

    }

    public void joinGameHelper(Context ctx, JoinGameRequest request, Gson serializer, String authToken)throws BadRequestException{
        try{
            ctx.status(200);
            service.joinGameService(authToken, request);


        }
        catch(AlreadyTakenException ate){
            ctx.status(403);
            ExceptionMessage message = new ExceptionMessage("Error: already taken");
            String json = serializer.toJson(message);
            ctx.result(json);
        }
        catch(UnAuthorizedException ex){
            ctx.status(401);
            ExceptionMessage message = new ExceptionMessage("Error: unauthorized");
            String json = serializer.toJson(message);
            ctx.result(json);

        }
        catch(BadRequestException bqe){
            throw new BadRequestException();

        }
        catch(DataAccessException ex){
            ctx.status(500);
            ExceptionMessage message = new ExceptionMessage("Error: Failed to connect to databse");
            String json = serializer.toJson(message);
            ctx.result(json);
        }

    }
    public void checkRequest(JoinGameRequest request) throws BadRequestException{
        if(request.playerColor() == null || request.gameID() == null || !checkValidColor(request)){
            throw new BadRequestException();
        }

    }
    public boolean checkValidColor(JoinGameRequest request){
        return request.playerColor().equals("WHITE") || request.playerColor().equals("BLACK");
    }



    private void clear(Context ctx){
        try{
        ctx.status(200);
        service.clearService();

        ctx.result("{}");
        }catch(DataAccessException dae){
            Gson serializer = new Gson();
            ctx.status(500);
            ExceptionMessage message = new ExceptionMessage("Error: Failed to connect to databse");
            String json = serializer.toJson(message);
            ctx.result(json);
        }

    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}

