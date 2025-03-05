package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class AppointmentHistoryDto {

        private Long appointmentId;
        private String doctorFirstName;
        private String doctorLastName;
        private String patientFirstName;
        private String patientLastName;
        private String memberFirstName;
        private String memberLastName;
        private Integer appointmentSlot;
        private LocalDate appointmentDate;
        private String appointmentStatus;
        private String doctorComment;
        private String patientComment;

}
