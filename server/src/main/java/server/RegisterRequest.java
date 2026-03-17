package server;

import org.mindrot.jbcrypt.BCrypt;

public record RegisterRequest(String username, String password, String email){
    public RegisterRequest{
        if(password != null) {
            password = passwordHasher(password());
        }

    }
    String passwordHasher(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

    }
}