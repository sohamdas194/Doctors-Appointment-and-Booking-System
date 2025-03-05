package com.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class DoctorAvailabilityDto {

    private LocalDate date;
    private Boolean status;
    private Integer shift;

    public DoctorAvailabilityDto() {
    }

    public DoctorAvailabilityDto(LocalDate date, Boolean status, Integer shift) {
        this.date = date;
        this.status = status;
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "DoctorAvailabilityDto{" +
                "date=" + date +
                ", status=" + status +
                ", shift=" + shift +
                '}';
    }
}
