package com.wipro.exception;

public class NoSuchDoctorExistsException extends RuntimeException{
    public NoSuchDoctorExistsException() {
        super("Doctor not found!!");
    }

    public NoSuchDoctorExistsException(String message) {
        super(message);
    }
}
