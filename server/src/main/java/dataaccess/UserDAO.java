package dataaccess;

import model.UserData;
import server.RegisterRequest;

public interface UserDAO {

    public void createUser(RegisterRequest request);
    public UserData getUserData(String username);
    public void clear();

}
