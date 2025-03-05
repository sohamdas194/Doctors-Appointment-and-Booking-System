package com.wipro.controller;

import com.wipro.dto.*;
import com.wipro.service.ApplicationService;
import com.wipro.service.AppointmentService;
import com.wipro.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import com.wipro.constant.ResponseStatus;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ApplicationService applicationService;


    @GetMapping("/")
    public String greetDoctors() {
        return "Hello Doctors";
    }

    @GetMapping("/{email}")
    public boolean doesEmailExist1(@PathVariable("email") String email) {
        return applicationService.doesEmailExist(email);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> saveDoctorDetails(@RequestBody DoctorDto doctorDto) {
        return doctorService.saveDoctor(doctorDto);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<DoctorDto> getDoctorByID(@PathVariable("userId") Long userId) {
        return doctorService.getDoctorByID(userId);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<DoctorDto> updateDoctorByID(@PathVariable("userId") Long userId, @RequestBody DoctorDto doctorDto) {
        return doctorService.updateDoctorByID(userId, doctorDto);
    }

    @GetMapping("/specializations")
    public ResponseEntity<List<String>> getAllSpecialization() {
        return doctorService.listAllSpecializations();
    }

    @PostMapping("/doctorAvailable/{id}")
    public ResponseEntity<List<DoctorAvailabilityDto>> saveDoctorDates(@PathVariable("id") Long userId, @RequestBody List<DoctorAvailabilityDto> doctorAvailable) {
        return doctorService.saveAllDoctorDates(userId, doctorAvailable);
    }

    @GetMapping("/getAllDates/{id}")
    public ResponseEntity<List<DoctorAvailabilityDto>> getAllDates(@PathVariable("id") Long userId) {
        return doctorService.getAllDoctorDates(userId);
    }

    @PutMapping("/updateDate/{userId}/{date}")
    public ResponseEntity<DoctorAvailabilityDto> updatedDoctorDate(@PathVariable("userId") Long userId, @PathVariable("date") LocalDate date, @RequestBody DoctorAvailabilityDto updateAvailability) {
        return doctorService.updateDate(userId, date, updateAvailability);
    }

    @GetMapping("/getAllDoctorDates/{id}")
    public ResponseEntity<List<DoctorAvailabilityDto>> getAllDoctorDates(@PathVariable("id") Long userId) {
        return doctorService.getAllDoctorDatesFromDB(userId);
    }

    @GetMapping("/searchDoctors/name/{docName}/specialization/{spclzn}/experience/{exp}/city/{city}/pinCode/{pinCode}/date/{apmntDate}")
    public List<DoctorSearchDto> getAllFilteredDoctors(@PathVariable String docName, @PathVariable String spclzn, @PathVariable Integer exp, @PathVariable String city, @PathVariable String pinCode, @PathVariable String apmntDate) {
        return appointmentService.getAllFilteredDoctors(docName, spclzn, exp, city, pinCode, apmntDate);
    }

    @GetMapping("/completed/consultations/{userId}")
    public List<DoctorConsultationHistoryDto> getAllConsultationsCompleted(@PathVariable Long userId) {
        return doctorService.getAllConsultationsPreviously(userId);
    }

    @GetMapping("/consultations/between/{userId}")
    public List<DoctorConsultationHistoryDto> getAllConsultationsWithDateRangeAndPatientName(@PathVariable("userId") Long userId,
                                                                                             @RequestParam(required = false) LocalDate fromDate,
                                                                                             @RequestParam(required = false) LocalDate toDate,
                                                                                             @RequestParam(required = false) String patientName){
        return doctorService.getAllDoctorConsultationsWithInDateRangeAndPatientName(userId, fromDate, toDate, patientName);
    }
    @GetMapping("/dashboard/allAppointments/{userId}")
    public List<DoctorDashboardDto> getAllDoctorAppointments(@PathVariable Long userId){

        return doctorService.getAllDoctorUpcomingAppointments(userId);

    }

    @GetMapping("/dashboard/currentDate/{userId}")
    public  List<DoctorDashboardDto> getDoctorDashboardCurrentDate(@PathVariable Long userId){
        return  doctorService.getAllDoctorCurrentDateAppointments(userId);
    }

    @GetMapping("/dashboard/nextDate/{userId}")
    public  List<DoctorDashboardDto> getDoctorDashboardNextDate(@PathVariable Long userId){
        return  doctorService.getAllDoctorNextDateAppointments(userId);
    }

    @GetMapping("/dashboard/searchBetweenDates/{userId}/{fromDate}/{toDate}")
    public  List<DoctorDashboardDto> searchBetweenDates(@PathVariable Long userId, @PathVariable LocalDate fromDate, @PathVariable LocalDate toDate){
        return  doctorService.getSearchBetweenDateAppointments(userId, fromDate, toDate);
    }

    @PutMapping("/dashboard/updateDoctorComment/{appointmentId}")
    public  ResponseEntity<Map<String,Object>> updateDoctorComment(@PathVariable("appointmentId") Long appointmentId,@RequestBody AppointmentDto appointmentDto){
        doctorService.updateDoctorComment(appointmentId,appointmentDto);
        return ResponseEntity.ok(Collections.singletonMap(ResponseStatus.SUCCESS.name(),true));
    }
}
