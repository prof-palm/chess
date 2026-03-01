package service;

public class Service {
    public RegisterResult register(RegisterRequest registerRequest) {
        getUser(registerRequest.username());


    }
    public LoginResult login(LoginRequest loginRequest) {

    }
    public void logout(LogoutRequest logoutRequest) {

    }
}
