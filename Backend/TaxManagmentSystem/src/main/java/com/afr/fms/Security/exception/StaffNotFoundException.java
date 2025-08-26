package com.afr.fms.Security.exception;

public class StaffNotFoundException extends Exception{
    private String username;
    public static StaffNotFoundException forStaff(String username){
        return new StaffNotFoundException(username);
    }
    
    public StaffNotFoundException(){
        super();
    }

    public StaffNotFoundException(String message){
        super(message);
        this.username=message;
    }

    public StaffNotFoundException (String message, Throwable cause){
        super(message, cause);
    }
    @Override
    public String getMessage(){
        return "Staff '" + this.username + "' is not found";
    }
    
}
