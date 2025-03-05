package com.wipro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class AppointmentDto {
    private Long userId;
    private Long memberId;
    private Long doctorAvailabilityId;
    private Integer appointmentSlot;
    private String patientComment;
    private String appointmentStatus;
    private String doctorComment;
}
