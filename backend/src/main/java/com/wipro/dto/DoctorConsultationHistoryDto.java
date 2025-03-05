package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DoctorConsultationHistoryDto {

    private Long appointmentId;
    private String patientName;
    private String gender;
    private int age;
    private LocalDate dateOfAppointment;
    private String appointmentSlot;
    private String patientComments;
    private String DoctorComments;
    private String appointmentStatus;
    private Double consultationFee;
}
