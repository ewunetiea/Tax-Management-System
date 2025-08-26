package com.afr.fms.Security.exception;

/**
 * Exception thrown by system in case some one try to register with already
 * exisiting email
 * id in the system.
 */
public class UserAlreadyExistException extends Exception {
    private String user;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "User '" + this.user + "' Already Exists";
    }

}
