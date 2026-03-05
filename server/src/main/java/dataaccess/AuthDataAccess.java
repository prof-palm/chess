package dataaccess;
import java.util.Objects;
import java.util.UUID;
import model.AuthData;
import model.UserData;
import server.RegisterRequest;

import java.util.HashMap;

public class AuthDataAccess {

    private HashMap<String, AuthData> authData;

    public AuthDataAccess() {
        authData = new HashMap<>();
    }
    public  String generateToken() {
        return UUID.randomUUID().toString();
    }

    public HashMap<String, AuthData> getAuthDataBase(){
        return authData;
    }

    public AuthData getAuthData(String username) {
        return authData.get(username);
    }


    public void createAuth(HashMap<String, AuthData> authData, String username) {
        String authToken = generateToken();
        authData.put(authToken, new AuthData(authToken, username));


    }



}