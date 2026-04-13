package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.Service;
import service.UnAuthorizedException;
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
    //what do I do with unauthorized exceptions? Nothing calls it.I think I can handle it earlier
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
                case CONNECT -> connect(session, username, command, ctx, gameId);
                case MAKE_MOVE -> makeMove(session, username, command, ctx, gameId);
                case LEAVE -> leaveGame(session, username, command, ctx, gameId);
                case RESIGN -> resign(session, username, command, ctx, gameId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage(), null);
            ctx.send(errorMessage);
        }
    }
    public void saveSession(int gameID, Session session){
        connections.add(gameID, session);

    }


    public void connect(Session session, String username, UserGameCommand command, WsMessageContext ctx, int gameID) {
        try{
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

    }catch(DataAccessException | IOException dae){
            ctx.send(new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + dae.getMessage(), null));
        }

    }

    public void makeMove(Session session, String username, UserGameCommand command, WsMessageContext ctx, int gameID){
        try {
        GameData data = service.getGameDAO().getGame(gameID);
        ChessGame game = data.game();
        game.makeMove(command.getMove());
        GameData updatedGame = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), data.game());
        service.getGameDAO().updateGame(updatedGame);
        ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "loading game...", game);
        ctx.send(loadMessage);
        connections.broadcast(session, loadMessage, gameID);
        String message = String.format("%s moved from %s to %s", username, command.getMove().getStartPosition(), command.getMove().getEndPosition());
        ServerMessage moveNotification =  new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);
        connections.broadcast(session, moveNotification, gameID);
        if(game.isInCheck(ChessGame.TeamColor.WHITE, game.getBoard())){
            String checkMessage = String.format("%s is in check", data.whiteUsername());
            ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null);
            ctx.send(checkNotification);
            connections.broadcast(session, checkNotification, gameID);
        }
        else if(game.isInCheck(ChessGame.TeamColor.BLACK, game.getBoard())){
                String checkMessage = String.format("%s is in check", data.blackUsername());
                ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null);
                ctx.send(checkNotification);
                connections.broadcast(session, checkNotification, gameID);
            }
        else if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
                String checkMessage = String.format("%s is in CheckMate, %s WINS!", data.whiteUsername(), data.blackUsername());
                ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null);
                ctx.send(checkNotification);
                connections.broadcast(session, checkNotification, gameID);
            }
        else if(game.isInCheckmate(ChessGame.TeamColor.BLACK)){
            String checkMessage = String.format("%s is in CheckMate, %s WINS!", data.blackUsername(), data.whiteUsername());
            ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null);
            ctx.send(checkNotification);
            connections.broadcast(session, checkNotification, gameID);
        }
        else if(game.isInStalemate(ChessGame.TeamColor.BLACK)){
            String checkMessage = String.format("%s is in stalemate, DRAW!", data.blackUsername());
            ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null);
            ctx.send(checkNotification);
            connections.broadcast(session, checkNotification, gameID);
        }
        else if(game.isInStalemate(ChessGame.TeamColor.WHITE)){
            String checkMessage = String.format("%s is in stalemate, DRAW!", data.whiteUsername());
            ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null);
            ctx.send(checkNotification);
            connections.broadcast(session, checkNotification, gameID);
        }
        }
        catch(InvalidMoveException ie){
            ctx.send(new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid move", null));
        }
        catch(DataAccessException | IOException dae){
            ctx.send(new ServerMessage(ServerMessage.ServerMessageType.ERROR, dae.getMessage(), null));
        }
    }

    public void leaveGame(Session session, String username, UserGameCommand command, WsMessageContext ctx, Integer gameID){
        try{
        GameData data = service.getGameDAO().getGame(gameID);
        if(username.equals(data.blackUsername())){
            GameData updatedGame = new GameData(data.gameID(), data.whiteUsername(), null, data.gameName(), data.game());
            service.getGameDAO().updateGame(updatedGame);


        }
        else if(username.equals(data.whiteUsername())){
        GameData updatedGame = new GameData(data.gameID(), null, data.blackUsername(), data.gameName(), data.game());
            service.getGameDAO().updateGame(updatedGame);
        }
        String message = String.format("%s has left the game", username);
        connections.remove(gameID, session);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);
        connections.broadcast(session, notification, gameID);
    }
        catch(DataAccessException | IOException dae){
            ctx.send(new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + dae.getMessage(), null));
        }
    }
    //Maybe I made a game state attribute to chessGame
    public void resign(Session session, String username, UserGameCommand command, WsMessageContext ctx, Integer gameID){
        try {
            GameData data = service.getGameDAO().getGame(gameID);
            ChessGame game = data.game();
            game.setGameState(ChessGame.GameState.GAME_OVER);
            GameData updatedGame = new GameData(gameID, data.whiteUsername(), data.blackUsername(), data.gameName(), game);
            service.getGameDAO().updateGame(updatedGame);
            String resignMessageString = String.format("%s has resigned", username);
            ServerMessage resignMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, resignMessageString, updatedGame.game());
            ctx.send(resignMessage);
            connections.broadcast(session, resignMessage, gameID);
        } catch(DataAccessException | IOException dae){
            ctx.send(new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + dae.getMessage(), null));
        }
    }







    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {

    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {

    }
}
