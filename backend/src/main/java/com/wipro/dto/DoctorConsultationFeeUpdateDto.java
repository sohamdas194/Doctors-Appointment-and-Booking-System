package com.wipro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorConsultationFeeUpdateDto {
    private Long doctorId;
    private Double consultationFee;
}
