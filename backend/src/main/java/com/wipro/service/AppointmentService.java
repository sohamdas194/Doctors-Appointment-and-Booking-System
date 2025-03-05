package com.wipro.service;

import com.wipro.constant.AppointmentSlot;
import com.wipro.constant.AppointmentStatus;
import com.wipro.dto.*;
import com.wipro.entity.*;
import com.wipro.exception.*;
import com.wipro.repository.*;
import com.wipro.repositoryimpl.DoctorRepositoryImpl;
import com.wipro.utility.MailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.wipro.constant.AppointmentShiftTimings.*;
import static com.wipro.constant.MailSubjectAndBody.APPOINTMENT_SCHEDULED_SUBJECT;
import static com.wipro.constant.MailSubjectAndBody.NOTIFICATION_MAIL_SUBJECT;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class AppointmentService {

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository docRepoJpa;

    @Autowired
    private DoctorRepositoryImpl docRepoImpl;

    @Autowired
    private DoctorAvailabilityRepository docAvailabilityRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private FamilyMemberRepository memberRepo;

    public List<DoctorSearchDto> getAllFilteredDoctors(String docName, String spclzn, Integer exp, String city, String pinCode, String apmntDate) {
        List<DoctorEntity> allFilteredDoctors = docRepoImpl.getAllFilteredDoctors(docName, spclzn, exp, city, pinCode, apmntDate, (apmntDate.equals("_") ? true : false));

        return allFilteredDoctors.stream()
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

    private Integer getYearsTillToday(LocalDate pastDate) {
        return Math.round((DAYS.between(pastDate, LocalDate.now())) / 365);
    }

    public DoctorAvailabilityBookingDto getAvailableAppointmentSlots(Long docId, LocalDate apmntDate) {
        DoctorEntity doc = getDoctorByDoctorId(docId);

        Optional<DoctorAvailabilityEntity> docAvailabilityOp = docAvailabilityRepo.getDocOnDate(doc, apmntDate);
        if (docAvailabilityOp.isEmpty()) {
            throw new NoSuchDoctorAvailabilityExistsException();
        }

        List<Integer> usedSlots = appointmentRepo.getUsedAppointmentSlotsByDocAndDate(doc, apmntDate, AppointmentStatus.CANCELLED.name());

        if (usedSlots.size() >= TOTAL_NUMBER_OF_SLOTS) {
            throw new NoFreeAppointmentSlotExistsException();
        }

        Boolean disableValidSlotChk = true;
        DoctorAvailabilityEntity docAvlblty = docAvailabilityOp.get();
        List<Integer> validSlots = new ArrayList<>();
        if(docAvlblty.getDate().isEqual(LocalDate.now())) {
            validSlots = getValidAppointmentSlotNumbersByDoctorShift(docAvlblty.getShift());
            if(validSlots.isEmpty()) throw new NoFreeAppointmentSlotExistsException("No slots available for today!!");
            disableValidSlotChk = false;
        }

        List<Integer> emptySlots = new ArrayList<>();
        for (Integer i = 1; i <= TOTAL_NUMBER_OF_SLOTS; i++) {
            if (!usedSlots.contains(i) && (disableValidSlotChk || validSlots.contains(i))) {
                emptySlots.add(i);
            }
        }

        if(emptySlots.isEmpty()) throw new NoFreeAppointmentSlotExistsException();

        return new DoctorAvailabilityBookingDto(docAvlblty.getId(), docAvlblty.getShift(), emptySlots);
    }

    private List<Integer> getValidAppointmentTimeSlotNumbersInTimeRange(LocalTime start, LocalTime end, int slotDurationInMinutes) {
        if(start.isAfter(end)) return null;

        LocalTime now = LocalTime.now();

        List<Integer> validSlots = new ArrayList<>();
        Integer slotIndex = 1;
        for(LocalTime i = start; i.isBefore(end); i=i.plusMinutes(slotDurationInMinutes), slotIndex++) {
            if(i.isAfter(now)) validSlots.add(slotIndex);
        }

        return validSlots;
    }

    private List<Integer> getValidAppointmentSlotNumbersByDoctorShift(Integer docShift){

        if(docShift.equals(1)) {
            return getValidAppointmentTimeSlotNumbersInTimeRange(FIRST_SHIFT_START, FIRST_SHIFT_END, FIRST_SHIFT_SLOT_DURATION_IN_MINUTES);
        }

        if(docShift.equals(2)) {
            return getValidAppointmentTimeSlotNumbersInTimeRange(SECOND_SHIFT_START, SECOND_SHIFT_END, SECOND_SHIFT_SLOT_DURATION_IN_MINUTES);
        }

        return new ArrayList<Integer>();
    }

    public Map<String, Boolean> isDoctorAvailableOnDate(Long docId, LocalDate date) {
        Optional<DoctorEntity> docOp = docRepoJpa.findById(docId);
        if (docOp.isPresent()) {
            return Collections.singletonMap("isDoctorAvailable", docAvailabilityRepo.isDoctorAvailableOnDate(docOp.get(), date));
        }

        throw new NoSuchDoctorExistsException();
    }

    public List<MemberSummaryDto> getMembersSummary(Long userId) {
        List<FamilyMemberEntity> members = memberRepo.findAllByPatient(getPatientByUserId(userId));

        return members.stream()
                .map(member -> {
                    return new MemberSummaryDto(
                            member.getMemberId(),
                            member.getFirstName(),
                            member.getGender(),
                            getYearsTillToday(member.getDob())
                    );
                }).collect(Collectors.toList());
    }

    public ResponseEntity<Void> bookAppointment(AppointmentDto appointmentDto) {
        AppointmentEntity appointment = new AppointmentEntity();

        PatientEntity patient = getPatientByUserId(appointmentDto.getUserId());
        appointment.setPatient(patient);

        if(appointmentDto.getMemberId() != null) {
            patient.getMembers().stream()
                    .filter(member -> member.getMemberId().equals(appointmentDto.getMemberId()))
                    .findFirst().ifPresentOrElse(member -> {
                        appointment.setMember(member);
                    }, () -> {
                        throw new NoSuchFamilyMemberExistsException();
                    });
        }

        docAvailabilityRepo.findById(appointmentDto.getDoctorAvailabilityId())
                .ifPresentOrElse(doctorAvailability -> {
                    if (doctorAvailability.getDate().isBefore(LocalDate.now())) {
                        throw new NotValidAppointmentSchedulingDetailsException("Scheduling date can't be in the past!!");
                    }
                    appointment.setDoctorAvailability(doctorAvailability);
                }, () -> {
                    throw new NoSuchDoctorAvailabilityExistsException();
                });

        Integer apmntSlotNum = appointmentDto.getAppointmentSlot();
        if (apmntSlotNum > TOTAL_NUMBER_OF_SLOTS || apmntSlotNum < 1) {
            throw new NotValidAppointmentSchedulingDetailsException("Invalid appointment slot!!");
        }
        if (getAvailableAppointmentSlots(
                appointment.getDoctorAvailability().getDoctor().getDoctorId(),
                appointment.getDoctorAvailability().getDate()).getAvailableSlots().contains(apmntSlotNum)) {
            appointment.setAppointmentSlotNumber(appointmentDto.getAppointmentSlot());
        } else {
            throw new NotValidAppointmentSchedulingDetailsException("Appointment slot not available anymore!!");
        }


        String patientComments = appointmentDto.getPatientComment();
        if (patientComments == null || patientComments.isBlank()) {
            throw new NotValidAppointmentSchedulingDetailsException("Patient comments cannot be empty!!");
        }
        appointment.setPatientComment(patientComments);

        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED.toString());

        appointmentRepo.save(appointment);

        sendScheduledMails(appointment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    public List<MembersAppointmentDto> getFamilyMembersAppointmentDetails(Long userId) {
        PatientEntity patient= getPatientByUserId(userId);
        List<AppointmentEntity> appointmentList= appointmentRepo.findAppointmentListByPatient(patient);
        List<MembersAppointmentDto>membersAppointmentDtoList= appointmentList.stream()
                .filter(appointment->appointment.getAppointmentStatus().contains(AppointmentStatus.SCHEDULED.toString()))
                .map(this::convertToPatientAppointmentDto)
                .collect(Collectors.toList());
        return membersAppointmentDtoList.stream()
                .sorted(Comparator.comparing(MembersAppointmentDto::getAppointmentDate).thenComparing(MembersAppointmentDto::getAppointmentTime))
                .collect(Collectors.toList());
    }
    private PatientEntity getPatientByUserId(Long userId) {
        Optional<UserEntity> userOp = userRepo.findById(userId);
        if (userOp.isEmpty()) {
            throw new NoSuchUserExistsException();
        }

        Optional<PatientEntity> patientOp = patientRepo.findByUser(userOp.get());
        if (patientOp.isEmpty()) {
            throw new NoSuchPatientExistsException();
        }
        return patientOp.get();
    }

    private DoctorEntity getDoctorByDoctorId(Long docId) {
        Optional<DoctorEntity> docOp = docRepoJpa.findById(docId);
        if (docOp.isEmpty()) {
            throw new NoSuchDoctorExistsException();
        }
        return docOp.get();
    }

    private Integer calculateByDate(LocalDate dob) {
        Period period = Period.between(dob, LocalDate.now());
        return period.getYears();
    }
    public ResponseEntity<Map<String, Object>> cancelAppointment(Long appointmentId) {
        AppointmentEntity appointment=  appointmentRepo.findByAppointmentId(appointmentId);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED.name());
//        appointment.setAppointmentSlotNumber(null);
        appointmentRepo.save(appointment);
        return new ResponseEntity<>(Collections.singletonMap("success", true), HttpStatus.CREATED);
    }
    private MembersAppointmentDto convertToPatientAppointmentDto(AppointmentEntity appointmentEntity) {
        if(appointmentEntity.getMember()==null){
            return new MembersAppointmentDto(appointmentEntity.getAppointmentId(),
                    appointmentEntity.getDoctorAvailability().getDate(),
                    AppointmentSlot.getAppointmentSlotByValues(appointmentEntity.getDoctorAvailability().getShift(),appointmentEntity.getAppointmentSlotNumber()).get().getSlotTiming(),
                    appointmentEntity.getAppointmentStatus(),
                    (appointmentEntity.getPatient().getFirstName()+" "+appointmentEntity.getPatient().getLastName()),
                    appointmentEntity.getPatient().getGender(),
                    calculateByDate(appointmentEntity.getPatient().getDob()),
                    appointmentEntity.getPatientComment(),
                    appointmentEntity.getDoctorAvailability().getDoctor().getFirstName()+" "+appointmentEntity.getDoctorAvailability().getDoctor().getLastName(),
                    appointmentEntity.getDoctorAvailability().getDoctor().getSpecialization(),
                    appointmentEntity.getDoctorAvailability().getDoctor().getPhoneNumber(),
                    calculateByDate(appointmentEntity.getDoctorAvailability().getDoctor().getConsultingFrom()));}

        else return new  MembersAppointmentDto(appointmentEntity.getAppointmentId(),
                appointmentEntity.getDoctorAvailability().getDate(),
                AppointmentSlot.getAppointmentSlotByValues(appointmentEntity.getDoctorAvailability().getShift(),appointmentEntity.getAppointmentSlotNumber()).get().getSlotTiming(),
                appointmentEntity.getAppointmentStatus(),
                (appointmentEntity.getMember().getFirstName()+" "+appointmentEntity.getMember().getLastName()),
                appointmentEntity.getMember().getGender(),
                calculateByDate(appointmentEntity.getMember().getDob()),
                appointmentEntity.getPatientComment(),
                appointmentEntity.getDoctorAvailability().getDoctor().getFirstName()+" "+appointmentEntity.getDoctorAvailability().getDoctor().getLastName(),
                appointmentEntity.getDoctorAvailability().getDoctor().getSpecialization(),
                appointmentEntity.getDoctorAvailability().getDoctor().getPhoneNumber(),
                calculateByDate(appointmentEntity.getDoctorAvailability().getDoctor().getConsultingFrom()));
    }
    public List<MembersAppointmentDto> getAppointmentsBetween(Long userId, LocalDate fromDate, LocalDate toDate) {
        PatientEntity patient= getPatientByUserId(userId);
        List<AppointmentEntity> appointmentList= appointmentRepo.findAppointmentListByPatient(patient);
        List<MembersAppointmentDto>membersAppointmentDtoList= appointmentList.stream()
                .filter(appointment->appointment.getAppointmentStatus().contains(AppointmentStatus.SCHEDULED.toString()) && (appointment.getDoctorAvailability().getDate().isEqual(fromDate) || appointment.getDoctorAvailability().getDate().isAfter(fromDate)) && (appointment.getDoctorAvailability().getDate().isEqual(toDate) || appointment.getDoctorAvailability().getDate().isBefore(toDate)))                .map(this::convertToPatientAppointmentDto)
                .collect(Collectors.toList());
        return membersAppointmentDtoList.stream()
                .sorted(Comparator.comparing(MembersAppointmentDto::getAppointmentDate).thenComparing(MembersAppointmentDto::getAppointmentTime))
                .collect(Collectors.toList());
    }

    private void sendScheduledMails(AppointmentEntity appointment) {

        if(appointment.getMember() == null) {

            mailSenderUtil.SendMail(
                    appointment.getDoctorAvailability().getDoctor().getUserEntity().getEmail(),
                    APPOINTMENT_SCHEDULED_SUBJECT,
                    getScheduledDoctorMailBody(
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getPatient().getFirstName(),
                            appointment.getPatient().getLastName(),
                            appointment.getPatient().getGender(),
                            appointment.getPatient().getDob(),
                            appointment.getDoctorAvailability().getDate()
                    ));

            mailSenderUtil.SendMail(
                    appointment.getPatient().getUser().getEmail(),
                    APPOINTMENT_SCHEDULED_SUBJECT,
                    getScheduledPatientMailBody(
                            appointment.getPatient().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getLastName(),
                            appointment.getDoctorAvailability().getDoctor().getSpecialization(),
                            appointment.getDoctorAvailability().getDate()
                    ));
        } else {
            mailSenderUtil.SendMail(
                    appointment.getDoctorAvailability().getDoctor().getUserEntity().getEmail(),
                    APPOINTMENT_SCHEDULED_SUBJECT,
                    getScheduledDoctorMailBody(
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getMember().getFirstName(),
                            appointment.getMember().getLastName(),
                            appointment.getMember().getGender(),
                            appointment.getMember().getDob(),
                            appointment.getDoctorAvailability().getDate()
                    ));

            mailSenderUtil.SendMail(
                    appointment.getPatient().getUser().getEmail(),
                    APPOINTMENT_SCHEDULED_SUBJECT,
                    getScheduledPatientFamilyMailBody(
                            appointment.getPatient().getFirstName(),
                            appointment.getMember().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getFirstName(),
                            appointment.getDoctorAvailability().getDoctor().getLastName(),
                            appointment.getDoctorAvailability().getDoctor().getSpecialization(),
                            appointment.getDoctorAvailability().getDate()
                    ));
        }

    }

    private String getScheduledPatientMailBody(String patientFirstName, String doctorFirstName, String doctorLastName, String doctorSpecialization, LocalDate appointmentDate) {
        return "Hi " + patientFirstName + ",\n\n" +
                "Appointment has been scheduled with doctor " + doctorFirstName + " " + doctorLastName +
                " (" + doctorSpecialization +
                ") on " + appointmentDate + ".\n\n" +
                "Thanks,\n" +
                "DABS Team";
    }

    private String getScheduledPatientFamilyMailBody(String patientFirstName, String memberFirstName, String doctorFirstName, String doctorLastName, String doctorSpecialization, LocalDate appointmentDate) {
        return "Hi " + patientFirstName + ",\n\n" +
                "Your family member " +
                memberFirstName +
                "'s appointment has been scheduled with Dr. " + doctorFirstName + " " + doctorLastName +
                " (" + doctorSpecialization +
                ") on " + appointmentDate + ".\n\n" +
                "Thanks,\n" +
                "DABS Team";
    }

    private String getScheduledDoctorMailBody(String doctorFirstName, String patientFirstName, String patientLastName, String patientGender, LocalDate patientDOB, LocalDate appointmentDate) {
        return "Hi Dr. " + doctorFirstName + ",\n\n" +
                "Appointment has been scheduled for patient " + patientFirstName + " " + patientLastName +
                " (" + patientGender.charAt(0) + getYearsTillToday(patientDOB) +
                ") on " + appointmentDate + ".\n\n" +
                "Regards,\n" +
                "DABS Team";
    }
}