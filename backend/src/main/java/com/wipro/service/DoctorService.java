package com.wipro.service;
import com.wipro.constant.ResponseStatus;
import com.wipro.dto.*;
import com.wipro.entity.DoctorEntity;
import com.wipro.exception.NoSuchDoctorAvailabilityExistsException;
import com.wipro.repository.DoctorRepository;
import com.wipro.repository.UserRepository;
import com.wipro.repositoryimpl.DoctorRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class DoctorService {

    private DoctorRepository doctorRepository;

    private UserRepository userRepository;

    private DoctorRepositoryImpl doctorRepositoryImpl;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository, DoctorRepositoryImpl doctorRepositoryImpl) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.doctorRepositoryImpl = doctorRepositoryImpl;
    }


    public ResponseEntity<Map<String, Object>> saveDoctor(DoctorDto doctorDto) {
        LocalDate date = LocalDate.now();
        if (doctorDto.getDateOfBirth().isAfter(date)) {
            return new ResponseEntity<>(Collections.singletonMap(ResponseStatus.FAILED.name(), false), HttpStatus.CONFLICT);
        } else if (doctorDto.getConsultingFrom().isAfter(date)) {
            return new ResponseEntity<>(Collections.singletonMap(ResponseStatus.FAILED.name(), false), HttpStatus.CONFLICT);
        } else if (doctorDto.getConsultingFrom().isBefore(doctorDto.getDateOfBirth())) {
            return new ResponseEntity<>(Collections.singletonMap(ResponseStatus.FAILED.name(), false), HttpStatus.CONFLICT);
        }

        return doctorRepositoryImpl.saveDoctorsImpl(doctorDto);
    }

    public ResponseEntity<DoctorDto> getDoctorByID(Long userId) {
        return doctorRepositoryImpl.getDoctorByID(userId);
    }

    public ResponseEntity<DoctorDto> updateDoctorByID(Long userId, DoctorDto doctorDto) {
        return doctorRepositoryImpl.updateDoctorByID(userId, doctorDto);
    }

    public ResponseEntity<List<String>> listAllSpecializations() {
        List<String> specializations=new ArrayList<>();
        specializations.add("Orthopedics");
        specializations.add("Endocrinologist");
        specializations.add("Obstetrics and Gynecology");
        specializations.add("Dermatology");
        specializations.add("Pediatrics");
        specializations.add("Radiology");
        specializations.add("General Surgery");
        specializations.add("Ophthalmology");
        specializations.add("Cardiologist");
        specializations.add("Immunologist");
        specializations.add("Anesthesiologist");
        specializations.add("Pathology");
        specializations.add("ENT");
        specializations.add("Family Physicians");
        specializations.add("Gastroenterologist");
        specializations.add("Neurologist");
        specializations.add("Psychiatrist");
        return new ResponseEntity<>(specializations,HttpStatus.OK);
    }

    public ResponseEntity<List<DoctorAvailabilityDto>> saveAllDoctorDates(Long userId, List<DoctorAvailabilityDto> doctorAvailable) {
        Optional<DoctorEntity> doctor = doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if (doctor.isPresent()) {
            //Long doctorId=doctor.get().getDoctorId();
            return doctorRepositoryImpl.saveAllDoctorDates(doctor.get(), doctorAvailable);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<DoctorAvailabilityDto>> getAllDoctorDates(Long userId) {
        Optional<DoctorEntity> doctor = doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if (doctor.isPresent()) {
            return doctorRepositoryImpl.getAllDates(doctor.get());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<DoctorAvailabilityDto>> getAllDoctorDatesFromDB(Long userId) {
        Optional<DoctorEntity> doctor = doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if (doctor.isPresent()) {
            return doctorRepositoryImpl.getAllDoctorDates(doctor.get());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<DoctorAvailabilityDto> updateDate(Long userId, LocalDate date, DoctorAvailabilityDto doctorAvailabilityDto) {
        Optional<DoctorEntity> doctor = doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if (doctor.isPresent()) {
            return doctorRepositoryImpl.updateDoctorAvailabilityByDateAndShift(doctor.get(), date, doctorAvailabilityDto);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public List<DoctorConsultationHistoryDto> getAllConsultationsPreviously(Long userId){
        Optional<DoctorEntity> doctor=doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if(doctor.isPresent()) {
            return doctorRepositoryImpl.getAllDoctorConsultations(doctor.get());
        }
        throw new NoSuchDoctorAvailabilityExistsException();
    }

    public  List<DoctorConsultationHistoryDto> getAllDoctorConsultationsWithInDateRangeAndPatientName(Long userId,LocalDate fromDate, LocalDate toDate, String patientName){
        Optional<DoctorEntity> doctor=doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if(doctor.isPresent()){
            return doctorRepositoryImpl.getAllConsultationsInDateRangeAndPatientName(doctor.get(),fromDate,toDate,patientName);
        }
        throw new  NoSuchDoctorAvailabilityExistsException();
    }
    public List<DoctorDashboardDto> getAllDoctorUpcomingAppointments(Long userId){
        Optional<DoctorEntity> doctor=doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if(doctor.isPresent()){
            return doctorRepositoryImpl.getAllDoctorsFutureAppointments(doctor.get());
        }
        throw new NullPointerException();
    }

    public List<DoctorDashboardDto> getAllDoctorNextDateAppointments(Long userId){
        Optional<DoctorEntity> doctor=doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if(doctor.isPresent()){
            return doctorRepositoryImpl.getDoctorAppointmentByDate(doctor.get(), LocalDate.now().plusDays(1));        }
        throw new NullPointerException();
    }

    public List<DoctorDashboardDto> getAllDoctorCurrentDateAppointments(Long userId){
        Optional<DoctorEntity> doctor=doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if(doctor.isPresent()){
            return doctorRepositoryImpl.getDoctorAppointmentByDate(doctor.get(), LocalDate.now());
        }
        throw new NullPointerException();
    }

    public ResponseEntity<AppointmentDto> updateDoctorComment(Long appointmentId, AppointmentDto appointmentDto){
        return doctorRepositoryImpl.updateDoctorComment(appointmentId,appointmentDto);
    }

    public List<DoctorDashboardDto> getSearchBetweenDateAppointments(Long userId, LocalDate fromDate, LocalDate toDate) {
        Optional<DoctorEntity> doctor=doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if(doctor.isPresent()){
            return doctorRepositoryImpl.getAppointmentsBetweenDates(doctor.get(),fromDate,toDate);
        }
        throw new NullPointerException();
    }
}
