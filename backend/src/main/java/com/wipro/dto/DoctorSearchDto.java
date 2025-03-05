package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSearchDto {
    private Long doctorId;
    private String name;
    private String specialization;
    private Integer experience;
    private String city;
    private String pinCode;
    private Double consultationFee;
}
