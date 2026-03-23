package dataaccess;

import model.UserData;
import server.RegisterRequest;

public interface UserDAO {

    void createUser(RegisterRequest request)throws DataAccessException;

    UserData getUser(String username)throws DataAccessException;

    void clear() throws DataAccessException;

    boolean contains(String username)throws DataAccessException;

}
