package com.wipro.repositoryimpl;


import com.wipro.dto.PatientDto;
import com.wipro.entity.PatientEntity;
import com.wipro.entity.UserEntity;
import com.wipro.model.Response;
import com.wipro.repository.PatientRepository;
import com.wipro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
//import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.wipro.constant.Role;

import java.util.Optional;

@Component
public class PatientRepositoryImpl {

    private UserRepository userRepository;
    private PatientRepository patientRepo;
    private JavaMailSender javaMailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public PatientRepositoryImpl(PatientRepository patientRepo, UserRepository userRepository, JavaMailSender javaMailSender) {
        this.patientRepo = patientRepo;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String frommEmailId;

    public Response saveUserPatient(PatientDto patientDto) {

        Response response = new Response();

        UserEntity user = new UserEntity(patientDto.getEmail(), passwordEncoder.encode(patientDto.getPassword()), Role.PATIENT.name());
        userRepository.save(user);
        PatientEntity patient = new PatientEntity(patientDto.getFirstName(), patientDto.getLastName(), patientDto.getDob(), patientDto.getGender(), patientDto.getBloodGroup(), patientDto.getMaritalStatus(), patientDto.getPhone(), patientDto.getStreetAddressLine1(), patientDto.getStreetAddressLine2(), patientDto.getCity(), patientDto.getState(), patientDto.getPin(), patientDto.getEmergencyFirstName(), patientDto.getEmergencyLastName(), patientDto.getEmergencyRelationship(), patientDto.getEmergencyContact(), patientDto.getReasonForRegistration(), patientDto.getAdditionalNotes(), user);
        patientRepo.save(patient);
        new Thread(() -> sendWelcomeEmail(patientDto)).start();
        response.setMessage("Patient data saved successfully");

        return response;
    }

    private void sendWelcomeEmail(PatientDto patientDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.frommEmailId);
        simpleMailMessage.setTo(patientDto.getEmail());
        simpleMailMessage.setText("Hi " + patientDto.getFirstName() + ",/n Welcome to Doctor Appointment System");
        simpleMailMessage.setSubject("Doctor Appointment System");
        javaMailSender.send(simpleMailMessage);
    }

    public PatientDto getPatientById(Long userId) {
        Optional<UserEntity> userOp = userRepository.findById(userId);
        if (userOp.isEmpty()) {
            throw new RuntimeException("User not found!!");
        }

        Optional<PatientEntity> patientOp = patientRepo.findByUser(userOp.get());
        if (patientOp.isEmpty()) {
            throw new RuntimeException("Patient not found!!");
        }
        PatientEntity patientEntity = patientOp.get();
        return getPatientDto(patientEntity);
    }

    private PatientDto getPatientDto(PatientEntity entity) {

        return new PatientDto(entity.getPatientId(), entity.getFirstName(),
                entity.getLastName(), entity.getDob(),
                entity.getGender(), entity.getHeight(),
                entity.getWeight(), entity.getBloodGroup(),
                entity.getMaritalStatus(), entity.getPhone(),
                entity.getStreetAddressLine1(), entity.getStreetAddressLine2(),
                entity.getCity(), entity.getState(), entity.getPin(),
                entity.getEmergencyFirstName(), entity.getEmergencyLastName(),
                entity.getEmergencyRelationship(), entity.getEmergencyContact(),
                entity.getReasonForRegistration(), entity.getAdditionalNotes(),
                entity.getInsuranceCompany(), entity.getInsuranceId(),
                entity.getPolicyHolderName(), entity.getPolicyDate());
    }

    public ResponseEntity<PatientDto> updatePatient(PatientDto patientDto, Long userId) {
        PatientEntity patient = this.patientRepo.findPatientsByUserUserId(userId);

        if (patient != null) {

            patient.setHeight(patientDto.getHeight());
            patient.setWeight(patientDto.getWeight());
            patient.setMaritalStatus(patientDto.getMaritalStatus());
            patient.setPhone(patientDto.getPhone());
            patient.setStreetAddressLine1(patientDto.getStreetAddressLine1());
            patient.setStreetAddressLine2(patientDto.getStreetAddressLine2());
            patient.setCity(patientDto.getCity());
            patient.setState(patientDto.getState());
            patient.setPin(patientDto.getPin());
            patient.setEmergencyFirstName(patientDto.getEmergencyFirstName());
            patient.setEmergencyLastName(patientDto.getEmergencyLastName());
            patient.setEmergencyRelationship(patientDto.getEmergencyRelationship());
            patient.setEmergencyContact(patientDto.getEmergencyContact());
            patient.setReasonForRegistration(patientDto.getReasonForRegistration());
            patient.setAdditionalNotes(patientDto.getAdditionalNotes());
            patient.setInsuranceCompany(patientDto.getInsuranceCompany());
            patient.setInsuranceId(patientDto.getInsuranceId());
            patient.setPolicyHolderName(patientDto.getPolicyHolderName());
            patient.setPolicyDate(patientDto.getPolicyDate());

            patientRepo.save(patient);
            return new ResponseEntity<PatientDto>(HttpStatus.OK);
        }
        return new ResponseEntity<PatientDto>(HttpStatus.BAD_REQUEST);
    }

}
