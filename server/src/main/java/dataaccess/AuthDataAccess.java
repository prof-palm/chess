package dataaccess;
import model.AuthData;

import java.util.HashMap;

public class AuthDataAccess implements AuthDAO{

    private HashMap<String, AuthData> authData;

    public AuthDataAccess() {
        authData = new HashMap<>();
    }


    public  boolean contains(String authToken){
        return authData.containsKey(authToken);
    }

    public AuthData getAuth(String authToken) {
        return authData.get(authToken);
    }


    public void createAuth(String username, String authToken) {
        authData.put(authToken, new AuthData(authToken, username));


    }
    public void clear(){
        authData.clear();
    }

    public void deleteAuth(String authToken){
        authData.remove(authToken);


    }



}