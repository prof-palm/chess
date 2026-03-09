package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import server.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class Service {

    private UserDataAccess userData = new UserDataAccess();
    AuthDataAccess authData = new AuthDataAccess();
    private GameDataAccess gameData = new GameDataAccess();

    public  String generateToken() {
        return UUID.randomUUID().toString();
    }
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
    public ArrayList<GameData> listGamesService(String authToken)throws UnAuthorizedException{
        //method getAuth, function below should just be included in authDataAccess
        if(!authData.getAuthDataBase().containsKey(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            ArrayList<GameData> result = new ArrayList<>();
            for(GameData value : (gameData.getGameDataBase()).values()){
                result.add(value);
            }
            return result;


        }
    }
    public void clearService(){
        gameData.clear();
        userData.clear();
        authData.clear();

    }
    public CreateGameResult createGameService(String authToken, CreateGameRequest request)throws UnAuthorizedException{
        if(!authData.getAuthDataBase().containsKey(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            Integer gameID = gameData.createGame(request.gameName());
            return new CreateGameResult(gameID);






        }
    }
    public void joinGameService(String authToken, JoinGameRequest request)throws UnAuthorizedException, BadRequestException, AlreadyTakenException {
        if (!authData.getAuthDataBase().containsKey(authToken)) {
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
                String username = authData.getAuthData(authToken).username();
                gameData.updateGame(request, username);

            }

        }
    }

}






