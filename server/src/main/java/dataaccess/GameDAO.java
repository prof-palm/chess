package dataaccess;

import chess.ChessGame;
import model.GameData;
import requests.JoinGameRequest;

import java.util.Collection;

public interface GameDAO {
    public void clear() throws DataAccessException;

    public Collection<GameData> listGames() throws DataAccessException;

    public GameData getGame(Integer gameID) throws DataAccessException;

    public Integer createGame(String gameName) throws DataAccessException;

    public void updateGame(GameData data) throws DataAccessException;

}
