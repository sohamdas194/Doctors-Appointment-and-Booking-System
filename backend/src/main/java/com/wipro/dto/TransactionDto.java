package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long appointmentId;
    private Long transactionId;
    private Double consultationFee;
    private Long patientId;
    private Long memberId;
    private Long doctorId;
    private String status;
}
