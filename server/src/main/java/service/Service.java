package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import server.*;

import java.util.*;


public class Service {

    private UserDataAccess userData = new UserDataAccess();
    AuthDataAccess authData = new AuthDataAccess();
    private GameDataAccess gameData = new GameDataAccess();

    public  String generateToken() {
        return UUID.randomUUID().toString();
    }
    public  RegisterResult registerService(RegisterRequest request) throws AlreadyTakenException {
        if (userData.getUser(request.username()) == null) {
            userData.createUser(request);
            String authToken = generateToken();
            authData.createAuth(request.username(), authToken);
            AuthData data = authData.getAuth(authToken);
            return new RegisterResult(data.username(), data.authToken());

        } else {
            throw new AlreadyTakenException();

        }


    }


    public LoginResult loginService(LoginRequest request) throws BadRequestException, UnAuthorizedException {
        if(!userData.contains(request.username())){
            throw new UnAuthorizedException();
        }
        else if (!(userData.getUser(request.username()).password()).equals(request.password()) ){
            throw new UnAuthorizedException();
        }
        else{
            String authToken = generateToken();
            authData.createAuth(request.username(), authToken);
            AuthData data = authData.getAuth(authToken);
            return new LoginResult(data.username(), data.authToken());


        }
    }
    public void logoutService(String authToken)throws UnAuthorizedException{
        if(!authData.contains(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            authData.deleteAuth(authToken);



        }


    }
    public Collection<GameData> listGamesService(String authToken)throws UnAuthorizedException{
        //method getAuth, function below should just be included in authDataAccess
        if(!authData.contains(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            return gameData.listGames();


        }
    }
    public void clearService(){
        gameData.clear();
        userData.clear();
        authData.clear();

    }
    public CreateGameResult createGameService(String authToken, CreateGameRequest request)throws UnAuthorizedException{
        if(!authData.contains(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            Integer gameID = gameData.createGame(request.gameName());
            return new CreateGameResult(gameID);






        }
    }
    public void joinGameService(String authToken, JoinGameRequest request)throws UnAuthorizedException, BadRequestException, AlreadyTakenException {
        if (!authData.contains(authToken)) {
            throw new UnAuthorizedException();

        } else {
            if (gameData.getGame(request.gameID()) == null) {
                throw new BadRequestException();
            }
            else if(request.playerColor().equals("WHITE") && gameData.getGame(request.gameID()).whiteUsername() != null) {
                throw new AlreadyTakenException();

            }
            else if(request.playerColor().equals("BLACK") && gameData.getGame(request.gameID()).blackUsername() != null){
                throw new AlreadyTakenException();
            }
            else{
                String username = authData.getAuth(authToken).username();
                gameData.updateGame(request, username);

            }

        }
    }

}






