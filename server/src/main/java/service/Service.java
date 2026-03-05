package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import server.RegisterRequest;
import server.RegisterResult;

import  dataaccess.AuthDataAccess.*;
import  dataaccess.UserDataAccess.*;


public class Service {

    private UserDataAccess userData = new UserDataAccess();
    private AuthDataAccess authData = new AuthDataAccess();
    private GameDataAccess gameData = new GameDataAccess();


    public  RegisterResult registerService(RegisterRequest request) throws AlreadyTakenException {
        if (userData.getUserData(request.username()) == null) {
            userData.createUser(userData.getUserDataBase(), request);
            authData.createAuth(authData.getAuthDataBase(), request);
            AuthData data = authData.getAuthData(request.username());
            return new RegisterResult(data.username(), data.authToken());

        } else {
            throw new AlreadyTakenException();

        }

    }

}
    /*public LoginResult login(LoginRequest loginRequest) {

    }
    public void logout(LogoutRequest logoutRequest) {

    }
     */

