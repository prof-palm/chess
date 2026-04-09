package websocket;

import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;


//need to somehow get the game ID of a given game
public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();


    public void add(Integer gameID, Session session) {
        if(!connections.containsKey(gameID)){
            connections.put(gameID, new ArrayList<>());
        }
            connections.get(gameID).add(session);




    }

    public void remove(Integer gameID, Session session) {
        connections.get(gameID).remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage message, Integer gameID) throws IOException {
        String msg = message.toString();
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
