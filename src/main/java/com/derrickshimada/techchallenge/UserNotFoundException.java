package com.derrickshimada.techchallenge;

public class UserNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 4104532665868418459L;

    UserNotFoundException(String username) {
        super("Could not find user: " + username);
    }
}