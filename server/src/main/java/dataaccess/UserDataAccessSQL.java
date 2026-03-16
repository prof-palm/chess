package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import server.RegisterRequest;

import javax.xml.crypto.Data;
import java.sql.*;

import static dataaccess.DatabaseManager.createDatabase;
import static dataaccess.DatabaseManager.getConnection;

public class UserDataAccessSQL {



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
            for (String statement : createUserDataTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException("");
        }
    }

    private final String[] createUserDataTable = {
         """
                CREATE TABLE  IF NOT EXISTS userData (
                                id INT NOT NULL AUTO_INCREMENT,
                                username VARCHAR(255) NOT NULL UNIQUE,
                                password VARCHAR(255) NOT NULL,
                                email VARCHAR(255) NOT NULL,
                                PRIMARY KEY (id)
                            )"""

    };


    public void createUser(RegisterRequest request) throws SQLException {
        try(Connection conn = getConnection()){


        try(var statement = conn.prepareStatement("INSERT INTO userData (username, password, email) VALUES (?,?,?)")){

        statement.setString(1, request.username());
        statement.setString(2, request.password());
        statement.setString(3, request.email());
        }
        catch(SQLException sql){
            //something
        }

        }
        catch(DataAccessException dte){
            //something

        }
    }



    public UserData getUser(String username) {
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
            throw new SQLException();

        }
    } catch (DataAccessException | SQLException dte) {
        System.out.print("Failed to access Database");
    }
        return null;
    }



    public void clear(){
        try(Connection conn = getConnection()) {
            var statement = "IF EXISTS DROP userData";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean contains(String username){
        try(Connection conn = getConnection()){
            var statement = "SELECT 1 FROM userData WHERE username = ? LIMIT 1";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
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
}

