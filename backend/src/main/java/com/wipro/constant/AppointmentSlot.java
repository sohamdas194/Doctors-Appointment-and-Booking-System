package com.wipro.constant;

import java.util.Arrays;
import java.util.Optional;

public enum AppointmentSlot {


    FIRST_SHIFT_FIRST_SLOT(1,1,"8:00AM-8:30AM"),
    FIRST_SHIFT_SECOND_SLOT(1,2,"8:30AM-9:00AM"),
    FIRST_SHIFT_THIRD_SLOT(1,3,"9:00AM-9:30AM"),
    FIRST_SHIFT_FOURTH_SLOT(1,4,"9:30AM-10:00AM"),
    FIRST_SHIFT_FIVE_SLOT(1,5,"10:00AM-10:30AM"),
    FIRST_SHIFT_SIXTH_SLOT(1,6,"10:30AM-11:00AM"),
    FIRST_SHIFT_SEVEN_SLOT(1,7,"11:00AM-11:30AM"),
    FIRST_SHIFT_EIGHT_SLOT(1,8,"11:30AM-12:00PM"),

    SECOND_SHIFT_FIRST_SLOT(2,1,"14:00-14:30"),
    SECOND_SHIFT_SECOND_SLOT(2,2,"14:30-15:00"),
    SECOND_SHIFT_THIRD_SLOT(2,3,"15:00-15:30"),
    SECOND_SHIFT_FOURTH_SLOT(2,4,"15:30-16:00"),
    SECOND_SHIFT_FIVE_SLOT(2,5,"16:00-16:30"),
    SECOND_SHIFT_SIXTH_SLOT(2,6,"16:30-17:00"),
    SECOND_SHIFT_SEVEN_SLOT(2,7,"17:00-17:30"),
    SECOND_SHIFT_EIGHT_SLOT(2,8,"17:30-18:00");

    private final Integer shift;
    private final Integer slotNumber;
    private final String slotTiming;

    AppointmentSlot(Integer shift, Integer slotNumber, String slotTiming) {
        this.shift = shift;
        this.slotNumber = slotNumber;
        this.slotTiming = slotTiming;
    }

    public Integer getShift() {
        return shift;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public String getSlotTiming() {
        return slotTiming;
    }

    public  static Optional<AppointmentSlot> getAppointmentSlotByValues(int shift, int slotNumber){
        return Arrays.stream(AppointmentSlot.values()).filter(x->x.shift==shift && x.slotNumber==slotNumber).findFirst();
    }

}
