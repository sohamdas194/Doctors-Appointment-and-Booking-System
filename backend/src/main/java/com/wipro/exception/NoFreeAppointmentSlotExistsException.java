package com.wipro.exception;

public class NoFreeAppointmentSlotExistsException extends RuntimeException{
    public NoFreeAppointmentSlotExistsException() {
        super("No free slots available!");
    }

    public NoFreeAppointmentSlotExistsException(String message) {
        super(message);
    }
}
