package com.wipro.exception;

public class NoSuchAppointmentExistsException extends RuntimeException{
    public NoSuchAppointmentExistsException() {
        super("Appointment not found!!");
    }

    public NoSuchAppointmentExistsException(String message) {
        super(message);
    }
}
