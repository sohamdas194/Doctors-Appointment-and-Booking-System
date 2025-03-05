package com.wipro.exception;

public class NotValidAppointmentSchedulingDetailsException extends RuntimeException{
    public NotValidAppointmentSchedulingDetailsException() {
        super("Invalid Appointment Scheduling Details!!");
    }

    public NotValidAppointmentSchedulingDetailsException(String message) {
        super(message);
    }
}
