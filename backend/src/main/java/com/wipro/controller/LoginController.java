package com.wipro.controller;

import com.wipro.model.*;
import com.wipro.service.JwtServiceImpl;
import com.wipro.service.LoginServiceImpl;
import com.wipro.service.PasswordResetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private PasswordResetServiceImpl passwordResetService;

    @Autowired
    private JwtServiceImpl jwtService;

    @PostMapping("/authenticate")
    public AuthenticatedUser authenticate(@RequestBody UserCredentials creds) {
        AuthenticatedUser authUser = loginService.checkUserCredentials(creds);
        authUser.setAuthToken(jwtService.createToken(authUser));

        return authUser;
    }

    @GetMapping("/getTestToken")
    public String authenticate() {
        AuthenticatedUser user = new AuthenticatedUser();
        user.setUserId(Long.MAX_VALUE);
        user.setUserRole("TEST");
        return jwtService.createToken(user);
    }

    @PostMapping("/forgotPassword")
    public ForgotPasswordBody forgotPassword(@RequestBody ForgotPasswordBody forgotPasswordBody) {
        return passwordResetService.executeForgotPasswordProcess(forgotPasswordBody.userEmail());
    }

    @PostMapping("/resetPassword")
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        passwordResetService.resetPassword(resetPasswordRequest);
    }

    @GetMapping("/validatePasswordResetToken/{token}")
    public PasswordResetTokenValidity validatePasswordToken(@PathVariable String token) {
        return new PasswordResetTokenValidity(
                passwordResetService.isPasswordResetTokenValid(token));
    }
}


