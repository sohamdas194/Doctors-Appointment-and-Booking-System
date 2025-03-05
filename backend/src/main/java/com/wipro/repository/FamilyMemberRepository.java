package com.wipro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.entity.FamilyMemberEntity;
import com.wipro.entity.PatientEntity;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMemberEntity, Long>{

    //@Query(nativeQuery = true, value = "SELECT m.member_id FROM dabs2.familymember m join dabs2.patients p on p.patient= m.patient WHERE m.patient= patient")
    public List<FamilyMemberEntity> findMembersByPatient(PatientEntity patient);

    public FamilyMemberEntity findByMemberId(Long memberId);

    public List<FamilyMemberEntity> findAllByPatient(PatientEntity patient);
}
