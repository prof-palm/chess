package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.*;

class ServiceTest {

    private Service service;


    @BeforeEach
    void setup(){
        service = new Service();
    }


    @Test
    @DisplayName("Successful username pass")
    void UsernameSuccessfullyStored() {
        RegisterRequest request = new RegisterRequest("Bobby", "water", "hi@gmail.com");
        try{
        RegisterResult result = service.registerService(request);
            Assertions.assertEquals("Bobby", result.username());
    }
        catch(AlreadyTakenException ate){
            System.out.print("Username is taken");

        }
    }
    @Test
    void DuplicateEntry(){
        RegisterRequest request = new RegisterRequest("Bobby", "water", "hi@gmail.com");

        try{
            service.registerService(request);
            Assertions.assertThrows(AlreadyTakenException.class, () ->
                    service.registerService(request));
        }
        catch(AlreadyTakenException ate){
            System.out.print("Username is taken");

        }
    }




    @Test
    void loginServiceValidAuthToken() {
        LoginRequest request = new LoginRequest("Bob", "water");
        try{
            LoginResult result = service.loginService(request);
            Assertions.assertNotNull(result.authToken());
        }
        catch(Exception ex){
            System.out.print("authToken not created");
        }


    }

    @Test
    void loginServiceBadRequest(){
        LoginRequest request = new LoginRequest(null, "water");
        Assertions.assertThrows(UnAuthorizedException.class, () ->
            service.loginService(request));

    }

    @Test
    void logoutServiceSuccess() throws UnAuthorizedException, AlreadyTakenException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        service.logoutService(result.authToken());
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.listGamesService(result.authToken()));

    }

    @Test
    void logoutServiceDoubleLogout() throws AlreadyTakenException, UnAuthorizedException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        service.logoutService(result.authToken());
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.logoutService(result.authToken()));


    }

    @Test
    void listGamesService() throws AlreadyTakenException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);

    }

    @Test
    void clearServiceSuccess() {




    }

    @Test
    void createGameServiceSuccess() throws AlreadyTakenException, UnAuthorizedException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        CreateGameRequest gameRequest = new CreateGameRequest("Puss_in_Boots");
        CreateGameResult gameResult = service.createGameService(result.authToken(), gameRequest);
        Assertions.assertNotNull(gameResult);




    }
    @Test
    void createGameServiceFail() throws AlreadyTakenException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        service.registerService(request);
        CreateGameRequest gameRequest = new CreateGameRequest("game");
        Assertions.assertThrows(UnAuthorizedException.class, () -> service.createGameService("hello", gameRequest));


    }

    @Test
    void joinGameService() {


    }
}