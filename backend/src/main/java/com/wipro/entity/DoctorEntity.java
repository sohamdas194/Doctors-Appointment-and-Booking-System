package com.wipro.entity;

//import com.wipro.utility.DateConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class DoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "doctor_id", updatable = false)
    private Long doctorId;

    @NotNull
    @Column(name = "first_name", updatable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", updatable = false)
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth", updatable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "gender", updatable = false)
    private String gender;
    @NotNull
    @Column(name = "address_line_no1")
    private String address1;
    @Column(name = "address_line_no2")
    private String address2;
    @NotNull
    @Column(name = "city")
    private String city;

    @NotNull
    @Column(name = "country")
    private String country;

    @NotNull
    @Column(name = "state")
    private String state;

    @NotNull
    @Column(name = "pin_code")
    private String pinCode;

    @NotNull
    @Column(name = "phone_number", length = 10)
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @NotNull
    @Column(name = "blood_group", updatable = false)
    private String bloodGroup;

    @NotNull
    @Column(name = "specialization")
    private String specialization;

    @NotNull
    @Column(name = "consulting_from", updatable = false)
    private LocalDate consultingFrom;

    @Column(name = "consultation_fee")
    private Double consultationFee;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private List<DoctorAvailabilityEntity> doctorAvailabilities;

    public DoctorEntity() {

    }

    public DoctorEntity(String firstName, String lastName,
                        LocalDate dateOfBirth, String gender,
                        String city, String country,
                        String address1, String address2, String state, String pinCode,
                        String phoneNumber, String bloodGroup,
                        String specialization, LocalDate consultingFrom,
                        UserEntity userEntity) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.city = city;
        this.country = country;
        this.address1 = address1;
        this.address2 = address2;
        this.state = state;
        this.pinCode = pinCode;
        this.phoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
        this.specialization = specialization;
        this.consultingFrom = consultingFrom;
        this.userEntity = userEntity;
    }

}
