package dataaccess;

import model.AuthData;


import java.sql.*;

import static dataaccess.DatabaseManager.createDatabase;
import static dataaccess.DatabaseManager.getConnection;

public class AuthDataAccessSQL implements AuthDAO{




    public void createAuth(String username, String authToken) throws DataAccessException {
        try(Connection conn = getConnection()){


            try(var statement = conn.prepareStatement("INSERT INTO authData (authToken, username) VALUES (?,?)")){

                statement.setString(1, authToken);
                statement.setString(2, username);
                statement.executeUpdate();
            }
            catch(SQLException sql){
                throw new DataAccessException(sql.getMessage());
            }

        }
        catch(DataAccessException | SQLException dte){
            throw new DataAccessException(dte.getMessage());

        }
    }
    public AuthData getAuth(String authToken)throws DataAccessException{
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
                throw new DataAccessException(sql.getMessage());

            }
        } catch (DataAccessException | SQLException dte) {
            throw new DataAccessException(dte.getMessage());
        }
    }
    public boolean contains(String authToken) throws DataAccessException{
        try(Connection conn = getConnection()){
            var statement = "SELECT 1 FROM authData WHERE authToken = ? LIMIT 1";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try(ResultSet rs = ps.executeQuery()){
                    return rs.next();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }


    }
    public void deleteAuth(String authToken) throws DataAccessException{
        try(Connection conn = getConnection()) {
            var statement = "DELETE FROM authData WHERE authToken=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                ps.executeUpdate();
            }

        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());        }

    }
    public void clear() throws DataAccessException{
        try(Connection conn = getConnection()) {
            var statement = "TRUNCATE TABLE authData";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }

        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
