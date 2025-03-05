package com.wipro.constant;

import java.time.LocalTime;

public class AppointmentShiftTimings {
    public static final LocalTime FIRST_SHIFT_START = LocalTime.parse("08:00:00");
    public static final LocalTime FIRST_SHIFT_END = LocalTime.parse("12:00:00");
    public static final LocalTime SECOND_SHIFT_START = LocalTime.parse("14:00:00");
    public static final LocalTime SECOND_SHIFT_END = LocalTime.parse("18:00:00");
    public static final int FIRST_SHIFT_SLOT_DURATION_IN_MINUTES = 30;
    public static final int SECOND_SHIFT_SLOT_DURATION_IN_MINUTES = 30;
    public static final int TOTAL_NUMBER_OF_SLOTS = 8;
}
