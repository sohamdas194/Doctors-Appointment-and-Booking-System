package com.wipro.entity;

//import com.wipro.utility.DateConverter;

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
@Table(name = "doctor_available")
public class DoctorAvailabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "doctor_available_id")
    private Long id;
    @NotNull
    @Column(name = "date")

    private LocalDate date;
    @NotNull
    @Column(name = "status", columnDefinition = "BOOLEAN")
    private Boolean status;
    @NotNull
    private Integer shift;
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id")
    private DoctorEntity doctor;

    @OneToMany(mappedBy = "doctorAvailability", cascade = CascadeType.ALL)
    private List<AppointmentEntity> appointments;

    public DoctorAvailabilityEntity(LocalDate date, Boolean status, Integer shift, DoctorEntity doctor) {
        this.date = date;
        this.status = status;
        this.shift = shift;
        this.doctor = doctor;
    }

    public DoctorAvailabilityEntity() {
    }

    public DoctorEntity getDoctorEntity() {
        return doctor;
    }

    public void setDoctorEntity(DoctorEntity doctorEntity) {
        this.doctor = doctorEntity;
    }

    @Override
    public String toString() {
        return "DoctorAvailabilityEntity{" +
                "id=" + id +
                ", date=" + date +
                ", status=" + status +
                ", shift=" + shift +
                ", doctor=" + doctor +
                '}';
    }
}
