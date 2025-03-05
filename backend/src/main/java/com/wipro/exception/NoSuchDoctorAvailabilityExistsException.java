package com.wipro.exception;

public class NoSuchDoctorAvailabilityExistsException extends RuntimeException{
    public NoSuchDoctorAvailabilityExistsException() {
        super("Doctor not available!!");
    }

    public NoSuchDoctorAvailabilityExistsException(String message) {
        super(message);
    }
}
