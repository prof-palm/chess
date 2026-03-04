package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import server.RegisterRequest;
import server.RegisterResult;

import static dataaccess.AuthDataAccess.*;
import static dataaccess.UserDataAccess.*;

public class Service {
    public RegisterResult registerService(RegisterRequest request) {
        if (UserDataAccess.getUserData(request.username()) == null) {
            createUser(UserDataAccess.getUserDataBase(), request);
            createAuth(AuthDataAccess.getAuthDataBase(), request);
            return new RegisterResult();

        } else {
            throw AlreadyTakenException;

        }
    return null;

    }

}
    /*public LoginResult login(LoginRequest loginRequest) {

    }
    public void logout(LogoutRequest logoutRequest) {

    }
     */

