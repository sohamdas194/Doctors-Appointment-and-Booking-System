package com.wipro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentStatusUpdateDto {
    private Long AppointmentId;
    private String appointmentStatus;
}
