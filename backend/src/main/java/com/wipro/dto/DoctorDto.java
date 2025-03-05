package com.wipro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto extends UserDto{

    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("dateOfBirth")
//    @Convert(converter = DateConverter.class)
    private LocalDate dateOfBirth;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("address1")
    private String address1;
    @JsonProperty("address2")
    private String address2;
    @JsonProperty("state")
    private String state;
    @JsonProperty("pinCode")
    private String pinCode;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("bloodGroup")
    private String bloodGroup;
    @JsonProperty("specialization")
    private String specialization;
    @JsonProperty("consultingFrom")
    private LocalDate consultingFrom;
    @JsonProperty("consultationFee")
    private Double consultationFee;



    public DoctorDto(String email, String password,
                     String role, String firstName,
                     String lastName, LocalDate dateOfBirth,
                     String gender, String city,
                     String country, String address1,
                     String address2,String state,
                     String pinCode, String phoneNumber,
                     String bloodGroup, String specialization,
                     LocalDate consultingFrom)
    {
        super(email, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.city = city;
        this.country = country;
        this.address1 = address1;
        this.address2=address2;
        this.state=state;
        this.pinCode = pinCode;
        this.phoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
        this.specialization = specialization;
        this.consultingFrom = consultingFrom;
    }


}
