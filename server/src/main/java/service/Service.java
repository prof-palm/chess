package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import server.*;


public class Service {

    private UserDataAccess userData = new UserDataAccess();
    private AuthDataAccess authData = new AuthDataAccess();
    private GameDataAccess gameData = new GameDataAccess();


    public  RegisterResult registerService(RegisterRequest request) throws AlreadyTakenException {
        if (userData.getUserData(request.username()) == null) {
            userData.createUser(userData.getUserDataBase(), request);
            authData.createAuth(authData.getAuthDataBase(), request.username());
            AuthData data = authData.getAuthData(request.username());
            return new RegisterResult(data.username(), data.authToken());

        } else {
            throw new AlreadyTakenException();

        }


    }
    //throws User not Found exception
    //throws Unauthorized exception
    public LoginResult loginService(LoginRequest request) throws BadRequestException, InvalidLoginException {
        if(!userData.getUserDataBase().containsKey(request.username())){
            throw new InvalidLoginException();
        }
        else if (!(userData.getUserData(request.username()).password()).equals(request.password()) ){
            throw new InvalidLoginException();
        }
        else{
            authData.createAuth(authData.getAuthDataBase(), request.username());
            AuthData data = authData.getAuthData(request.username());
            return new LoginResult(data.username(), data.authToken());


        }
    }




}
    /*public LoginResult login(LoginRequest loginRequest) {

    }
    public void logout(LogoutRequest logoutRequest) {

    }
     */

