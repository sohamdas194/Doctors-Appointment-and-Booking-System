package com.wipro.service;

import com.wipro.constant.AppointmentSlot;
import com.wipro.constant.AppointmentStatus;
import com.wipro.dto.AppointmentSearchResultDto;
import com.wipro.dto.AppointmentStatusUpdateDto;
import com.wipro.dto.DoctorConsultationFeeUpdateDto;
import com.wipro.dto.DoctorSearchDto;
import com.wipro.entity.AppointmentEntity;
import com.wipro.exception.NoSuchAppointmentExistsException;
import com.wipro.repository.AppointmentRepository;
import com.wipro.repository.DoctorRepository;
import com.wipro.repositoryimpl.AppointmentRepositoryImpl;
import com.wipro.utility.MailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wipro.constant.MailSubjectAndBody.NOTIFICATION_MAIL_SUBJECT;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class AdminService {
    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private AppointmentRepositoryImpl appointmentRepoImpl;

    @Scheduled(cron = "0 59 23 * * *")
    private void updateAppointmentStatusAtEOD() {
        appointmentRepo.updateAppointmentStatusAtEOD(AppointmentStatus.NO_SHOW.name(), AppointmentStatus.SCHEDULED.name());
    }

    private Integer getYearsTillToday(LocalDate pastDate) {
        return Math.round((DAYS.between(pastDate, LocalDate.now())) / 365);
    }

    public ResponseEntity<Void> updateAppointmentStatus(AppointmentStatusUpdateDto statusUpdateDto) {
        appointmentRepo.updateAppointmentStatusByAppointmentId(statusUpdateDto.getAppointmentId(), statusUpdateDto.getAppointmentStatus());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<AppointmentSearchResultDto> searchAppointments(String name, String fromDate, String toDate, Boolean upcomingFlag) {
        LocalDate fromDt;
        LocalDate toDt;
        if(fromDate.equals("_") && toDate.equals("_")) {
            fromDt = null;
            toDt = null;
        } else if(fromDate.equals("_")) {
            fromDt = null;
            toDt = LocalDate.parse(toDate);
        } else if(toDate.equals("_")) {
            fromDt = LocalDate.parse(fromDate);
            toDt = null;
        } else {
            fromDt = LocalDate.parse(fromDate);
            toDt = LocalDate.parse(toDate);
        }

        return appointmentRepoImpl.getAllAppointmentsByNameAndDateRange(name, fromDt, toDt, upcomingFlag, AppointmentStatus.COMPLETED.name(), AppointmentStatus.NO_SHOW.name(), AppointmentStatus.CANCELLED.name()).stream()
                .map(appointment -> {
                    return new AppointmentSearchResultDto(
                            appointment.getAppointmentId(),
                            appointment.getDoctorAvailability().getDoctor().getFirstName() + " " +
                                    appointment.getDoctorAvailability().getDoctor().getLastName(),
                            appointment.getDoctorAvailability().getDoctor().getSpecialization(),
                            appointment.getMember()==null ? appointment.getPatient().getFirstName() + " " +
                                    appointment.getPatient().getLastName() :
                                    appointment.getMember().getFirstName() + " " +
                                            appointment.getMember().getLastName(),
                            appointment.getMember()==null ? appointment.getPatient().getGender() :
                                    appointment.getMember().getGender(),
                            appointment.getMember()==null ? getYearsTillToday(appointment.getPatient().getDob()) :
                                    getYearsTillToday(appointment.getMember().getDob()),
                            appointment.getDoctorAvailability().getDate(),
                            AppointmentSlot.getAppointmentSlotByValues(appointment.getDoctorAvailability().getShift(),appointment.getAppointmentSlotNumber()).get().getSlotTiming(),
                            appointment.getAppointmentStatus(),
                            appointment.getPatientComment(),
                            appointment.getDoctorComment()
                    );
                }).toList();
    }

    public ResponseEntity<Void> updateDoctorConsultationFee(DoctorConsultationFeeUpdateDto doctorConsultationFeeDto) {
        docRepo.updateConsultationFeeByDoctorId(doctorConsultationFeeDto.getDoctorId() ,doctorConsultationFeeDto.getConsultationFee());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public void notifyDoctorAndPatiendAboutAppointment(Long appointmentId) {
        Optional<AppointmentEntity> appointmentOp = appointmentRepo.findById(appointmentId);

        if(appointmentOp.isEmpty()) {
            throw new NoSuchAppointmentExistsException();
        }

        sendNotificationMails(appointmentOp.get());

    }

    private void sendNotificationMails(AppointmentEntity appointment) {

        if(appointment.getMember() == null) {

            mailSenderUtil.SendMail(
                    appointment.getDoctorAvailability().getDoctor().getUserEntity().getEmail(),
                    NOTIFICATION_MAIL_SUBJECT,
                    getNotifyDoctorMailBody(
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getPatient().getFirstName(),
                            appointment.getPatient().getLastName(),
                            appointment.getPatient().getGender(),
                            appointment.getPatient().getDob(),
                            appointment.getDoctorAvailability().getDate(),
                            appointment.getAppointmentStatus()
                    ));

            mailSenderUtil.SendMail(
                    appointment.getPatient().getUser().getEmail(),
                    NOTIFICATION_MAIL_SUBJECT,
                    getNotifyPatientMailBody(
                            appointment.getPatient().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getLastName(),
                            appointment.getDoctorAvailability().getDoctor().getSpecialization(),
                            appointment.getDoctorAvailability().getDate(),
                            appointment.getAppointmentStatus()
                    ));
        } else {
            mailSenderUtil.SendMail(
                    appointment.getDoctorAvailability().getDoctor().getUserEntity().getEmail(),
                    NOTIFICATION_MAIL_SUBJECT,
                    getNotifyDoctorMailBody(
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getMember().getFirstName(),
                            appointment.getMember().getLastName(),
                            appointment.getMember().getGender(),
                            appointment.getMember().getDob(),
                            appointment.getDoctorAvailability().getDate(),
                            appointment.getAppointmentStatus()
                    ));

            mailSenderUtil.SendMail(
                    appointment.getPatient().getUser().getEmail(),
                    NOTIFICATION_MAIL_SUBJECT,
                    getNotifyPatientFamilyMailBody(
                            appointment.getPatient().getFirstName(),
                            appointment.getMember().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getLastName(),
                            appointment.getDoctorAvailability().getDoctor().getSpecialization(),
                            appointment.getDoctorAvailability().getDate(),
                            appointment.getAppointmentStatus()
                    ));
        }

    }

    private static String getNotifyPatientMailBody(String patientFirstName, String doctorFirstName, String doctorLastName, String doctorSpecialization, LocalDate appointmentDate, String appointmentStatus) {
        return "Hi " + patientFirstName + ",\n\n" +
                "Appointment status with doctor " + doctorFirstName + " " + doctorLastName +
                " (" + doctorSpecialization +
                ") on " + appointmentDate + ", has changed to " + appointmentStatus + ".\n\n" +
                "Thanks,\n" +
                "DABS Team";
    }

    private static String getNotifyPatientFamilyMailBody(String patientFirstName, String memberFirstName, String doctorFirstName, String doctorLastName, String doctorSpecialization, LocalDate appointmentDate, String appointmentStatus) {
        return "Hi " + patientFirstName + ",\n\n" +
                "Your family member " +
                memberFirstName +
                "'s appointment status with Dr. " + doctorFirstName + " " + doctorLastName +
                " (" + doctorSpecialization +
                ") on " + appointmentDate + ", has changed to " + appointmentStatus + ".\n\n" +
                "Thanks,\n" +
                "DABS Team";
    }

    private static String getNotifyDoctorMailBody(String doctorFirstName, String patientFirstName, String patientLastName, String patientGender, LocalDate patientDOB, LocalDate appointmentDate, String appointmentStatus) {
        return "Hi Dr. " + doctorFirstName + ",\n\n" +
                "Appointment status for patient " + patientFirstName + " " + patientLastName +
                " (" + patientGender.charAt(0) + Math.round(ChronoUnit.DAYS.between(patientDOB, LocalDate.now())/365) +
                ") on " + appointmentDate + ", has changed to " + appointmentStatus + ".\n\n" +
                "Regards,\n" +
                "DABS Team";
    }

    public List<DoctorSearchDto> searchDoctors(String name) {
        return docRepo.findAllDoctorsByName(name).stream()
                .map(doc -> {
                    return new DoctorSearchDto(
                            doc.getDoctorId(),
                            doc.getFirstName() + " " + doc.getLastName(),
                            doc.getSpecialization(),
                            getYearsTillToday(doc.getConsultingFrom()),
                            doc.getCity(),
                            doc.getPinCode(),
                            doc.getConsultationFee());
                }).collect(Collectors.toList());
    }
}