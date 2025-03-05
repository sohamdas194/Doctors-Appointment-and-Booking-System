package com.wipro.repository;

import com.wipro.entity.DoctorAvailabilityEntity;
import com.wipro.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailabilityEntity, Long> {
    @Query("select count(da)>0 from DoctorAvailabilityEntity da where da.doctor = :doc and da.date = :apmntDate and da.status = true")
    public Boolean isDoctorAvailableOnDate(DoctorEntity doc, LocalDate apmntDate);

    @Query("select da from DoctorAvailabilityEntity da where da.doctor = :doc and da.date = :apmntDate and da.status = true")
    public Optional<DoctorAvailabilityEntity> getDocOnDate(DoctorEntity doc, LocalDate apmntDate);

    @Query("select a from DoctorAvailabilityEntity a where a.date >= :currDate and a.doctor = :doc")
    public List<DoctorAvailabilityEntity> getAllValidAvailableDates(LocalDate currDate, DoctorEntity doc);

    @Query("select da from DoctorAvailabilityEntity da where da.doctor=:doctor AND da.date=:date")
    Optional<DoctorAvailabilityEntity> findByDoctorIdAndDate(@Param("doctor") DoctorEntity doctor, @Param("date") LocalDate date);

    @Query("select a from DoctorAvailabilityEntity a where a.doctor=:doc")
    public List<DoctorAvailabilityEntity> getAllDatesFromDB(DoctorEntity doc);
}
