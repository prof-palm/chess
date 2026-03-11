package dataaccess;

import model.UserData;
import server.RegisterRequest;

public interface UserDAO {

    public void createUser(RegisterRequest request);
    public UserData getUser(String username);
    public void clear();
    public  boolean contains(String username);

}
