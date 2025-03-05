package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AppointmentSearchResultDto {
    private Long appointmentId;
    private String doctorName;
    private String doctorSpecialization;
    private String patientName;
    private String patientGender;
    private Integer patientAge;
    private LocalDate appointmentDate;
    private String appointmentTimeSlot;
    private String appointmentStatus;
    private String patientComment;
    private String doctorComment;
}
