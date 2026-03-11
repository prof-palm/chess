package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public AuthData getAuth(String authToken);
    public void createAuth(String username, String authToken);
    public void clear();
    public void deleteAuth(String authToken);
    public  boolean contains(String authToken);
}
