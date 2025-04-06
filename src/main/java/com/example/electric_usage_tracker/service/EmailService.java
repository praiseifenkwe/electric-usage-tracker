package com.example.electric_usage_tracker.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("debhaitulua@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Click this link to reset your password:\n" + resetLink);
        mailSender.send(message);
    }
}