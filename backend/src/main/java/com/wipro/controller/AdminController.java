package com.wipro.controller;

import com.wipro.dto.AppointmentSearchResultDto;
import com.wipro.dto.AppointmentStatusUpdateDto;
import com.wipro.dto.DoctorConsultationFeeUpdateDto;
import com.wipro.dto.DoctorSearchDto;
import com.wipro.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PatchMapping("/updateDoctorConsultationFee/")
    public ResponseEntity<Void> updateDoctorConsultationFee(@RequestBody DoctorConsultationFeeUpdateDto docConsultFeeDto) {
        return adminService.updateDoctorConsultationFee(docConsultFeeDto);
    }

    @PatchMapping("/updateAppointmentStatus/")
    public ResponseEntity<Void> updateAppointmentStatus(@RequestBody AppointmentStatusUpdateDto statusUpdateDto) {
        return adminService.updateAppointmentStatus(statusUpdateDto);
    }

    @GetMapping("/notifyDoctorAndPatient/appointment/{appointmentId}")
    public void sendNotificationToDoctorAndPatient(@PathVariable Long appointmentId) {
        adminService.notifyDoctorAndPatiendAboutAppointment(appointmentId);
    }

    @GetMapping("/searchAppointments/name/{name}/fromDate/{fromDate}/toDate/{toDate}/upcoming/{upcomingFlag}")
    public List<AppointmentSearchResultDto> searchAppointments(@PathVariable String name, @PathVariable String fromDate, @PathVariable String toDate, @PathVariable Boolean upcomingFlag) {
        System.out.println(upcomingFlag);
        return adminService.searchAppointments(name, fromDate, toDate, upcomingFlag);
    }

    @GetMapping("/searchDoctors/name/{name}")
    public List<DoctorSearchDto> searchDoctors(@PathVariable String name) {
        return adminService.searchDoctors(name);
    }


}
