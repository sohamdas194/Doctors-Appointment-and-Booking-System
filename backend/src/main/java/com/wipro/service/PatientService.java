package com.wipro.service;

import com.wipro.dto.AppointmentHistoryDto;
import com.wipro.dto.PatientDto;
import com.wipro.entity.AppointmentEntity;
import com.wipro.entity.PatientEntity;
import com.wipro.model.Response;
import com.wipro.repository.PatientRepository;
import com.wipro.repository.UserRepository;
import com.wipro.repositoryimpl.PatientRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    PatientRepositoryImpl patientRepository;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private UserRepository userRepo;

    public Response saveUserPatient(PatientDto patientDto) {
        return patientRepository.saveUserPatient(patientDto);
    }

    public PatientDto getPatientById(Long userId) {
        return patientRepository.getPatientById(userId);
    }

    public ResponseEntity<PatientDto> updatePatient(PatientDto patientDto, Long userId) {
        return patientRepository.updatePatient(patientDto, userId);
    }

    public List<AppointmentHistoryDto> getPatientAppointmentHistoryDateRange(Long userId, LocalDate fromDate, LocalDate toDate) {
        PatientEntity patient = patientRepo.findByUserId(userId).orElse(null);
        if(patient == null) return null;

        List<AppointmentEntity> patientAppointments = patientRepo.getAppointmentsByPatientIdInDateRange(patient.getPatientId(), fromDate, toDate);

        return patientAppointments.stream()
                .map(appointment -> {
                    return new AppointmentHistoryDto(
                            appointment.getAppointmentId(),
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getLastName(),
                            appointment.getPatient().getFirstName(),
                            appointment.getPatient().getLastName(),
                            appointment.getMember() == null ? null :  appointment.getMember().getFirstName(),
                            appointment.getMember() == null ? null :  appointment.getMember().getLastName(),
                            appointment.getAppointmentSlotNumber(),
                            appointment.getDoctorAvailability().getDate(),
                            appointment.getAppointmentStatus(),
                            appointment.getDoctorComment(),
                            appointment.getPatientComment()

                    );
                }).collect(Collectors.toList());
    }

    public List<AppointmentHistoryDto> getPatientAppointmentHistory(Long userId) {
        System.out.println(userId);
        PatientEntity patient = patientRepo.findByUserId(userId).orElse(null);
        if(patient == null) return null;

        List<AppointmentEntity> patientAppointments = patientRepo.getAppointmentsByPatientId(patient.getPatientId());

        return patientAppointments.stream()
                .map(appointment -> {
                    return new AppointmentHistoryDto(
                            appointment.getAppointmentId(),
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getLastName(),
                            appointment.getPatient().getFirstName(),
                            appointment.getPatient().getLastName(),
                            appointment.getMember() == null ? null :  appointment.getMember().getFirstName(),
                            appointment.getMember() == null ? null :  appointment.getMember().getLastName(),
                            appointment.getAppointmentSlotNumber(),
                            appointment.getDoctorAvailability().getDate(),
                            appointment.getAppointmentStatus(),
                            appointment.getDoctorComment(),
                            appointment.getPatientComment()

                    );
                }).collect(Collectors.toList());
    }


}
