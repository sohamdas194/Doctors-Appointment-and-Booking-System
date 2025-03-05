package com.wipro.service;

import com.wipro.constant.AppointmentStatus;
import com.wipro.dto.TransactionDto;
import com.wipro.entity.*;
import com.wipro.repository.AppointmentRepository;
import com.wipro.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    TransactionRepository transactionRepository;
    AppointmentRepository appointmentRepository;

    @Autowired
    public TransactionService( TransactionRepository transactionRepository, AppointmentRepository appointmentRepository){
        this.transactionRepository=transactionRepository;
        this.appointmentRepository=appointmentRepository;
    }

//    public ResponseEntity<Map<String, Object>> createTransaction(Long appointmentId) {
//        AppointmentEntity appointment =getAppointmentDetail(appointmentId);
//        TransactionEntity transaction= new TransactionEntity();
//        transaction.setAppointmentId(appointmentId);
//        transaction.setConsultationFee(appointment.getDoctorAvailability().getDoctor().getConsultationFee());
//        transaction.setPatientId(appointment.getPatient().getPatientId());
//        if( null!=appointment.getMember() && null!=appointment.getMember().getMemberId())
//            transaction.setMemberId(appointment.getMember().getMemberId());
//        transaction.setDoctorId(appointment.getDoctorAvailability().getDoctor().getDoctorId());
//        transaction.setTransactionDateTime(LocalDateTime.now());
//        transactionRepository.save(transaction);
//        return new ResponseEntity<>(Collections.singletonMap("success", true), HttpStatus.CREATED);
//    }

    public ResponseEntity<TransactionDto> createTransaction(Long appointmentId) {
        AppointmentEntity appointment =getAppointmentDetail(appointmentId);
        TransactionEntity transaction= new TransactionEntity();
        transaction.setAppointmentId(appointmentId);
        transaction.setConsultationFee(appointment.getDoctorAvailability().getDoctor().getConsultationFee());
        transaction.setPatientId(appointment.getPatient().getPatientId());
        if( null!=appointment.getMember() && null!=appointment.getMember().getMemberId())
            transaction.setMemberId(appointment.getMember().getMemberId());
        transaction.setDoctorId(appointment.getDoctorAvailability().getDoctor().getDoctorId());
        transaction.setTransactionDateTime(LocalDateTime.now());

        transaction.setStatus(AppointmentStatus.ON_GOING.name());

        TransactionEntity transaction1 = transactionRepository.save(transaction);

        return new ResponseEntity(transaction1, HttpStatus.CREATED);
    }
     private AppointmentEntity getAppointmentDetail(Long appointmentId){
        return appointmentRepository.findByAppointmentId(appointmentId);
    }

    public List<TransactionDto> getTransactions() {
        return transactionRepository.findAll().stream().map(this::convertToTransactionDto).collect(Collectors.toList());
    }

    private TransactionDto convertToTransactionDto(TransactionEntity transactionEntity) {
        return new TransactionDto(transactionEntity.getAppointmentId(), transactionEntity.getTransactionId(), transactionEntity.getConsultationFee(), transactionEntity.getPatientId(), transactionEntity.getMemberId(), transactionEntity.getDoctorId(), transactionEntity.getStatus());
    }

    public ResponseEntity<Void> closeTransaction(Long transactionId) {
        TransactionEntity transaction = transactionRepository.findById(transactionId).orElse(null);
        if(transaction == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        transaction.setStatus(AppointmentStatus.COMPLETED.name());

        appointmentRepository.updateAppointmentStatusByAppointmentId(transaction.getAppointmentId(), AppointmentStatus.COMPLETED.name());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
