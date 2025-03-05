package com.wipro.controller;

import com.wipro.dto.*;
import com.wipro.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/getAvailableAppointmentSlots/doctor/{docId}/date/{apmntDate}")
    public DoctorAvailabilityBookingDto getAvailableAppointmentSlots(@PathVariable Long docId, @PathVariable LocalDate apmntDate) {
        return appointmentService.getAvailableAppointmentSlots(docId, apmntDate);
    }

    @GetMapping("/isDoctorAvailable/doctor/{docId}/date/{apmntDate}")
    public Map<String, Boolean> isDoctorAvailableOnDate(@PathVariable Long docId, @PathVariable LocalDate apmntDate) {
        return appointmentService.isDoctorAvailableOnDate(docId, apmntDate);
    }

    @GetMapping("/getMembersSummary/user/{userId}")
    public List<MemberSummaryDto> getMembersSummary(@PathVariable Long userId) {
        return appointmentService.getMembersSummary(userId);
    }

    @PostMapping("/bookAppointment")
    public ResponseEntity<Void> bookAppointment(@RequestBody AppointmentDto appointmentDto) {
        return appointmentService.bookAppointment(appointmentDto);
    }

    @GetMapping("/getFamilyMembersAppointmentDetails/{userId}")
    public List<MembersAppointmentDto> getFamilyMembersAppointmentDetails(@PathVariable Long userId) {
        return appointmentService.getFamilyMembersAppointmentDetails(userId);
    }
    @PatchMapping("/cancelAppointment/{appointmentId}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@PathVariable Long appointmentId){
        return appointmentService.cancelAppointment(appointmentId);
    }

    @GetMapping("/getBetweenDates/{userId}/{fromDate}/{toDate}")
    public List<MembersAppointmentDto> getAppointmentsBetween(@PathVariable Long userId,@PathVariable LocalDate fromDate,@PathVariable LocalDate toDate) {
        return appointmentService.getAppointmentsBetween(userId,fromDate,toDate);
    }

}
