package com.afr.fms.Security.exception;

public class NoUsersAvailableException extends Exception {
    
    
    public NoUsersAvailableException(){
        super();
    }

    public NoUsersAvailableException(String message){
        super(message);
    }

    public NoUsersAvailableException (String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public String getMessage(){
        return "No users currently in the system!";
    }
    
}
