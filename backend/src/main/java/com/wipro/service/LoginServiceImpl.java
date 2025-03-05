package com.wipro.service;

import com.wipro.model.AuthenticatedUser;
import com.wipro.model.UserCredentials;
import com.wipro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public AuthenticatedUser checkUserCredentials(UserCredentials creds) {
        AuthenticatedUser authUser = new AuthenticatedUser();
        userRepo.findByEmail(creds.getUserEmail()).ifPresentOrElse(
                user -> {
                    if (bcryptEncoder.matches(creds.getUserPassword(), user.getPassword())) {
                        if (user.getRole() != null) {
                            authUser.setUserId(user.getUserId());
                            authUser.setUserEmail(user.getEmail());
                            authUser.setUserRole(user.getRole());
                        } else {
                            throw new RuntimeException("User does not have any role!!");
                        }
                    } else {
                        throw new RuntimeException("Incorrect Password!!");
                    }
                },
                () -> {
                    throw new RuntimeException("User not found!!");
                });

        return authUser;
    }
}
