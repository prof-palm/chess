package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import server.*;

import java.util.UUID;


public class Service {

    private UserDataAccess userData = new UserDataAccess();
    private AuthDataAccess authData = new AuthDataAccess();
    private GameDataAccess gameData = new GameDataAccess();


    public  RegisterResult registerService(RegisterRequest request) throws AlreadyTakenException {
        if (userData.getUserData(request.username()) == null) {
            userData.createUser(userData.getUserDataBase(), request);
            String authToken = generateToken();
            authData.createAuth(authData.getAuthDataBase(), request.username(), authToken);
            AuthData data = authData.getAuthData(authToken);
            return new RegisterResult(data.username(), data.authToken());

        } else {
            throw new AlreadyTakenException();

        }


    }
    public  String generateToken() {
        return UUID.randomUUID().toString();
    }
    //throws User not Found exception
    //throws Unauthorized exception
    public LoginResult loginService(LoginRequest request) throws BadRequestException, UnAuthorizedException {
        if(!userData.getUserDataBase().containsKey(request.username())){
            throw new UnAuthorizedException();
        }
        else if (!(userData.getUserData(request.username()).password()).equals(request.password()) ){
            throw new UnAuthorizedException();
        }
        else{
            String authToken = generateToken();
            authData.createAuth(authData.getAuthDataBase(), request.username(), authToken);
            AuthData data = authData.getAuthData(authToken);
            return new LoginResult(data.username(), data.authToken());


        }
    }
    public void logoutService(String authToken)throws UnAuthorizedException{
        if(!authData.getAuthDataBase().containsKey(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            authData.getAuthDataBase().remove(authToken);



        }


    }




}
    /*public LoginResult login(LoginRequest loginRequest) {

    }
    public void logout(LogoutRequest logoutRequest) {

    }
     */

