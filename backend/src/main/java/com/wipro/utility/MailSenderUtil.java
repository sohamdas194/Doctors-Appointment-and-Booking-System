package com.wipro.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSenderUtil {

    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private JavaMailSender javaMailSender;

    public void SendMail(String receiverAddress, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom("DABS <"+sender+">");
        msg.setTo(receiverAddress);
        msg.setSubject(subject);
        msg.setText(body);

        new Thread(() -> javaMailSender.send(msg)).start();
    }


}
