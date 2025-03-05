package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto extends UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private Long height;
    private Long weight;
    private String bloodGroup;
    private String maritalStatus;
    private Long phone;
    private String streetAddressLine1;
    private String streetAddressLine2;
    private String city;
    private String state;
    private String pin;
    private String emergencyFirstName;
    private String emergencyLastName;
    private String emergencyRelationship;
    private String emergencyContact;
    private String reasonForRegistration;
    private String additionalNotes;
    private String insuranceCompany;
    private String insuranceId;
    private String policyHolderName;
    private LocalDate policyDate;

    public PatientDto() {

    }

    public PatientDto(String email, String password, String role, String firstName, String lastName, LocalDate dob, String gender, Long height, Long weight, String bloodGroup, String maritalStatus, Long phone, String streetAddressLine1, String streetAddressLine2, String city, String state, String pin, String emergencyFirstName, String emergencyLastName, String emergencyRelationship, String emergencyContact, String reasonForRegistration, String additionalNotes, String insuranceCompany, String insuranceId, String policyHolderName, LocalDate policyDate) {
        super(email, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
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
        this.insuranceCompany = insuranceCompany;
        this.insuranceId = insuranceId;
        this.policyHolderName = policyHolderName;
        this.policyDate = policyDate;
    }

    public PatientDto(Long id, String firstName, String lastName, LocalDate dob, String gender, Long height, Long weight, String bloodGroup, String maritalStatus, Long phone, String streetAddressLine1, String streetAddressLine2, String city, String state, String pin, String emergencyFirstName, String emergencyLastName, String emergencyRelationship, String emergencyContact, String reasonForRegistration, String additionalNotes, String insuranceCompany, String insuranceId, String policyHolderName, LocalDate policyDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
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
        this.insuranceCompany = insuranceCompany;
        this.insuranceId = insuranceId;
        this.policyHolderName = policyHolderName;
        this.policyDate = policyDate;
    }

    @Override
    public String toString() {
        return "PatientDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", phone=" + phone +
                ", streetAddressLine1='" + streetAddressLine1 + '\'' +
                ", streetAddressLine2='" + streetAddressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pin='" + pin + '\'' +
                ", emergencyFirstName='" + emergencyFirstName + '\'' +
                ", emergencyLastName='" + emergencyLastName + '\'' +
                ", emergencyRelationship='" + emergencyRelationship + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", reasonForRegistration='" + reasonForRegistration + '\'' +
                ", additionalNotes='" + additionalNotes + '\'' +
                '}';
    }

}
