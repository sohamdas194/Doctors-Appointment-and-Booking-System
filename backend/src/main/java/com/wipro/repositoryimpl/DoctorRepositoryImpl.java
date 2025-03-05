package com.wipro.repositoryimpl;
import com.wipro.constant.AppointmentSlot;

import com.wipro.constant.AppointmentStatus;
import com.wipro.constant.ResponseStatus;
import com.wipro.constant.Role;
import com.wipro.dto.*;
import com.wipro.entity.AppointmentEntity;
import com.wipro.entity.DoctorAvailabilityEntity;
import com.wipro.entity.DoctorEntity;
import com.wipro.entity.UserEntity;
import com.wipro.repository.AppointmentRepository;
import com.wipro.repository.DoctorAvailabilityRepository;
import com.wipro.repository.DoctorRepository;
import com.wipro.repository.UserRepository;
import com.wipro.utility.MailSenderUtil;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.wipro.constant.MailSubjectAndBody;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.wipro.constant.AppointmentShiftTimings.*;
import static java.time.temporal.ChronoUnit.DAYS;


@Component
public class DoctorRepositoryImpl {

    private DoctorRepository doctorRepository;
    private UserRepository userRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    private MailSenderUtil mailSenderUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private DoctorAvailabilityRepository doctorAvailabilityRepo;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EntityManager entityManager;

    @Lazy
    public DoctorRepositoryImpl(DoctorRepository doctorRepository, UserRepository userRepository, JavaMailSender javaMailSender, DoctorAvailabilityRepository doctorAvailabilityRepo) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.doctorAvailabilityRepo = doctorAvailabilityRepo;
    }

    @Value("${spring.mail.username}")
    private String frommEmailId;

    public ResponseEntity<Map<String, Object>> saveDoctorsImpl(DoctorDto doctorDto) {

        UserEntity userEntity = new UserEntity(doctorDto.getEmail(), passwordEncoder.encode(doctorDto.getPassword()), Role.DOCTOR.name());
        userRepository.save(userEntity);
        DoctorEntity doctorEntity = new DoctorEntity(doctorDto.getFirstName(), doctorDto.getLastName(),
                doctorDto.getDateOfBirth(), doctorDto.getGender(), doctorDto.getCity(), doctorDto.getCountry(), doctorDto.getAddress1(),
                doctorDto.getAddress2(), doctorDto.getState(),
                doctorDto.getPinCode(), doctorDto.getPhoneNumber(), doctorDto.getBloodGroup(), doctorDto.getSpecialization(),
                doctorDto.getConsultingFrom(), userEntity);
        doctorRepository.save(doctorEntity);
        new Thread(() -> sendWelcomeEmail(doctorDto.getEmail(), doctorDto.getFirstName())).start();
        return new ResponseEntity<>(Collections.singletonMap(ResponseStatus.SUCCESS.name(), true), HttpStatus.CREATED);
    }


    public void sendWelcomeEmail(String emailTo, String firstName) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.frommEmailId);
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setText("Hi " + "Dr. " + firstName + " Welcome to Doctor Appointment System");
        simpleMailMessage.setSubject("Doctor Appointment System");
        javaMailSender.send(simpleMailMessage);
    }


    public ResponseEntity<DoctorDto> getDoctorByID(Long userId) {
        Optional<DoctorEntity> doctor = doctorRepository.findByUserEntity(userRepository.findById(userId).orElse(null));
        if (doctor.isPresent()) {
            Optional<UserEntity> user = userRepository.findById(userId);
            if (user.isPresent()) {
                DoctorDto doctorDto = new DoctorDto(user.get().getEmail(), user.get().getPassword(), user.get().getRole(),
                        doctor.get().getFirstName(), doctor.get().getLastName(), doctor.get().getDateOfBirth(), doctor.get().getGender(),
                        doctor.get().getCity(), doctor.get().getCountry(), doctor.get().getAddress1(), doctor.get().getAddress2(),
                        doctor.get().getState(), doctor.get().getPinCode(), doctor.get().getPhoneNumber(), doctor.get().getBloodGroup(),
                        doctor.get().getSpecialization(), doctor.get().getConsultingFrom());

                return new ResponseEntity<>(doctorDto, HttpStatus.OK);
            }
        }
        return new ResponseEntity<DoctorDto>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<DoctorDto> updateDoctorByID(Long doctorId, DoctorDto doctorDto) {
        Optional<DoctorEntity> doctor = doctorRepository.findByUserEntity(userRepository.findById(doctorId).orElse(null));
        if (doctor.isPresent()) {
            doctor.get().setAddress1(doctorDto.getAddress1());
            doctor.get().setAddress2(doctorDto.getAddress2());
            doctor.get().setCity(doctorDto.getCity());
            doctor.get().setPhoneNumber(doctorDto.getPhoneNumber());
            doctor.get().setSpecialization(doctorDto.getSpecialization());
            doctor.get().setState(doctorDto.getState());
            doctor.get().setPinCode(doctorDto.getPinCode());
            doctor.get().setCountry(doctorDto.getCountry());
            doctorRepository.save(doctor.get());
            return new ResponseEntity<DoctorDto>(HttpStatus.OK);
        }
        return new ResponseEntity<DoctorDto>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<DoctorAvailabilityDto>> saveAllDoctorDates(DoctorEntity doc, List<DoctorAvailabilityDto> doctorAvailable) {
        List<DoctorAvailabilityEntity> existingAvailabilities = doctorAvailabilityRepo.getAllValidAvailableDates(LocalDate.now(), doc);
        List<DoctorAvailabilityEntity> newAvailabilites = new ArrayList<>();
        for (DoctorAvailabilityDto doctorAvailabilityDto : doctorAvailable) {
            boolean isOverlapping = existingAvailabilities.stream().anyMatch(
                    availability -> availability.getDate().equals(doctorAvailabilityDto.getDate()) &&
                            availability.getStatus().equals(doctorAvailabilityDto.getStatus()) &&
                            availability.getShift().equals(doctorAvailabilityDto.getShift())
            );
            if (!isOverlapping) {
                DoctorAvailabilityEntity doctorAvailabilityEntity = new DoctorAvailabilityEntity(doctorAvailabilityDto.getDate(), doctorAvailabilityDto.getStatus(), doctorAvailabilityDto.getShift(), doc);
                newAvailabilites.add(doctorAvailabilityEntity);
            } else {
                throw new IllegalArgumentException("Overlapping dates found for doctor availability");
            }
        }
        for (DoctorAvailabilityEntity doctor : newAvailabilites) {
            doctorAvailabilityRepo.save(doctor);
        }

        List<DoctorAvailabilityDto> docAvails = doctorAvailabilityRepo.getAllValidAvailableDates(LocalDate.now(), doc).stream().map(docAvail -> {
                    return new DoctorAvailabilityDto(docAvail.getDate(), docAvail.getStatus(), docAvail.getShift());
                }
        ).toList();

        return new ResponseEntity<>(docAvails, HttpStatus.CREATED);
    }

    public ResponseEntity<List<DoctorAvailabilityDto>> getAllDates(DoctorEntity doc) {
        List<DoctorAvailabilityDto> docAvails = doctorAvailabilityRepo.getAllValidAvailableDates(LocalDate.now(), doc).stream().map(docAvail -> {
                    return new DoctorAvailabilityDto(docAvail.getDate(), docAvail.getStatus(), docAvail.getShift());
                }
        ).toList();
        return new ResponseEntity<>(docAvails, HttpStatus.OK);
    }

    public ResponseEntity<List<DoctorAvailabilityDto>> getAllDoctorDates(DoctorEntity doc) {
        List<DoctorAvailabilityDto> docAvails = doctorAvailabilityRepo.getAllDatesFromDB(doc).stream().map(docavail -> {
            return new DoctorAvailabilityDto(docavail.getDate(), docavail.getStatus(), docavail.getShift());
        }).toList();
        return new ResponseEntity<>(docAvails, HttpStatus.OK);
    }

    public ResponseEntity<DoctorAvailabilityDto> updateDoctorAvailabilityByDateAndShift(DoctorEntity doctor, LocalDate date, DoctorAvailabilityDto updateAvailability) {
        Optional<DoctorAvailabilityEntity> optionalEntity = doctorAvailabilityRepo.findByDoctorIdAndDate(doctor, date);
        boolean doctorLeaveFlag=false;
        boolean docShiftChangeFlag=false;
        if (optionalEntity.isPresent()) {
            DoctorAvailabilityEntity entity = optionalEntity.get();
            if (isUpdateAllowed(entity, date)) {

                if (entity.getShift() != updateAvailability.getShift()) {
                    entity.setShift(updateAvailability.getShift());
                    if (entity.getShift() == 0) {
                        doctorLeaveFlag=true;
                        entity.setStatus(false);
                    } else {
                        docShiftChangeFlag=true;
                        entity.setStatus(true);
                    }
                }
                DoctorAvailabilityEntity savedEntity = doctorAvailabilityRepo.save(entity);
                DoctorAvailabilityDto responseDto = new DoctorAvailabilityDto(
                        savedEntity.getDate(),
                        savedEntity.getStatus(),
                        savedEntity.getShift()
                );
                if(doctorLeaveFlag)
                    updateAppointmentStatus("LEAVE",doctor,savedEntity);
                if(docShiftChangeFlag)
                    updateAppointmentStatus("CHANGE",doctor,savedEntity);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void updateAppointmentStatus(String status, DoctorEntity doctor, DoctorAvailabilityEntity availabilityEntity) {
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findByDoctorAvailability(availabilityEntity);
        for(AppointmentEntity appointmentEntity : appointmentEntityList){
            appointmentEntity.setAppointmentStatus(AppointmentStatus.CANCELLED.toString());
            appointmentRepository.save(appointmentEntity);
            notifyUser(status,appointmentEntity);
        }
    }

    private void notifyUser(String status, AppointmentEntity appointmentEntity) {
        String doctorName = appointmentEntity.getDoctorAvailability().getDoctor().getFirstName() + " " + appointmentEntity.getDoctorAvailability().getDoctor().getLastName();
        String patientName = (null!= appointmentEntity.getMember())? appointmentEntity.getMember().getFirstName() + " " + appointmentEntity.getMember().getLastName() : appointmentEntity.getPatient().getFirstName() + " " + appointmentEntity.getPatient().getLastName();
        String emailID = appointmentEntity.getPatient().getUser().getEmail();
        mailSenderUtil.SendMail(
                emailID,
                MailSubjectAndBody.NOTIFICATION_MAIL_SUBJECT,
                getNotifyPatientMailBody(
                        patientName,
                        doctorName,
                        appointmentEntity.getDoctorAvailability().getDate(),
                        ("LEAVE".equals(status)) ? "ON LEAVE" : "SHIFT CHANGED"
                ));

    }

    private String getNotifyPatientMailBody(String patientName, String doctorName, LocalDate appointmentDate, String appointmentStatus) {
        return "Hi " + patientName + ",\n\n" +
                "Appointment status with doctor " + doctorName
                + " on " + appointmentDate + ", has changed to "+ AppointmentStatus.CANCELLED.toString() + " due to doctor unavailability("+ appointmentStatus +").\n\n" +
                "Kindly reschedule based on Doctor's next availability, sorry for the inconvenience,\n\n\n"+
                "Thanks,\n" +
                "DABS Team";
    }


    private boolean isUpdateAllowed(DoctorAvailabilityEntity entity, LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate entityDate = entity.getDate();
        LocalTime currentTime = LocalTime.now();
        return (entityDate.isAfter(today) || entityDate.isEqual(today.plusDays(1)) || (entityDate.isEqual(today) && currentTime.isBefore(LocalTime.of(8, 0))));
    }


    public List<DoctorEntity> getAllFilteredDoctors(String docName, String spclzn, Integer exp, String city, String pinCode, String apmntDate, boolean disableDateSearch) {
        return entityManager.createQuery("Select d from DoctorEntity d left join d.doctorAvailabilities da where " +
                        "d.firstName || ' ' || d.lastName like '%'||:docName||'%' and " +
                        "d.specialization like '%'||:spclzn||'%' and " +
                        "d.consultingFrom <= :maxConsultingFrom and " +
                        "d.city like '%'||:city||'%' and " +
                        "d.pinCode like '%'||:pinCode||'%' and " +
                        "d.consultationFee is not null and " +
                        "(:disableDateSearch = true or " +
                        "( da.date = :apmntDate " +
                        "and da.status = true " +
                        "and (CURRENT_DATE !=:apmntDate or " +
                        "(da.shift = 1 and CURRENT_TIME < :firstShiftEnd)  " +
                        "or (da.shift = 2 and CURRENT_TIME < :secondShiftEnd)) " +
                        "and (:maxSlotCount > (select count(aIn.appointmentSlotNumber) from AppointmentEntity aIn " +
                        "where aIn.doctorAvailability = da and aIn.appointmentStatus != :cancelledStatus" +
                        "))))", DoctorEntity.class)
                .setParameter("docName", docName)
                .setParameter("spclzn", spclzn)
                .setParameter("maxConsultingFrom", LocalDate.now().minusYears(exp))
                .setParameter("city", city)
                .setParameter("pinCode", pinCode)
                .setParameter("disableDateSearch", disableDateSearch)
                .setParameter("apmntDate", disableDateSearch? LocalDate.parse("0001-01-01") : LocalDate.parse(apmntDate))
                .setParameter("maxSlotCount", TOTAL_NUMBER_OF_SLOTS)
                .setParameter("firstShiftEnd", FIRST_SHIFT_END)
                .setParameter("secondShiftEnd", SECOND_SHIFT_END)
                .setParameter("cancelledStatus", AppointmentStatus.CANCELLED.toString())
                .getResultList();
    }


    public List<DoctorConsultationHistoryDto> getAllDoctorConsultations(DoctorEntity doctor) {
        return doctorRepository.getAllDoctorCompletedConsultations(doctor.getDoctorId(),AppointmentStatus.CONSULTED.toString(),AppointmentStatus.COMPLETED.toString(),AppointmentStatus.NO_SHOW.toString(),AppointmentStatus.CANCELLED.toString()).stream()
                .map(appointment -> {
                    int shift = appointment.getDoctorAvailability().getShift();
                    int slotNumber = (appointment.getAppointmentSlotNumber());
                    Optional<AppointmentSlot> appointmentSlot = AppointmentSlot.getAppointmentSlotByValues(shift, slotNumber);
                    String appointmentSlotString = appointmentSlot.map(AppointmentSlot::getSlotTiming).orElse("");
                    return new DoctorConsultationHistoryDto(
                            appointment.getAppointmentId(),

                            appointment.getMember() == null ? appointment.getPatient().getFirstName()+" "+appointment.getPatient().getLastName()
                                    : appointment.getMember().getFirstName()+" "+appointment.getMember().getLastName(),
                            appointment.getPatient().getGender(),
                            appointment.getMember() == null ? Math.round(DAYS.between(appointment.getPatient().getDob(), LocalDate.now()) / 365) :
                                    Math.round(DAYS.between(appointment.getMember().getDob(), LocalDate.now()) / 365),
                            appointment.getDoctorAvailability().getDate(),
                            appointmentSlotString,
                            appointment.getPatientComment(),
                            appointment.getDoctorComment(),
                            appointment.getAppointmentStatus(),
                            appointment.getDoctorAvailability().getDoctor().getConsultationFee()
                    );
                }).toList();

    }


    public List<DoctorConsultationHistoryDto> getAllConsultationsInDateRangeAndPatientName(DoctorEntity doctor, LocalDate fromDate, LocalDate toDate, String patientName){
        return doctorRepository.getAllConsultationsByDoctorIdAndDateRangeAndPatientName(doctor.getDoctorId(),fromDate,toDate,patientName,AppointmentStatus.CONSULTED.toString(),AppointmentStatus.COMPLETED.toString(),AppointmentStatus.NO_SHOW.toString(),AppointmentStatus.CANCELLED.toString()).stream()
                .map(appointment -> {
                    int shift = appointment.getDoctorAvailability().getShift();
                    int slotNumber = (appointment.getAppointmentSlotNumber());
                    Optional<AppointmentSlot> appointmentSlot = AppointmentSlot.getAppointmentSlotByValues(shift, slotNumber);
                    String appointmentSlotString = appointmentSlot.map(AppointmentSlot::getSlotTiming).orElse("");
                    return new DoctorConsultationHistoryDto(
                            appointment.getAppointmentId(),

                            appointment.getMember()==null?  appointment.getPatient().getFirstName()+" "+appointment.getPatient().getLastName() :
                                    appointment.getMember().getFirstName()+" "+appointment.getMember().getLastName(),
                            appointment.getPatient().getGender(),
                            appointment.getMember()==null? Math.round(DAYS.between(appointment.getPatient().getDob(), LocalDate.now())/365) :
                                    Math.round(DAYS.between(appointment.getMember().getDob(), LocalDate.now())/365),
                            appointment.getDoctorAvailability().getDate(),
                            appointmentSlotString,
                            appointment.getPatientComment(),
                            appointment.getDoctorComment(),
                            appointment.getAppointmentStatus(),
                            appointment.getDoctorAvailability().getDoctor().getConsultationFee()
                    );
                }).toList();
    }

    public List<DoctorDashboardDto> getAllDoctorsFutureAppointments(DoctorEntity doctor){
        return  doctorRepository.getAllUpcomingAppointments(doctor.getDoctorId(), AppointmentStatus.SCHEDULED.toString()).stream()
                .map(this::convertToDoctorDashboardDto).collect(Collectors.toList());
    }

    public List<DoctorDashboardDto> getDoctorAppointmentByDate(DoctorEntity doctor,LocalDate date){
        return  doctorRepository.getAppointmentsByDate(doctor.getDoctorId(),date, AppointmentStatus.SCHEDULED.toString()).stream()
                .map(this::convertToDoctorDashboardDto).collect(Collectors.toList());
    }

    public List<DoctorDashboardDto> getAppointmentsBetweenDates(DoctorEntity doctor,LocalDate fromDate, LocalDate toDate){
        return  doctorRepository.getAppointmentsBetweenDates(doctor.getDoctorId(),fromDate,toDate, AppointmentStatus.SCHEDULED.toString()).stream()
                .map(this::convertToDoctorDashboardDto).collect(Collectors.toList());
    }
    private DoctorDashboardDto convertToDoctorDashboardDto(AppointmentEntity appointmentEntity) {
        int shift=appointmentEntity.getDoctorAvailability().getShift();
        int slotNumber=appointmentEntity.getAppointmentSlotNumber();
        Optional<AppointmentSlot> appointmentSlot=AppointmentSlot.getAppointmentSlotByValues(shift,slotNumber);
        String appointmentSlotTiming=appointmentSlot.map(AppointmentSlot::getSlotTiming).orElse(null);
        return new DoctorDashboardDto(
                appointmentEntity.getAppointmentId(),
                appointmentEntity.getMember()==null?appointmentEntity.getPatient().getFirstName() :
                        appointmentEntity.getMember().getFirstName(),
                appointmentEntity.getMember()==null?appointmentEntity.getPatient().getLastName() :
                        appointmentEntity.getMember().getLastName(),
                appointmentEntity.getPatient().getGender(),
                appointmentEntity.getMember() == null ? Math.round(DAYS.between(appointmentEntity.getPatient().getDob(), LocalDate.now()) / 365) :
                        Math.round(DAYS.between(appointmentEntity.getMember().getDob(), LocalDate.now()) / 365),
                appointmentEntity.getDoctorAvailability().getDate(),
                appointmentSlotTiming,
                appointmentEntity.getPatientComment());
    }



    public ResponseEntity<AppointmentDto> updateDoctorComment(Long appointmentId, AppointmentDto appointmentDto) {
        Optional<AppointmentEntity> updateComment = appointmentRepository.findById(appointmentId);
        if (updateComment.isPresent()) {
            updateComment.get().setDoctorComment(appointmentDto.getDoctorComment());
            updateComment.get().setAppointmentStatus(appointmentDto.getAppointmentStatus());
            appointmentRepository.save(updateComment.get());
            return new ResponseEntity<AppointmentDto>(HttpStatus.OK);
        }
        return new ResponseEntity<AppointmentDto>(HttpStatus.BAD_REQUEST);
    }


}
