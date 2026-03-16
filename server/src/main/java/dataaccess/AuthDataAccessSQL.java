package dataaccess;

import model.AuthData;


import java.sql.*;

import static dataaccess.DatabaseManager.createDatabase;
import static dataaccess.DatabaseManager.getConnection;

public class AuthDataAccessSQL implements AuthDAO{





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
            for (String statement : createAuthDataTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException("");
        }
    }



    private final String[] createAuthDataTable = {
            """
                CREATE TABLE  IF NOT EXISTS authData (
                                id INT NOT NULL AUTO_INCREMENT,
                                authData VARCHAR(255) NOT NULL,
                                username VARCHAR(255) NOT NULL,
                                PRIMARY KEY (id)
                            )"""

    };

    public void createAuth(String username, String authToken) {
        init();
        try(Connection conn = getConnection()){


            try(var statement = conn.prepareStatement("INSERT INTO authData (authToken, username) VALUES (?,?)")){

                statement.setString(1, authToken);
                statement.setString(2, username);
            }
            catch(SQLException sql){
                //something
            }

        }
        catch(DataAccessException | SQLException dte){
            //something

        }
    }
    public AuthData getAuth(String authToken) {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM authData WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String username = rs.getString("username");
                        return new AuthData(authToken, username);
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
    public boolean contains(String authToken){
        try(Connection conn = getConnection()){
            var statement = "SELECT 1 FROM authData WHERE authToken = ? LIMIT 1";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try(ResultSet rs = ps.executeQuery()){
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


    }
    public void deleteAuth(String authToken) {
        try(Connection conn = getConnection()) {
            var statement = "DELETE FROM authData WHERE authToken=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
    public void clear(){
        try(Connection conn = getConnection()) {
            var statement = "IF EXISTS DROP authData";
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
