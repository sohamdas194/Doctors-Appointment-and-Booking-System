package com.wipro.service;

import com.wipro.dto.FamilyMemberDto;
import com.wipro.repositoryimpl.FamilyMemberRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FamilyMemberService {


    private FamilyMemberRepositoryImpl familyMemberRepositoryImpl;

    @Autowired
    public FamilyMemberService(FamilyMemberRepositoryImpl familyMemberRepositoryImpl) {
        this.familyMemberRepositoryImpl = familyMemberRepositoryImpl;
    }


    public ResponseEntity<Map<String, Object>> addFamilyMember(Long userId, FamilyMemberDto familyMemberDto) {
        if (familyMemberDto.getDob().isAfter(LocalDate.now())) {
            return new ResponseEntity<>(Collections.singletonMap("failed", false), HttpStatus.CONFLICT);
        }
        return familyMemberRepositoryImpl.addFamilyMember(userId, familyMemberDto);
    }

    public ResponseEntity<Map<String, Object>> updateFamilyMember(Long memberId, FamilyMemberDto familyMemberDto) {
        return familyMemberRepositoryImpl.updateMember(memberId, familyMemberDto);
    }

    public Optional<List<FamilyMemberDto>> getFamilyMembers(Long userId) {
        return familyMemberRepositoryImpl.getMembersList(userId);
    }


    public FamilyMemberDto getFamilyMember(Long memberId) {
        return familyMemberRepositoryImpl.getFamilyMember(memberId);

    }
}
