package com.example.electric_usage_tracker.service;

import com.example.electric_usage_tracker.model.PasswordResetToken;
import com.example.electric_usage_tracker.model.User;
import com.example.electric_usage_tracker.repository.PasswordResetTokenRepository;
import com.example.electric_usage_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.auth.password-reset-token-expiry-hours}")
    private int tokenExpiryHours;

    public void generatePasswordResetToken(String email) {
        // Changed from Optional<User> to direct User retrieval
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(tokenExpiryHours);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(expiryDate);
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:9090/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            throw new RuntimeException("Invalid token!");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now()) || resetToken.isUsed()) {
            throw new RuntimeException("Token expired or already used!");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}