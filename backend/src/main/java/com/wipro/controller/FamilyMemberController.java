package com.wipro.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.dto.FamilyMemberDto;
import com.wipro.service.FamilyMemberService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/FamilyMember")
public class FamilyMemberController {

    @Autowired
    private FamilyMemberService familyMemberService;

    @PostMapping("/addFamilyMember/{userId}")
    public ResponseEntity<Map<String, Object>> addFamilyMember(@PathVariable Long userId, @RequestBody FamilyMemberDto familyMemberDto) {
        return familyMemberService.addFamilyMember(userId, familyMemberDto);
    }

    @PutMapping("/updateMember/{memberId}")
    public ResponseEntity<Map<String, Object>> updateFamilyMember(@PathVariable Long memberId, @RequestBody FamilyMemberDto familyMemberDto) {
        return familyMemberService.updateFamilyMember(memberId, familyMemberDto);
    }

    @GetMapping("/getFamilyMembers/{userId}")
    public Optional<List<FamilyMemberDto>> getFamilyMembers(@PathVariable Long userId) {
        return familyMemberService.getFamilyMembers(userId);
    }

    @GetMapping("/edit/{memberId}")
    public FamilyMemberDto getFamilyMember(@PathVariable Long memberId) {
        return familyMemberService.getFamilyMember(memberId);
    }

}
