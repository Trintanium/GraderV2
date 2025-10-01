package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.entity.UserDto;
import com.example.graderbackend.entity.EmailVerification;
import com.example.graderbackend.entity.User;
import com.example.graderbackend.exception.EmailSendingException;
import com.example.graderbackend.exception.InvalidPasswordException;
import com.example.graderbackend.exception.InvalidTokenException;
import com.example.graderbackend.repository.EmailVerificationRepository;
import com.example.graderbackend.repository.UserRepository;
import com.example.graderbackend.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapperServiceImpl mapperService;

    public EmailVerification createEmailVerification(User user) {
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setToken(UUID.randomUUID().toString());
        emailVerification.setUser(user);
        emailVerification.setExpiry(LocalDateTime.now().plusHours(24));
        return emailVerificationRepository.save(emailVerification);
    }

    public Optional<UserDto> verifyEmailAndReturnUser(String token) {
        return getValidEmailToken(token).map(v -> {
            v.setUsed(true);
            emailVerificationRepository.save(v);

            User user = v.getUser();
            user.setEmailVerified(true);
            User savedUser = userRepository.save(user);

            return mapperService.toDto(savedUser, UserDto.class);
        });
    }

    public boolean resetPassword(String token, String newPassword, String confirmPassword) {
        return getValidEmailToken(token).map(v -> {
            User user = v.getUser();

            if (passwordEncoder.matches(newPassword, user.getPassword())) {
                throw new InvalidPasswordException();
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            v.setUsed(true);
            emailVerificationRepository.save(v);

            return true;
        }).orElseThrow(() -> new InvalidTokenException(token));
    }

    public boolean isValidEmailToken(String token) {
        return getValidEmailToken(token).isPresent();
    }

    public Optional<EmailVerification> getValidEmailToken(String token) {
        return emailVerificationRepository.findByToken(token)
                .filter(v -> !v.isUsed() && v.getExpiry().isAfter(LocalDateTime.now()));
    }

    public void sendVerificationEmail(EmailVerification emailVerification) {
        String frontendUrl = "http://localhost:5173/email/verify";
        sendEmail(
                emailVerification.getUser().getEmail(),
                emailVerification.getToken(),
                "Verify your email",
                frontendUrl,
                "Please click the button below to verify your email."
        );
    }

    public void sendForgotPasswordEmail(EmailVerification emailVerification) {
        String frontendUrl = "http://localhost:5173/password/reset";
        sendEmail(
                emailVerification.getUser().getEmail(),
                emailVerification.getToken(),
                "Reset your password",
                frontendUrl,
                "Please click the button below to reset your password."
        );
    }

    public void sendEmail(String email, String token, String subject, String frontendUrl, String message) {
        try {
            String actionUrl = frontendUrl + "?token=" + token;
            String content = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px;
                                border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                        <h2 style="color: #333;">%s</h2>
                        <p style="font-size: 16px; color: #555;">%s</p>
                        <a href="%s" style="display: inline-block; margin: 20px 0; padding: 10px 20px;
                            font-size: 16px; color: #fff; background-color: #007bff;
                            text-decoration: none; border-radius: 4px;">Click Here</a>
                        <p style="font-size: 14px; color: #777;">Or copy and paste this link into your browser:</p>
                        <p style="font-size: 14px; color: #007bff;">%s</p>
                        <p style="font-size: 12px; color: #aaa;">This is an automated message. Please do not reply.</p>
                    </div>
                    """.formatted(subject, message, actionUrl, actionUrl);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSendingException(e);
        }
    }
}