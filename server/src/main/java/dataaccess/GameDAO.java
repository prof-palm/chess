package dataaccess;

import model.GameData;
import server.JoinGameRequest;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
    public void clear() throws DataAccessException;

    public Collection<GameData> listGames() throws DataAccessException;

    public GameData getGame(Integer gameID) throws DataAccessException;

    public Integer createGame(String gameName) throws DataAccessException;

    public void updateGame(JoinGameRequest request, String username) throws DataAccessException;
}
