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


    public HashMap<String, AuthData> getAuthDataBase(){
        return authData;
    }

    public AuthData getAuthData(String authToken) {
        return authData.get(authToken);
    }


    public void createAuth(HashMap<String, AuthData> authData, String username, String authToken) {
        authData.put(authToken, new AuthData(authToken, username));


    }
    public void clear(){
        authData.clear();
    }



}