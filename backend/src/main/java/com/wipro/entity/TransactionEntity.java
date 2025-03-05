package com.wipro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGen")
    @SequenceGenerator(name = "mySeqGen", sequenceName = "transaction_seq", allocationSize = 1)
    @Column(name = "transaction_id", updatable = false)
    private Long transactionId;
    @Column(name = "appointment_id")
    private Long appointmentId;
    @Column(name="consultation_fee")
    private Double consultationFee;
    @Column(name="patient_id")
    private Long patientId;
    @Column(name="member_id")
    private Long memberId;
    @Column(name="doctor_id")
    private Long doctorId;
    @Column(name="transaction_date")
    private LocalDateTime transactionDateTime;
    private String status;

}
