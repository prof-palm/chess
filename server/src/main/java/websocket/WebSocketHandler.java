package websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.Service;
import service.UnAuthorizedException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Service service;

    public WebSocketHandler(Service service){
        this.service = service;
    }


    //need to add methods for each of these
    //have an additional if stat
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        Session session = ctx.session;

        try {
            Gson serializer = new Gson();
            UserGameCommand command = serializer.fromJson(
                    ctx.message(), UserGameCommand.class);
            int gameId = command.getGameID();
            String username = service.getAuthDAO().getAuth(command.getAuthToken()).username();
            saveSession(gameId, session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command, ctx, gameId);
                case MAKE_MOVE -> makeMove(session, username, command, ctx, gameId);
                //when calling makeMove, esure the command is deserialized once more.
                case LEAVE -> leaveGame(session, username, command, ctx);
                case RESIGN -> resign(session, username, command, ctx);
            }
        } catch (UnAuthorizedException ex) {
            sendMessage(session, gameId, new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session, gameId, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }
    public void saveSession(int gameID, Session session){
        connections.add(gameID, session);

    }
    //this should send a server message back, how do I do that with proper
    //possible nullptr exception, should be handled
    //command type might not be necessary
    public void connect(Session session, String username, UserGameCommand command, WsMessageContext ctx, int gameID) throws DataAccessException, IOException {
        GameData data = service.getGameDAO().getGame(gameID);
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "loading game...", data.game());
        ctx.send(message);
        if(data.blackUsername() != null && data.blackUsername().equals(username)){
            String broadcastMessage = String.format("%s has joined the game as BLACK", username);
            connections.broadcast(session, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage, data.game()), gameID);
        }
        else if (data.whiteUsername() != null && data.whiteUsername().equals(username)){
            String broadcastMessage = String.format("%s has joined the game as WHITE", username);
            connections.broadcast(session, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage, data.game()), gameID);
        }
        else{
            String broadcastMessage = String.format("%s has joined the game as OBSERVER", username);
            connections.broadcast(session, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage, data.game()), gameID);
        }
    }

    public void makeMove(Session session, String username, MakeMoveCommand command, WsMessageContext ctx, int gameID){
        GameData data = service.getGameDAO().getGame(gameID);
        ChessGame game = data.game();
        try {
            game.makeMove(command.getMove());
        }
        catch(InvalidMoveException ie){
            ctx.send(new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Invalid move", null));
        }
        GameData updatedGame = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), data.game());
        service.getGameDAO().updateGame(updatedGame);
        String message = String.format("%s moved from %s to %s", username, command.getMove().getStartPosition(), command.getMove().getEndPosition());
        ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, message, game);
        ctx.send(loadMessage);
        connections.broadcast(session, loadMessage, gameID);














    }







    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {

    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {

    }
}
