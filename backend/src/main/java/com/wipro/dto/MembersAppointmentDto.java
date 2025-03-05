package com.wipro.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MembersAppointmentDto {

	private long appointmentId;
	private LocalDate appointmentDate;
	private String appointmentTime;
	private String appointmentStatus;
	private String patientName;
	private String patientGender;
	private Integer patientAge;
	private String patientComments;
	private String docName;
	private String docSpecialization;
	private String docContactNo;
	private Integer docExp;
}
