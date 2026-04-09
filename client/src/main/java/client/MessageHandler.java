package client;

import websocket.messages.ServerMessage;

public interface MessageHandler {
    void notify(ServerMessage notification);
}
