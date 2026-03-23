package service;

import dataaccess.*;

import model.AuthData;
import model.GameData;
import org.mindrot.jbcrypt.BCrypt;
import server.*;

import java.util.*;


public class Service {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public Service(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }
    public Service(){
        authDAO = new AuthDataAccessSQL();
        userDAO = new UserDataAccessSQL();
        gameDAO = new GameDataAccessSQL();

    }

    public  String generateToken() {
        return UUID.randomUUID().toString();
    }


    public  RegisterResult registerService(RegisterRequest request) throws AlreadyTakenException, DataAccessException {
        if (userDAO.getUser(request.username()) == null) {
            userDAO.createUser(request);
            String authToken = generateToken();
            authDAO.createAuth(request.username(), authToken);
            AuthData data = authDAO.getAuth(authToken);
            return new RegisterResult(data.username(), data.authToken());

        } else {
            throw new AlreadyTakenException();

        }


    }


    public LoginResult loginService(LoginRequest request) throws BadRequestException, UnAuthorizedException, DataAccessException {
        if(!userDAO.contains(request.username())){
            throw new UnAuthorizedException();
        }
        else if (!verifyUser(request.password(), userDAO.getUser(request.username()).password()) ){
            throw new UnAuthorizedException();
        }
        else{
            String authToken = generateToken();
            authDAO.createAuth(request.username(), authToken);
            AuthData data = authDAO.getAuth(authToken);
            return new LoginResult(data.username(), data.authToken());


        }
    }
    boolean verifyUser(String clearPassword, String hashedPassword ) {

        return BCrypt.checkpw(clearPassword, hashedPassword);
    }

    public void logoutService(String authToken)throws UnAuthorizedException, DataAccessException{
        if(!authDAO.contains(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            authDAO.deleteAuth(authToken);



        }


    }
    public Collection<GameData> listGamesService(String authToken)throws UnAuthorizedException, DataAccessException{
        //method getAuth, function below should just be included in authDataAccess
        if(!authDAO.contains(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            return gameDAO.listGames();


        }
    }
    public void clearService()throws DataAccessException{
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();

    }
    public CreateGameResult createGameService(String authToken, CreateGameRequest request)throws UnAuthorizedException, DataAccessException{
        if(!authDAO.contains(authToken)){
            throw new UnAuthorizedException();

        }
        else{
            Integer gameID = gameDAO.createGame(request.gameName());
            return new CreateGameResult(gameID);






        }
    }
    public void joinGameService(String authToken, JoinGameRequest request)throws UnAuthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        if (!authDAO.contains(authToken)) {
            throw new UnAuthorizedException();

        } else {
            if (gameDAO.getGame(request.gameID()) == null) {
                throw new BadRequestException();
            }
            else if(request.playerColor().equals("WHITE") && gameDAO.getGame(request.gameID()).whiteUsername() != null) {
                throw new AlreadyTakenException();

            }
            else if(request.playerColor().equals("BLACK") && gameDAO.getGame(request.gameID()).blackUsername() != null){
                throw new AlreadyTakenException();
            }
            else{
                String username = authDAO.getAuth(authToken).username();
                gameDAO.updateGame(request, username);

            }

        }
    }

}






