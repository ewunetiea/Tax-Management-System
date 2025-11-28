package com.tms.Security.exception;

public class MultipleSessionsException extends Exception {

    private String username;
    private int sessionCount;

    public static MultipleSessionsException forUser(String username, int sessionCount) {
        return new MultipleSessionsException(username, sessionCount);
    }

    public MultipleSessionsException() {
        super();
    }

    public MultipleSessionsException(String username, int sessionCount) {
        super(username);
        this.username = username;
        this.sessionCount = sessionCount;
    }

    public MultipleSessionsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "User '" + this.username + "' already has " + this.sessionCount + " active sessions.";
    }

    public String getUsername() {
        return username;
    }

    public int getSessionCount() {
        return sessionCount;
    }
}
