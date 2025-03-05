package com.wipro.exception;

public class NoSuchFamilyMemberExistsException extends RuntimeException{
    public NoSuchFamilyMemberExistsException() {
        super("Family Member not found!!");
    }

    public NoSuchFamilyMemberExistsException(String message) {
        super(message);
    }
}
