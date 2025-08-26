package com.afr.fms.Security.exception;

public class UserNotFoundException extends Exception {

    private String username;

    public static UserNotFoundException forUser(String username) {
        return new UserNotFoundException(username);
    }

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
        this.username = message;
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "User '" + this.username + "' is not found";
    }
}
