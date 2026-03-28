package dataaccess;

import model.UserData;
import requests.RegisterRequest;

import java.util.HashMap;

public class UserDataAccess implements UserDAO {

    private HashMap<String, UserData> userData;

    public UserDataAccess() {
        userData = new HashMap<>();
    }


    public  boolean contains(String username){
        return userData.containsKey(username);
    }


    public  UserData getUser(String username) {
        return userData.get(username);
    }


     public void createUser(RegisterRequest request) {
        userData.put(request.username(), new UserData(request.username(), request.password(), request.email()));
    }
    public void clear(){
        userData.clear();

    }

}









