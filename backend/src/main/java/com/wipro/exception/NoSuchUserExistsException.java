package com.wipro.exception;

public class NoSuchUserExistsException extends RuntimeException{
    public NoSuchUserExistsException() {
        super("User not found!!");
    }

    public NoSuchUserExistsException(String message) {
        super(message);
    }
}
