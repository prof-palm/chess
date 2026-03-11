package dataaccess;

import model.UserData;
import server.RegisterRequest;

import java.util.HashMap;
import java.util.Objects;

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



    //    public static void removeUserData(HashMap<String, List<String>> userData, LogoutRequest request){
//        userData.remove(request.username);
//    }

}









