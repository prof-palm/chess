package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import requests.JoinGameRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static dataaccess.DatabaseManager.getConnection;

public class GameDataAccessSQL implements GameDAO{


    public GameData getGame(Integer gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT whiteUsername, blackUsername, gameName, game FROM gameData WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String gameJSON = rs.getString("game");
                        Gson serializer = new Gson();
                        ChessGame game = serializer.fromJson(gameJSON, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                    else{
                        return null;
                    }
                }
            }
            catch(SQLException sql){
                throw new DataAccessException(sql.getMessage());

            }
        } catch (DataAccessException | SQLException dte) {
            throw new DataAccessException(dte.getMessage());
        }
    }
    public Integer createGame(String gameName) throws DataAccessException{
        try(Connection conn = getConnection()){
            Integer gameID = randomID();
            ChessGame game = new ChessGame();
            Gson serializer = new Gson();
            String gameSerialized = serializer.toJson(game);
            try(var statement = conn.prepareStatement(
                    "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)")) {
                statement.setInt(1, gameID);
                statement.setString(2, null);
                statement.setString(3, null);
                statement.setString(4, gameName);
                statement.setString(5, gameSerialized);
                statement.executeUpdate();
                return gameID;
            }
            catch(SQLException sql){
                throw new DataAccessException("");
            }

        }
        catch(DataAccessException | SQLException dte){
            throw new DataAccessException("");

        }

    }
    public Collection<GameData> listGames()throws DataAccessException{
        try(Connection conn = getConnection()){
        ArrayList<GameData> result = new ArrayList<>();
        var statement = "SELECT * from gameData";
        try(PreparedStatement ps = conn.prepareStatement(statement)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Gson serializer = new Gson();
                    ChessGame game = serializer.fromJson(rs.getString("game"), ChessGame.class);
                    GameData entry = new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                            rs.getString("blackUsername"), rs.getString("gameName"), game);
                    result.add(entry);

                }
                return result;
            }

        }

    }
        catch(Exception exe){
            throw new DataAccessException("");

        }
    }

    public void updateGame(JoinGameRequest request, String username) throws DataAccessException{
        try(Connection conn = getConnection()){
        if(request.playerColor().equals("WHITE")){
            try(PreparedStatement updateRow = conn.prepareStatement("UPDATE gameData SET whiteUsername=? WHERE gameID=?") ) {
                updateRow.setString(1,username);
                updateRow.setInt(2, request.gameID());
                updateRow.executeUpdate();


            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }

        }
        else{
            try(PreparedStatement updateRow = conn.prepareStatement("UPDATE gameData SET blackUsername=? WHERE gameID=?") ) {
                updateRow.setString(1,username);
                updateRow.setInt(2, request.gameID());
                updateRow.executeUpdate();


            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }

        }

    } catch (SQLException sql) {
            throw new DataAccessException(sql.getMessage());
        }
    }




    public Integer randomID(){
        return (int)(Math.random() * 9000) + 1000;
    }


    public void clear() throws DataAccessException{
        try(Connection conn = getConnection()) {
            var statement = "TRUNCATE TABLE gameData";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }

        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("");
        }

    }
}
