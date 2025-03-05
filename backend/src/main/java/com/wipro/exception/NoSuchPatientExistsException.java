package com.wipro.exception;

public class NoSuchPatientExistsException extends RuntimeException{
    public NoSuchPatientExistsException() {
        super("Patient not found!!");
    }

    public NoSuchPatientExistsException(String message) {
        super(message);
    }
}
