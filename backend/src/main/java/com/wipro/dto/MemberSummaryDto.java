package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSummaryDto {
    private Long memberId;
    private String name;
    private String gender;
    private Integer age;
}
