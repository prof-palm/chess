package server;

import org.mindrot.jbcrypt.BCrypt;

public record RegisterRequest(String username, String password, String email){}