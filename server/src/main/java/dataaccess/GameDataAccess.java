
package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import server.JoinGameRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GameDataAccess {

    private HashMap<Integer, GameData> gameData;

    public GameDataAccess() {
        gameData = new HashMap<>();
    }
    public HashMap<Integer, GameData> getGameDataBase(){
        return gameData;
    }
    public void clear(){
        gameData.clear();
    }
    public Collection<GameData> values(){
        return gameData.values();
    }

    public GameData getGame(Integer gameID){
        return gameData.get(gameID);

    }
    public Integer createGame(String gameName){
        Integer gameID = randomID();
        gameData.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }
    public Integer randomID(){
        return (int)(Math.random() * 9000) + 1000;
    }

    public void updateGame(JoinGameRequest request, String username){
        GameData currentGame = getGame(request.gameID());
        if(request.playerColor().equals("WHITE")){
            GameData updatedGame = new GameData(request.gameID(), username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game());
            gameData.remove(request.gameID());
            gameData.put(updatedGame.gameID(), updatedGame);
        }
        else {
            GameData updatedGame = new GameData(request.gameID(), currentGame.whiteUsername(), username, currentGame.gameName(), currentGame.game());
            gameData.remove(request.gameID());
            gameData.put(updatedGame.gameID(), updatedGame);
        }






    }





}