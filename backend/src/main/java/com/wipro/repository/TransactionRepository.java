package com.wipro.repository;

import com.wipro.entity.AppointmentEntity;
import com.wipro.entity.TransactionEntity;
import com.wipro.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    public AppointmentEntity findByAppointmentId(Long Id);
}
