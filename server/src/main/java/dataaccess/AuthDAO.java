package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public AuthData getAuth(String authToken)throws DataAccessException;

    public void createAuth(String username, String authToken)throws DataAccessException;

    public void clear( )throws DataAccessException;

    public void deleteAuth(String authToken)throws DataAccessException;

    public  boolean contains(String authToken)throws DataAccessException;
}
