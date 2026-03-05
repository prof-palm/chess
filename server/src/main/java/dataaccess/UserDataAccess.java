package dataaccess;

import model.UserData;
import server.RegisterRequest;

import java.util.HashMap;
import java.util.Objects;

public class UserDataAccess {

    private HashMap<String, UserData> userData;

    public UserDataAccess() {
        userData = new HashMap<>();
    }


    public  HashMap<String, UserData> getUserDataBase(){
        return userData;
    }


    public  UserData getUserData(String username) {
        return userData.get(username);
    }


    public void createUser(HashMap<String, UserData> userData, RegisterRequest request) {
        userData.put(request.username(), new UserData(request.username(), request.password(), request.email()));
    }



    //    public static void removeUserData(HashMap<String, List<String>> userData, LogoutRequest request){
//        userData.remove(request.username);
//    }

}









