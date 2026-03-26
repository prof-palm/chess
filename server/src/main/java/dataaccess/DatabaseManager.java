package dataaccess;


import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */

    private static String[] userDataTable = {
            """
                CREATE TABLE  IF NOT EXISTS userData (
                                id INT NOT NULL AUTO_INCREMENT,
                                username VARCHAR(255) NOT NULL UNIQUE,
                                password VARCHAR(255) NOT NULL,
                                email VARCHAR(255) NOT NULL,
                                PRIMARY KEY (id)
                            )"""

    };

    private static String[] authDataTable = {
            """
                CREATE TABLE  IF NOT EXISTS authData (
                                id INT NOT NULL AUTO_INCREMENT,
                                authToken VARCHAR(255) NOT NULL,
                                username VARCHAR(255) NOT NULL,
                                PRIMARY KEY (id)
                            )"""

    };
    private static String[] gameDataTable = {
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



    static {
        loadPropertiesFromResources();
        try{configureDatabase();}
        catch(DataAccessException ex){
            throw new RuntimeException("Failure to configure database");
        }



    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }

    static public void configureDatabase() throws DataAccessException {
        createDatabase();
        try(Connection conn = getConnection()){
            createTable(userDataTable, conn);
            createTable(authDataTable, conn);
            createTable(gameDataTable, conn);


        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }


    public static void createTable(String[] table, Connection conn) throws DataAccessException {
        for (String statement : table) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }






}
