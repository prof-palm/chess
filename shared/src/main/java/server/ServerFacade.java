package server;

import exceptions.ResponseException;
import com.google.gson.Gson;
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

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;

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

    public void joinGame(JoinGameRequest request, String authToken)throws ResponseException{
        var httpRequest = buildRequest("PUT", "/game", request, authToken);
        var response = sendRequest(httpRequest);
        handleResponse(response, null);
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

}




