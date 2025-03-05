package com.wipro.repository;

import com.wipro.entity.AppointmentEntity;
import com.wipro.entity.DoctorEntity;
import com.wipro.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity,Long> {

    public Optional<DoctorEntity> findByUserEntity(UserEntity user);


    @Query("SELECT a from AppointmentEntity a join DoctorAvailabilityEntity da on (a.doctorAvailability = da) " +
            "join DoctorEntity d on (da.doctor = d) join PatientEntity p on (a.patient = p) " +
            " left join FamilyMemberEntity m on (a.member = m) where d.doctorId = :doctorId and a.appointmentStatus IN (:consulted, :completed,:noShow,:cancelled) ORDER BY da.date DESC")
    public List<AppointmentEntity> getAllDoctorCompletedConsultations(Long doctorId, @Param("consulted") String consulted, @Param("completed") String completed, @Param("noShow") String noShow, @Param("cancelled") String cancelled);


    @Query("SELECT a FROM AppointmentEntity a " +
            "JOIN a.doctorAvailability da " +
            "JOIN da.doctor d " +
            "JOIN a.patient p " +
            "LEFT JOIN a.member m " +
            "WHERE d.doctorId = :doctorId " +
            "AND (:fromDate IS NULL OR da.date >= :fromDate) " +
            "AND (:toDate IS NULL OR da.date <= :toDate) " +
            "AND (:patientName IS NULL OR " +
            "((p.firstName LIKE %:patientName% OR p.lastName LIKE %:patientName%) AND a.member IS NULL) " +
            "OR (m.firstName IS NOT NULL AND (m.firstName LIKE %:patientName% OR m.lastName LIKE %:patientName%)) " +
            ") " +
            "AND a.appointmentStatus IN (:consulted, :completed, :noShow, :cancelled)" +
            " ORDER BY da.date DESC")
    public List<AppointmentEntity> getAllConsultationsByDoctorIdAndDateRangeAndPatientName(@Param("doctorId") Long doctorId, @Param("fromDate")LocalDate fromDate, @Param("toDate") LocalDate toDate,@Param("patientName") String patientName, @Param("consulted") String consulted, @Param("completed") String completed,@Param("noShow") String noShow,@Param("cancelled") String cancelled);


    @Modifying
    @Transactional
    @Query("update DoctorEntity d set d.consultationFee = :consultationFee where d.doctorId = :doctorId")
    public void updateConsultationFeeByDoctorId(Long doctorId, Double consultationFee);

    @Query("select d from DoctorEntity d where d.firstName||' '||d.lastName like '%'||:name||'%' order by d.consultationFee asc")
    public List<DoctorEntity> findAllDoctorsByName(String name);

    @Query("SELECT a from AppointmentEntity a join DoctorAvailabilityEntity da on (a.doctorAvailability = da) " +
            "join DoctorEntity d on (da.doctor = d) join PatientEntity p on (a.patient = p) " +
            " left join FamilyMemberEntity m on (a.member = m) where " +
            "d.doctorId = :doctorId and da.date>=CURRENT_DATE and a.appointmentStatus IN (:scheduled) ")
    public List<AppointmentEntity> getAllUpcomingAppointments(@Param("doctorId") Long doctorId, @Param("scheduled") String scheduled);


    @Query("SELECT a from AppointmentEntity a join DoctorAvailabilityEntity da on (a.doctorAvailability = da) " +
            "join DoctorEntity d on (da.doctor = d) join PatientEntity p on (a.patient = p) " +
            " left join FamilyMemberEntity m on (a.member = m) where " +
            "d.doctorId = :doctorId and da.date=:date and a.appointmentStatus IN (:scheduled) ")
    public List<AppointmentEntity> getAppointmentsByDate(@Param("doctorId") Long doctorId,  @Param("date") LocalDate date, @Param("scheduled") String scheduled);


    @Query("SELECT a from AppointmentEntity a join DoctorAvailabilityEntity da on (a.doctorAvailability = da) " +
            "join DoctorEntity d on (da.doctor = d) join PatientEntity p on (a.patient = p) " +
            " left join FamilyMemberEntity m on (a.member = m) where " +
            "d.doctorId = :doctorId and (da.date>=:fromDate and da.date<=:toDate)  and a.appointmentStatus IN (:scheduled) ")
    public List<AppointmentEntity> getAppointmentsBetweenDates(@Param("doctorId") Long doctorId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("scheduled") String scheduled);
}



