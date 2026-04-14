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



    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        Session session = ctx.session;
        Gson serializer = new Gson();
        try {
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
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: " + ex.getMessage(), null);
            String json = serializer.toJson(errorMessage);
            ctx.send(json);
        }
    }
    public void saveSession(int gameID, Session session){
        connections.add(gameID, session);

    }


    public void connect(Session session, String username, UserGameCommand command, WsMessageContext ctx, int gameID) {
        Gson serializer = new Gson();
        try{
        GameData data = service.getGameDAO().getGame(gameID);
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, null,  data.game());
        String json = serializer.toJson(message);
        ctx.send(json);
        if(data.blackUsername() != null && data.blackUsername().equals(username)){
            String broadcastMessage = String.format("%s has joined the game as BLACK", username);
            connections.broadcast(session, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage, null, null), gameID);
        }
        else if (data.whiteUsername() != null && data.whiteUsername().equals(username)){
            String broadcastMessage = String.format("%s has joined the game as WHITE", username);
            connections.broadcast(session, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage, null, null), gameID);
        }
        else{
            String broadcastMessage = String.format("%s has joined the game as OBSERVER", username);
            connections.broadcast(session, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage, null, null), gameID);
        }

    }catch(DataAccessException | IOException dae){
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: " + dae.getMessage(), null);
            String json = serializer.toJson(message);
            ctx.send(json);
        }
    catch(NullPointerException npe){
        ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: Game not Found", null);
        String errorJson = serializer.toJson(errorMessage);
        ctx.send(errorJson);
    }

    }

    public void makeMove(Session session, String username, UserGameCommand command, WsMessageContext ctx, int gameID) {
        Gson serializer = new Gson();
        try {
            GameData data = service.getGameDAO().getGame(gameID);
            ChessGame game = data.game();
            if (game.getGameState() == ChessGame.GameState.GAME_OVER) {
                ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: Game is finished", null);
                String notification = serializer.toJson(errorNotification);
                ctx.send(notification);
            } else if (!username.equals(data.whiteUsername()) && !username.equals(data.blackUsername())) {
                ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: only players can make moves", null);
                String notification = serializer.toJson(errorNotification);
                ctx.send(notification);

            }
            else {
                ChessGame.TeamColor teamColor;
                if (data.whiteUsername().equals(username)) {
                    teamColor = ChessGame.TeamColor.WHITE;
                } else {
                    teamColor = ChessGame.TeamColor.BLACK;
                }
                if (game.getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor() != teamColor) {
                    ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: Invalid move", null);
                    String notification = serializer.toJson(errorNotification);
                    ctx.send(notification);
                }
                else{
                game.makeMove(command.getMove());
                GameData updatedGame = new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), data.game());
                service.getGameDAO().updateGame(updatedGame);
                ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, null, game);
                String json = serializer.toJson(loadMessage);
                ctx.send(json);
                connections.broadcast(session, loadMessage, gameID);
                String message = String.format("%s moved from %s to %s", username, command.getMove().getStartPosition(), command.getMove().getEndPosition());
                ServerMessage moveNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null, null);
                connections.broadcast(session, moveNotification, gameID);
                if (game.isInCheck(ChessGame.TeamColor.WHITE, game.getBoard())) {
                    String checkMessage = String.format("%s is in check", data.whiteUsername());
                    ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null, null);
                    String notification = serializer.toJson(checkNotification);
                    ctx.send(notification);
                    connections.broadcast(session, checkNotification, gameID);
                } else if (game.isInCheck(ChessGame.TeamColor.BLACK, game.getBoard())) {
                    String checkMessage = String.format("%s is in check", data.blackUsername());
                    ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null, null);
                    String notification = serializer.toJson(checkNotification);
                    ctx.send(notification);
                    connections.broadcast(session, checkNotification, gameID);
                } else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                    String checkMessage = String.format("%s is in CheckMate, %s WINS!", data.whiteUsername(), data.blackUsername());
                    ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null, null);
                    String notification = serializer.toJson(checkNotification);
                    ctx.send(notification);
                    connections.broadcast(session, checkNotification, gameID);
                } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    String checkMessage = String.format("%s is in CheckMate, %s WINS!", data.blackUsername(), data.whiteUsername());
                    ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null, null);
                    String notification = serializer.toJson(checkNotification);
                    ctx.send(notification);
                    connections.broadcast(session, checkNotification, gameID);
                } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                    String checkMessage = String.format("%s is in stalemate, DRAW!", data.blackUsername());
                    ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null, null);
                    String notification = serializer.toJson(checkNotification);
                    ctx.send(notification);
                    connections.broadcast(session, checkNotification, gameID);
                } else if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                    String checkMessage = String.format("%s is in stalemate, DRAW!", data.whiteUsername());
                    ServerMessage checkNotification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage, null, null);
                    String notification = serializer.toJson(checkNotification);
                    ctx.send(notification);
                    connections.broadcast(session, checkNotification, gameID);
                }
            }
        }
        } catch (InvalidMoveException ie) {
            ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: Invalid move", null);
            String notification = serializer.toJson(errorNotification);
            ctx.send(notification);
        } catch (DataAccessException | IOException dae) {
            ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error:" + dae.getMessage(), null);
            String notification = serializer.toJson(errorNotification);
            ctx.send(notification);
        }
    }

    public void leaveGame(Session session, String username, UserGameCommand command, WsMessageContext ctx, Integer gameID){
        Gson serializer = new Gson();
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
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null, null);
        connections.broadcast(session, notification, gameID);
    }
        catch(DataAccessException | IOException dae){
            ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: " + dae.getMessage(), null);
            String notification = serializer.toJson(errorNotification);
            ctx.send(notification);
        }
        catch(NullPointerException npe){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: Game not Found", null);
            String errorJson = serializer.toJson(errorMessage);
            ctx.send(errorJson);
        }

    }
    //the updated game field, GAME_OVER has to be changed on client end
    //okay I literally set the game state to game over, so I have to check it on the pre-updated game.
    public void resign(Session session, String username, UserGameCommand command, WsMessageContext ctx, Integer gameID){
        Gson serializer = new Gson();
        try {
            GameData data = service.getGameDAO().getGame(gameID);
            ChessGame game = data.game();
            if(data.game().getGameState() == ChessGame.GameState.GAME_OVER){
                String message = "game is over";
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, message, null);
                String error = serializer.toJson(errorMessage);
                ctx.send(error);
            }
            else if(!username.equals(data.whiteUsername()) && !username.equals(data.blackUsername())){
                ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: only players can resign" , null);
                String notification = serializer.toJson(errorNotification);
                ctx.send(notification);

            }
            else{
            game.setGameState(ChessGame.GameState.GAME_OVER);
            GameData updatedGame = new GameData(gameID, data.whiteUsername(), data.blackUsername(), data.gameName(), game);
            service.getGameDAO().updateGame(updatedGame);
            String resignMessageString = String.format("%s has resigned", username);
            ServerMessage resignMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, resignMessageString, null, null);
            String resignJson = serializer.toJson(resignMessage);
            ctx.send(resignJson);
            connections.broadcast(session, resignMessage, gameID);}
        } catch(DataAccessException | IOException dae){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: " + dae.getMessage(), null);
            String errorJson = serializer.toJson(errorMessage);
            ctx.send(errorJson);
        }
        catch(NullPointerException npe){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null,"Error: Game not Found", null);
            String errorJson = serializer.toJson(errorMessage);
            ctx.send(errorJson);
        }
    }







    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {

    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {

    }
}
