package dataaccess;

import model.UserData;
import server.RegisterRequest;

public interface UserDAO {

    void createUser(RegisterRequest request);
    UserData getUser(String username);
    void clear();
    boolean contains(String username);

}
