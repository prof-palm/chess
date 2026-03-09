package service;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.*;

import java.util.ArrayList;

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
    void listGamesServiceSuccess() throws AlreadyTakenException, UnAuthorizedException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        ArrayList<GameData> listOfGames = service.listGamesService(result.authToken());
        Assertions.assertEquals(0, listOfGames.size());


    }
    @Test
    void listGamesServiceFail() throws AlreadyTakenException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        service.registerService(request);
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.listGamesService("result.authToken()"));


    }



    @Test
    void clearServiceSuccess() throws AlreadyTakenException, BadRequestException, UnAuthorizedException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        service.registerService(request);
        service.loginService(new LoginRequest(request.username(), request.password()));
        service.clearService();
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.loginService(new LoginRequest(request.username(), request.password())));





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
    void joinGameServiceSuccess() throws AlreadyTakenException, UnAuthorizedException, BadRequestException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        RegisterRequest request1 = new RegisterRequest("fire", "water", "water");
        RegisterResult result1 = service.registerService(request1);
        CreateGameRequest gameRequest = new CreateGameRequest("game");
        CreateGameResult gameResult = service.createGameService(result.authToken(), gameRequest);
        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", gameResult.gameID());
        service.joinGameService(result.authToken(), joinRequest);
        JoinGameRequest joinRequest1 = new JoinGameRequest("WHITE", gameResult.gameID());
        Assertions.assertThrows(AlreadyTakenException.class, () ->
                service.joinGameService(result1.authToken(),joinRequest1 ));





    }
    @Test
    void joinGameServiceFail() throws AlreadyTakenException, UnAuthorizedException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        CreateGameRequest gameRequest = new CreateGameRequest("game");
        CreateGameResult gameResult = service.createGameService(result.authToken(), gameRequest);
        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", gameResult.gameID());
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.joinGameService("result.authToken()", joinRequest));






    }

}