package com.wipro.repository;

import com.wipro.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    @Query("select a.appointmentSlotNumber from AppointmentEntity a join a.doctorAvailability da " +
            "where " +
            "da.doctor = :doc and " +
            "(da.date = :apmntDate and da.status = true) and " +
            "a.appointmentStatus != :cancelledStatus")
    public List<Integer> getUsedAppointmentSlotsByDocAndDate(DoctorEntity doc, LocalDate apmntDate, String cancelledStatus);

    @Modifying
    @Transactional
    @Query("update AppointmentEntity a set a.appointmentStatus = :newStatus where a.appointmentId = :appointmentId")
    public void updateAppointmentStatusByAppointmentId(Long appointmentId, String newStatus);

    @Modifying
    @Transactional
    @Query("update AppointmentEntity a set a.appointmentStatus = :noShowStatus where a.doctorAvailability.date = CURRENT_DATE and a.appointmentStatus = :scheduledStatus")
    public void updateAppointmentStatusAtEOD(String noShowStatus, String scheduledStatus);

    public List<AppointmentEntity> findAppointmentListByPatient(PatientEntity patient);

    public AppointmentEntity findByAppointmentId(Long appointmentId);

    List<AppointmentEntity> findByDoctorAvailability(DoctorAvailabilityEntity availabilityEntity);
}

