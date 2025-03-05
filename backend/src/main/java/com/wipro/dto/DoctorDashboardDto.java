package com.wipro.dto;

import lombok.*;

import java.time.LocalDate;
@Getter

@Setter

@NoArgsConstructor

@AllArgsConstructor

@ToString
public class DoctorDashboardDto {

    private Long appointmentId;
    private String firstName;
    private String lastName;
    private String gender;
    private int age;
    private LocalDate appointmentDate;
    private String slotTiming;
    private String patientComments;


}
