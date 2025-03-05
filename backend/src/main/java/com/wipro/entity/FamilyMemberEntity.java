package com.wipro.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "family_member")
public class FamilyMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "bloodGroup")
    private String bloodGroup;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Column(name = "relationship")
    private String relationship;

    @Column(name = "healthHistory")
    private String healthHistory;

    @Column(name = "height_in_cm")
    private int height;

    @Column(name = "weight_in_kg")
    private int weight;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private PatientEntity patient;

    public FamilyMemberEntity(String firstName, String lastName, String gender, String bloodGroup,
                              String contactNo, LocalDate dob, String relationship, int height, int weight, String healthHistory, PatientEntity patient) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.contactNo = contactNo;
        this.dob = dob;
        this.relationship = relationship;
        this.height = height;
        this.weight = weight;
        this.patient = patient;
        this.healthHistory = healthHistory;
    }

    public FamilyMemberEntity() {

    }
}
