package com.wipro.service;

import com.wipro.entity.UserEntity;
import com.wipro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private UserRepository userRepository;

    public Boolean doesEmailExist(String email) {
        Optional<UserEntity> emailExist = userRepository.findByEmail(email);
        System.out.println(emailExist.isPresent());
        return emailExist.isPresent();

    }

}
