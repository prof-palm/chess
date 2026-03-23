package service;

import dataaccess.AuthDataAccess;
import dataaccess.AuthDataAccessSQL;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.*;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;



class ServiceTest {

    private Service service;


    @BeforeEach
    void setup(){
        service = new Service();
    }
    @AfterEach
    void takedown() throws DataAccessException {
        service.clearService();
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

        } catch (DataAccessException e) {
            System.out.print("Error Accessing the DataBase");
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

        } catch (DataAccessException e) {
            System.out.print("Error Accessing the DataBase");
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
    void logoutServiceSuccess() throws UnAuthorizedException, AlreadyTakenException, DataAccessException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        service.logoutService(result.authToken());
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.listGamesService(result.authToken()));

    }

    @Test
    void logoutServiceDoubleLogout() throws AlreadyTakenException, UnAuthorizedException, DataAccessException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        service.logoutService(result.authToken());
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.logoutService(result.authToken()));


    }

    @Test
    void listGamesServiceSuccess() throws AlreadyTakenException, UnAuthorizedException, DataAccessException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        Collection<GameData> listOfGames = service.listGamesService(result.authToken());
        Assertions.assertEquals(0, listOfGames.size());


    }
    @Test
    void listGamesServiceFail() throws AlreadyTakenException, DataAccessException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        service.registerService(request);
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.listGamesService("result.authToken()"));


    }


//not clearing properly due to invalid salt
    @Test
    void clearServiceSuccess() throws AlreadyTakenException, BadRequestException, UnAuthorizedException, DataAccessException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        service.registerService(request);
        service.loginService(new LoginRequest(request.username(), request.password()));
        service.clearService();
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.loginService(new LoginRequest(request.username(), request.password())));





    }

    @Test
    void createGameServiceSuccess() throws AlreadyTakenException, UnAuthorizedException, DataAccessException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        CreateGameRequest gameRequest = new CreateGameRequest("Puss_in_Boots");
        CreateGameResult gameResult = service.createGameService(result.authToken(), gameRequest);
        Assertions.assertNotNull(gameResult);




    }
    @Test
    void createGameServiceFail() throws AlreadyTakenException, DataAccessException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        service.registerService(request);
        CreateGameRequest gameRequest = new CreateGameRequest("game");
        Assertions.assertThrows(UnAuthorizedException.class, () -> service.createGameService("hello", gameRequest));


    }
//joinGameService is not right by itself.
    @Test
    void joinGameServiceSuccess() throws AlreadyTakenException, UnAuthorizedException, BadRequestException, DataAccessException {
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
    void joinGameServiceFail() throws AlreadyTakenException, UnAuthorizedException, DataAccessException {
        RegisterRequest request = new RegisterRequest("water", "water", "water");
        RegisterResult result = service.registerService(request);
        CreateGameRequest gameRequest = new CreateGameRequest("game");
        CreateGameResult gameResult = service.createGameService(result.authToken(), gameRequest);
        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", gameResult.gameID());
        Assertions.assertThrows(UnAuthorizedException.class, () ->
                service.joinGameService("result.authToken()", joinRequest));






    }

}