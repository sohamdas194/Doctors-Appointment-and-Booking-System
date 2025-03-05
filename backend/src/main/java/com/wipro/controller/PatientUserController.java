package com.wipro.controller;

import com.wipro.constant.ResponseStatus;
import com.wipro.dto.AppointmentHistoryDto;
import com.wipro.dto.PatientDto;
import com.wipro.repositoryimpl.PatientRepositoryImpl;
import com.wipro.service.ApplicationService;
import com.wipro.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@Validated
@CrossOrigin(origins = "*")
@RequestMapping("/patient")
public class PatientUserController {

    @Autowired
    PatientService patientService;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    PatientRepositoryImpl patientRepositoryImpl;

    @PostMapping("/save")
    public ResponseEntity<Map<String,Object>> savePatients(@RequestBody @Valid PatientDto patientDto){
        patientService.saveUserPatient(patientDto) ;
        return ResponseEntity.ok(Collections.singletonMap(ResponseStatus.SUCCESS.name(),true));
    }


    @GetMapping("/getPatient/{userId}")
    public ResponseEntity<PatientDto>findPatientById(@PathVariable Long userId){
        PatientDto patientEntity = patientService.getPatientById(userId);
        return new ResponseEntity<>(patientEntity, HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Map<String,Object>> updatePatient(@RequestBody @Valid PatientDto patientDto, @PathVariable Long userId)
    {
        patientRepositoryImpl.updatePatient(patientDto,userId);
        return ResponseEntity.ok(Collections.singletonMap(ResponseStatus.SUCCESS.name(),true));
    }

    @GetMapping("/checkEmail/{email}")
    public Boolean doesEmailExist(@PathVariable("email") String email ){
        return applicationService.doesEmailExist(email);
    }

    @GetMapping("/{userId}/appointments/history")

    public List<AppointmentHistoryDto> getPatientAppointmentHistory(@PathVariable Long userId )
    {
        return patientService.getPatientAppointmentHistory(userId);
    }

    @GetMapping("/appointments/history/{userId}/{fromDate}/{toDate}")

    public List<AppointmentHistoryDto> getPatientAppointmentHistoryInDateRange(@PathVariable("userId") Long userId, @PathVariable("fromDate") LocalDate fromDate, @PathVariable ("toDate") LocalDate toDate )
    {
        return patientService.getPatientAppointmentHistoryDateRange(userId, fromDate, toDate);
    }
}
