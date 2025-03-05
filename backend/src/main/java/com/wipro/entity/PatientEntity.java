package com.wipro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "patient_id")
    private Long patientId;
    @NotNull(message = "firstname required")
    @Column(name = "first_name")
    private String firstName;
    @NotNull(message = "lastName required")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "height")
    private Long height;

    @Column(name = "weight")
    private Long weight;


    @Column(name = "marital_status")
    private String maritalStatus;


    @Column(name = "phone_number")
    private Long phone;


    @Column(name = "address_line1")
    private String streetAddressLine1;


    @Column(name = "address_line2")
    private String streetAddressLine2;


    @Column(name = "city")
    private String city;


    @Column(name = "state")
    private String state;


    @Column(name = "pin_code")
    private String pin;


    @Column(name = "emergency_first_name")
    private String emergencyFirstName;


    @Column(name = "emergency_last_name")
    private String emergencyLastName;


    @Column(name = "emergency_relationship")
    private String emergencyRelationship;


    @Column(name = "emergency_contact")
    private String emergencyContact;


    @Column(name = "reason_for_registration")
    private String reasonForRegistration;


    @Column(name = "additional_notes")
    private String additionalNotes;


    private String insuranceCompany;
    private String insuranceId;
    private String policyHolderName;
    private LocalDate policyDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<FamilyMemberEntity> members;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<AppointmentEntity> appointments;


    public PatientEntity(String firstName, String lastName, LocalDate dob, String gender, String bloodGroup, String maritalStatus, long phone, String streetAddressLine1, String streetAddressLine2, String city, String state, String pin, String emergencyFirstName, String emergencyLastName, String emergencyRelationship, String emergencyContact, String reasonForRegistration, String additionalNotes, UserEntity user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.maritalStatus = maritalStatus;
        this.phone = phone;
        this.streetAddressLine1 = streetAddressLine1;
        this.streetAddressLine2 = streetAddressLine2;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.emergencyFirstName = emergencyFirstName;
        this.emergencyLastName = emergencyLastName;
        this.emergencyRelationship = emergencyRelationship;
        this.emergencyContact = emergencyContact;
        this.reasonForRegistration = reasonForRegistration;
        this.additionalNotes = additionalNotes;
        this.user = user;
    }

    public PatientEntity() {
    }


}
