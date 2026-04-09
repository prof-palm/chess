package websocket;

import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

//need to somehow get teh game ID of a given game, pass in the game when this is called?
public class ConnectionManager {
    public final ConcurrentHashMap<int, Session> connections = new ConcurrentHashMap<>();

    public void add(Session session) {
        connections.put(GameData.gameID(), session);
    }

    public void remove(Session session) {
        connections.remove(GameData.gameID());
    }

    public void broadcast(Session excludeSession, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
