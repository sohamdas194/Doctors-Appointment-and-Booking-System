package com.wipro.service;

import com.wipro.entity.PasswordResetToken;
import com.wipro.entity.UserEntity;
import com.wipro.model.ForgotPasswordBody;
import com.wipro.model.ResetPasswordRequest;
import com.wipro.repository.PasswordResetTokenRepository;
import com.wipro.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetServiceImpl {
    @Value("${spring.mail.username}")
    private String sender;
    private final String passwordResetEndpointUrl = "http://localhost:4200/resetPassword";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public ForgotPasswordBody executeForgotPasswordProcess(String userEmail) {
        UserEntity user = userRepo.findByEmail(userEmail).orElse(null);

        if (user == null) {
            throw new RuntimeException("Error!! user not found");
        }

        if (sendPasswordResetMail(user)) {
            return new ForgotPasswordBody(user.getEmail());
        } else {
            throw new RuntimeException("Password reset mail sending, failed!");
        }
    }

    private boolean sendPasswordResetMail(UserEntity user) {
        try {
            String pwdResetUrl = generateResetTokenUrl(user);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("DABS <"+sender+">");
            msg.setTo(user.getEmail());

            msg.setSubject("DABS password reset link [Valid for 10 mins]");
            msg.setText(generateResetMailBody("User", pwdResetUrl));

            javaMailSender.send(msg);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateResetTokenUrl(UserEntity user) {
        UUID uuid = UUID.randomUUID();

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(10);

        PasswordResetToken resetToken;

        if (user.getPasswordResetToken() == null) {
            resetToken = new PasswordResetToken();
            resetToken.setUserId(user.getUserId());
            resetToken.setToken(uuid.toString());
            resetToken.setExpiryDateTime(expiryDateTime);
        } else {
            resetToken = user.getPasswordResetToken();
            resetToken.setToken(uuid.toString());
            resetToken.setExpiryDateTime(expiryDateTime);
        }

        if (passwordResetTokenRepo.save(resetToken) != null) {
            return passwordResetEndpointUrl + "/" + resetToken.getToken();
        }

        return null;
    }

    private String generateResetMailBody(String userName, String passwordResetUrl) {
        return "Hello " +
                userName +
                ",\n\n" +
                "Please click on this link to reset your password: " +
                passwordResetUrl +
                "\n\nValid for 10 minutes." +
                "\n\nRegards,\n" +
                "DABS Team";
    }

    public Boolean isPasswordResetTokenValid(String token) {
        try {
            getPasswordResetToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public PasswordResetToken getPasswordResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token).orElse(null);

        if (resetToken != null && hasTokenTokenExpired(resetToken.getExpiryDateTime())) {
            return resetToken;
        }

        throw new RuntimeException("Invalid Token!!");
    }

    private boolean hasTokenTokenExpired(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return expiryDateTime.isAfter(currentDateTime);
    }

    public void resetPassword(ResetPasswordRequest resetPasswordReq) {
        PasswordResetToken resetToken = getPasswordResetToken(resetPasswordReq.getToken());

        UserEntity user = resetToken.getUser();

        String encodedPassword = bcryptEncoder.encode(resetPasswordReq.getNewPassword());
        user.setPassword(encodedPassword);

        user.setPasswordResetToken(null);
        userRepo.save(user);
    }

}
