package com.wipro.repository;

import com.wipro.entity.AppointmentEntity;
import com.wipro.entity.PatientEntity;
import com.wipro.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    public Optional<PatientEntity> findByUser(UserEntity user);//Soham code

    public PatientEntity findPatientByUser(UserEntity user);//prasad code

    public PatientEntity findPatientsByUserUserId(Long userId);//madhus code

    //    select a from AppointmentEntity a join a.doctorAvailability da join da.doctor join ...
    @Query("SELECT a from AppointmentEntity a join DoctorAvailabilityEntity da on (a.doctorAvailability = da) join DoctorEntity d on (da.doctor = d) join PatientEntity p on (a.patient = p)  left join FamilyMemberEntity m on (a.member = m) where p.patientId=:patientId  and a.appointmentStatus IN ( 'COMPLETED', 'CANCELLED' )")
    public List<AppointmentEntity> getAppointmentsByPatientId(Long patientId);


    @Query("SELECT a from AppointmentEntity a join DoctorAvailabilityEntity da on (a.doctorAvailability = da) join DoctorEntity d on (da.doctor = d) join PatientEntity p on (a.patient = p)  left join FamilyMemberEntity m on (a.member = m) where p.patientId=:patientId and (:fromDate is Null or da.date>=:fromDate) and (:toDate is Null or da.date<=:toDate) and a.appointmentStatus IN ( 'COMPLETED', 'CANCELLED' )")
    public List<AppointmentEntity> getAppointmentsByPatientIdInDateRange(@Param("patientId") Long patientId,@Param("fromDate") LocalDate fromDate,@Param("toDate") LocalDate toDate);

    @Query("select p from PatientEntity p where p.user.userId = :userId")
    public Optional<PatientEntity> findByUserId(Long userId);

}
