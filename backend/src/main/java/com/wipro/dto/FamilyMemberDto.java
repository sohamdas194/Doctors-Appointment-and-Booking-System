package com.wipro.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyMemberDto {
    private Long memberId;
    private String firstName;
    private String lastName;
    private String gender;
    private String bloodGroup;
    private String contactNo;
    private LocalDate dob;
    private String relationship;
    private int height;
    private int weight;
    private String healthHistory;

}
