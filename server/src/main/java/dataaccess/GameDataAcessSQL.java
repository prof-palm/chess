package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import server.RegisterRequest;
import service.CreateGameResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.createDatabase;
import static dataaccess.DatabaseManager.getConnection;

public class GameDataAcessSQL {
    //Black and White username can be null
//How do I deal with ChessGame object, use a serializer - BLOB?

    public void init(){
        try {
            createDatabase();
            configureDatabase();
        }
        catch(DataAccessException dte){
            System.out.print("failed creation of database");
        }
    }

    private void configureDatabase() throws DataAccessException {
        try (Connection conn = getConnection()) {
            for (String statement : GameDataTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException("");
        }
    }


    //figure out the serialization of chessGame
    private final String[] GameDataTable = {
            """
                CREATE TABLE  IF NOT EXISTS gameData (
                                id INT NOT NULL AUTO_INCREMENT,
                                gameID INT NOT NULL,
                                whiteUsername VARCHAR(255),
                                blackUsername VARCHAR(255),
                                gameName VARCHAR(255) NOT NULL,
                                game TEXT NOT NULL,
                                PRIMARY KEY (id)
                            )"""

    };

    public GameData getGame(Integer gameID) {
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
                throw new SQLException();

            }
        } catch (DataAccessException | SQLException dte) {
            System.out.print("Failed to access Database");
        }
        return null;
    }
    public Integer createGame(String gameName) throws SQLException {
        try(Connection conn = getConnection()){
            Integer gameID = randomID();
            ChessGame game = new ChessGame();
            Gson serializer = new Gson();
            String gameSerialized = serializer.toJson(game);
            try(var statement = conn.prepareStatement("INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)")) {
                statement.setInt(1, gameID);
                statement.setString(2, null);
                statement.setString(3, null);
                statement.setString(4, gameName);
                statement.setString(5, gameSerialized);
                return gameID;
            }
            catch(SQLException sql){
                //something
            }

        }
        catch(DataAccessException dte){
            //something

        }

    }
    public Integer randomID(){
        return (int)(Math.random() * 9000) + 1000;
    }


    public void clear(){
        try(Connection conn = getConnection()) {
            var statement = "IF EXISTS DROP gameData";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }



}
