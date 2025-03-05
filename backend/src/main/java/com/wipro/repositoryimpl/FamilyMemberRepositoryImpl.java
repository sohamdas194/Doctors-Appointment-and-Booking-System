package com.wipro.repositoryimpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wipro.entity.UserEntity;
import com.wipro.repository.FamilyMemberRepository;
import com.wipro.repository.PatientRepository;
import com.wipro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.wipro.dto.FamilyMemberDto;
import com.wipro.entity.FamilyMemberEntity;
import com.wipro.entity.PatientEntity;

@Component
public class FamilyMemberRepositoryImpl {
    private UserRepository userRepository;
    private PatientRepository patientRepository;

    @Lazy
    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    public FamilyMemberRepositoryImpl(UserRepository userRepository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<Map<String, Object>> addFamilyMember(Long userId,FamilyMemberDto familyMemberDto) {
        PatientEntity patient = getPatientDetails(userId);
        FamilyMemberEntity familyMember = new FamilyMemberEntity(familyMemberDto.getFirstName(),
                familyMemberDto.getLastName(), familyMemberDto.getGender(), familyMemberDto.getBloodGroup(),
                familyMemberDto.getContactNo(), familyMemberDto.getDob(), familyMemberDto.getRelationship(),
                familyMemberDto.getHeight(),familyMemberDto.getWeight(), familyMemberDto.getHealthHistory(), patient);
        familyMemberRepository.save(familyMember);
        return new ResponseEntity<>(Collections.singletonMap("success", true), HttpStatus.CREATED);
    }

    public ResponseEntity<Map<String, Object>> updateMember(Long memberId,FamilyMemberDto familyMemberDto) {

        FamilyMemberEntity member = familyMemberRepository.findByMemberId(memberId);
        member.setContactNo(familyMemberDto.getContactNo());
        member.setHeight(familyMemberDto.getHeight());
        member.setWeight(familyMemberDto.getWeight());
        member.setHealthHistory(familyMemberDto.getHealthHistory());
        familyMemberRepository.save(member);
        return new ResponseEntity<>(Collections.singletonMap("success", true), HttpStatus.CREATED);
    }

    public Optional<List<FamilyMemberDto>> getMembersList(Long userId) {
        PatientEntity patient = getPatientDetails(userId);
        List<FamilyMemberEntity> famList = familyMemberRepository.findMembersByPatient(patient);
        return Optional.ofNullable(famList.stream().map(this::convertToFamilyMemberDto).collect(Collectors.toList()));
    }

    private FamilyMemberDto convertToFamilyMemberDto(FamilyMemberEntity familyMemberEntities) {
        return new FamilyMemberDto(familyMemberEntities.getMemberId(), familyMemberEntities.getFirstName(),familyMemberEntities.getLastName(),familyMemberEntities.getGender(), familyMemberEntities.getBloodGroup(), familyMemberEntities.getContactNo(), familyMemberEntities.getDob(),familyMemberEntities.getRelationship(), familyMemberEntities.getHeight(), familyMemberEntities.getWeight(), familyMemberEntities.getHealthHistory());
    }

    public PatientEntity getPatientDetails(Long userId){
        UserEntity user=userRepository.findByUserId(userId);
        System.out.println(user +"user data");
        PatientEntity patient =patientRepository.findPatientByUser(user);
        return patient;
    }

    public FamilyMemberDto getFamilyMember(Long memberId) {
        FamilyMemberEntity entity = familyMemberRepository.findByMemberId(memberId);
        return convertToFamilyMemberDto(entity);
    }
}
