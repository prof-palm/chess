package client;

import chess.ChessMove;
import com.google.gson.Gson;
import exceptions.ResponseException;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.CreateGameResult;
import results.ListGamesResult;
import results.LoginResult;
import results.RegisterResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import jakarta.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URISyntaxException;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private Session session = null;
    private MessageHandler messageHandler;



    public ServerFacade(String url){
        serverUrl = url;
    }

    public void connectToServer() throws ResponseException {
        String url = serverUrl;
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new jakarta.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    messageHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }



    public RegisterResult register(RegisterRequest request) throws ResponseException{
        var httpRequest = buildRequest("POST", "/user", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request)throws ResponseException{
        var httpRequest = buildRequest("POST", "/session", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, LoginResult.class);
    }

    public void logout(String authToken)throws ResponseException{
        var httpRequest = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(httpRequest);
        handleResponse(response, null);
    }

    public ListGamesResult listGame(String authToken)throws ResponseException{
        var httpRequest = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(httpRequest);
        return handleResponse(response, ListGamesResult.class);
    }
    public CreateGameResult createGame(CreateGameRequest request, String authToken)throws ResponseException{
        var httpRequest = buildRequest("POST", "/game", request, authToken);
        var response = sendRequest(httpRequest);
        return handleResponse(response, CreateGameResult.class);
    }
    //need to modify this so that it sends a websocket request
    public void joinGame(JoinGameRequest request, String authToken)throws ResponseException{
        var httpRequest = buildRequest("PUT", "/game", request, authToken);
        var response = sendRequest(httpRequest);
        handleResponse(response, null);
        connectToServer();

    }

    public void clear()throws ResponseException{
        var request = buildRequest("DELETE", "/db", null);
        sendRequest(request);
    }
    private HttpRequest buildRequest(String method, String path, Object body){
        return buildRequest(method, path, body, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if(authToken != null){
            request.setHeader("authorization", authToken);
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }
    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body, status);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


    public void connectToGame(String authToken, int gameID)throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT , authToken, gameID);
        send(command);
    }

    public void leave(String authToken, int gameID)throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE , authToken, gameID);
        send(command);
    }
    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException{
        UserGameCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE , authToken, gameID, move);
        send(command);
    }

    public void resign(String authToken, int gameID) throws IOException{
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN , authToken, gameID);
        send(command);
    }


    public void send(UserGameCommand command) throws IOException {
        Gson serializer = new Gson();
        session.getBasicRemote().sendText(serializer.toJson(command));
    }


    // This method must be overridden, but we don't have to do anything with it
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}





