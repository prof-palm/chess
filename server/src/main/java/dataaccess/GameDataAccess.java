
package dataaccess;

import chess.ChessGame;
import model.GameData;
import requests.JoinGameRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GameDataAccess implements GameDAO {

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


    public Collection<GameData> listGames(){
        ArrayList<GameData> result = new ArrayList<>();
        for(GameData value : (gameData.values())){
            result.add(value);
        }
        return result;

    }


    public void updateGame(GameData data){
        gameData.remove(data.gameID());
        gameData.put(data.gameID(), data);
    }




}