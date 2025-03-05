package com.wipro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long appointmentId;
//    @NotNull
    private Integer appointmentSlotNumber;
    @NotNull
    private String patientComment;
    private String doctorComment;
    @NotNull
    private String appointmentStatus;
    @ManyToOne
    @JoinColumn(name="patient_id",referencedColumnName = "patient_id")
    private PatientEntity patient;
    @ManyToOne
    @JoinColumn(name="member_id",referencedColumnName = "member_id")
    private FamilyMemberEntity member;
    @NotNull
    @ManyToOne
    @JoinColumn(name="doctor_available_id",referencedColumnName = "doctor_available_id")
    private DoctorAvailabilityEntity doctorAvailability;

}
