package dataaccess;

import model.UserData;
import requests.RegisterRequest;


import java.sql.*;

import static dataaccess.DatabaseManager.getConnection;

public class UserDataAccessSQL implements UserDAO{


    public void createUser(RegisterRequest request) throws DataAccessException{
        try(Connection conn = getConnection()){
        try(var statement = conn.prepareStatement("INSERT INTO userData (username, password, email) VALUES (?,?,?)")) {
        statement.setString(1, request.username());
        statement.setString(2, request.password());
        statement.setString(3, request.email());
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



    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
        var statement = "SELECT password, email FROM userData WHERE username=?";
        try (PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
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



    public void clear() throws DataAccessException{
        try(Connection conn = getConnection()) {
            var statement = "TRUNCATE TABLE userData";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }

        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
    public boolean contains(String username) throws DataAccessException{
        try(Connection conn = getConnection()){
            var statement = "SELECT 1 FROM userData WHERE username = ? LIMIT 1";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(ResultSet rs = ps.executeQuery()){
                    return rs.next();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }


    }
}

