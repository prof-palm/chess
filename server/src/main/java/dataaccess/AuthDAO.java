package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public AuthData getAuthData(String authToken);
    public void createAuth(String username, String authToken);
    public void clear();
}
