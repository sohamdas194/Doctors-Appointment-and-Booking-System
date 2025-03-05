package com.wipro.repositoryimpl;

import com.wipro.entity.AppointmentEntity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class AppointmentRepositoryImpl {
    @Autowired
    private EntityManager entityManager;

    public List<AppointmentEntity> getAllAppointmentsByNameAndDateRange(String name, LocalDate fromDate, LocalDate toDate, Boolean upcomingFlag, String completedStatus, String noShowStatus, String cancelledStatus) {
        String query = "select a from AppointmentEntity a " +
                "join a.doctorAvailability da " +
                "join da.doctor d " +
                "join a.patient p " +
                "left join a.member m " +
                "where " +
                "(a.doctorAvailability.doctor.firstName||' '||a.doctorAvailability.doctor.lastName like '%'||:name||'%' " +
                "or (a.member is null and a.patient.firstName||' '||a.patient.lastName like '%'||:name||'%')" +
                "or a.member.firstName||' '||a.member.lastName like '%'||:name||'%') " +
                "and ((:fromDate is null or a.doctorAvailability.date >= :fromDate) and (:toDate is null or a.doctorAvailability.date <= :toDate)) ";

        if(upcomingFlag) {
            query += "and (a.appointmentStatus not in (:completedStatus, :noShowStatus, :cancelledStatus)) ";

            return entityManager.createQuery(query, AppointmentEntity.class)
                    .setParameter("name", name)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("completedStatus", completedStatus)
                    .setParameter("noShowStatus", noShowStatus)
                    .setParameter("cancelledStatus", cancelledStatus)
                    .getResultList();

        } else {
            query += "order by da.date DESC";

            return entityManager.createQuery(query, AppointmentEntity.class)
                    .setParameter("name", name)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .getResultList();
        }
    }
}
