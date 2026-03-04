package dataaccess;
import java.util.UUID;
import model.AuthData;
import model.UserData;
import server.RegisterRequest;

import java.util.HashMap;

public class AuthDataAccess {

    private static HashMap<String, AuthData> authData;

    public AuthDataAccess() {
        authData = new HashMap<>();
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static HashMap<String, AuthData> getAuthDataBase(){
        return authData;
    }
    public static AuthData getAuthData(String username) {
        return authData.get(username);
    }


    public static void createAuth(HashMap<String, AuthData> authData, RegisterRequest request) {
        String authToken = generateToken();
        authData.put(request.username(), new AuthData(authToken, request.username()));


    }

}