package dataaccess;

import model.UserData;
import server.RegisterRequest;

import java.util.HashMap;

public class UserDataAccess {

    private static HashMap<String, UserData> userData;

    public UserDataAccess() {
        userData = new HashMap<>();
    }


    public static HashMap<String, UserData> getUserDataBase(){
        return userData;
    }


    public static UserData getUserData(String username) {
        return userData.get(username);
    }


    public static void createUser(HashMap<String, UserData> userData, RegisterRequest request) {
        userData.put(request.username(), new UserData(request.username(), request.password(), request.email()));
    }

//    public static void removeUserData(HashMap<String, List<String>> userData, LogoutRequest request){
//        userData.remove(request.username);
//    }

}









