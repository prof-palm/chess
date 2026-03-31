package client;

import Exceptions.ResponseException;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.ListGamesResult;
import server.BadRequestException;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }
    @BeforeEach
    public void clearDatabase() throws Exception{
        facade.clear();
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void registerPositiveTest() throws Exception {
        var authData = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    void registerNegativeTest()  {
        assertThrows(ResponseException.class, () -> facade.register(new RegisterRequest("player1", "password", null)) );
    }


    @Test
    void loginPositiveTest()throws Exception {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        var authData = facade.login(new LoginRequest("player1", "password"));
        assertTrue(authData.authToken().length()>10);
    }
    @Test
    void loginNegativeTest(){
        assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("water", "fire")));
    }
    @Test
    void logoutPositiveTest() throws Exception{
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        var authData = facade.login(new LoginRequest("player1", "password"));
        facade.logout(authData.authToken());
        assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest("WHITE", 1234), authData.authToken()));
    }
    @Test
    void logoutNegativeTest() throws Exception{
        var authData = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        facade.logout(authData.authToken());
        assertThrows(ResponseException.class, () -> facade.logout(authData.authToken()));
    }

    @Test
    void listGamePositiveTest()throws Exception {
        var authData = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        facade.createGame(new CreateGameRequest("fire"), authData.authToken());
        ListGamesResult list = facade.listGame(authData.authToken());
        assertFalse(list.games().isEmpty());

    }
    @Test
    void listGameNegativeTest()throws Exception{
        var authData = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        facade.createGame(new CreateGameRequest("fire"), authData.authToken());
        assertThrows(ResponseException.class, () -> facade.listGame("hello"));
    }

    @Test
    void createGamePositiveTest()throws Exception {
        var authData = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        var gameResult = facade.createGame(new CreateGameRequest("water"), authData.authToken());
        Assertions.assertNotNull(gameResult);


    }
    @Test
    void createGameNegativeTest()throws Exception {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        assertThrows(ResponseException.class, () -> facade.createGame(new CreateGameRequest("fire"), "hello"));
    }

    @Test
    void joinGamePositiveTest()throws Exception{
        var authData1 = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        var authData2 = facade.register(new RegisterRequest("player2", "password", "p2@email.com"));
        var gameData = facade.createGame(new CreateGameRequest("water"), authData1.authToken());
        facade.joinGame(new JoinGameRequest("WHITE", gameData.gameID()), authData1.authToken());
        assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest("WHITE", gameData.gameID()), authData2.authToken()));

    }
    @Test
    void joinGameNegativeTest()throws Exception{
        var authData1 = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        var gameData = facade.createGame(new CreateGameRequest("water"), authData1.authToken());
        assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameRequest("WHITE", gameData.gameID()), "hello"));

    }

    @Test
    void clear()throws Exception {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        facade.clear();
        assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("player1", "password")));


    }

}
