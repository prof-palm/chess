package dataaccess;

import model.GameData;
import server.JoinGameRequest;

import java.util.Collection;

public interface GameDAO {
    public void clear();

    public Collection<GameData> values();

    public GameData getGame(Integer gameID);

    public Integer createGame(String gameName);

    public void updateGame(JoinGameRequest request, String username);
}
