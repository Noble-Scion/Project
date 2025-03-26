package uk.ac.qub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uk.ac.qub.dto.LoginRequest;
import uk.ac.qub.dto.LoginResponse;
import uk.ac.qub.model.User;
import uk.ac.qub.service.EmailService;
import uk.ac.qub.repository.UserRepository;
import uk.ac.qub.dto.ForgotPasswordRequest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);

                LoginResponse response = new LoginResponse();
                response.setUserId(user.getUserId());
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
            userRepository.save(user);

            String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
            String emailBody = String.format(
                    "Hello,\n\nYou have requested to reset your password. Please click the link below to reset it:\n\n%s\n\n" +
                            "This link will expire in 24 hours.\n\nIf you didn't request this, please ignore this email.\n\n" +
                            "Best regards,\nCarShare Team", resetLink);

            emailService.sendEmail(request.getEmail(), "Password Reset Request", emailBody);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok().build();
    }
}

