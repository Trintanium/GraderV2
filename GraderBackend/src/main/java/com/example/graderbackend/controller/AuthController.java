package com.example.graderbackend.controller;

import com.example.graderbackend.dto.*;
import com.example.graderbackend.service.impl.AuthServiceImpl;
import com.example.graderbackend.service.impl.EmailVerificationServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private EmailVerificationServiceImpl emailVerificationService;

    // ------------------- REGISTER -------------------
    @PostMapping("/signUp")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        authService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getConfirmPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    // ------------------- EMAIL VERIFICATION -------------------
    @PostMapping("/email/verify")
    public ResponseEntity<AuthResponse> verifyEmail(@RequestBody TokenRequest request, HttpServletResponse response) {
        return emailVerificationService.verifyEmailAndReturnUser(request.getToken())
                .map(user -> {
                    TokenResponse tokenResponse = authService.createTokenResponse(user.getEmail());
                    Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(604800); // 7 days
                    response.addCookie(cookie);

                    AuthResponse authResponse = new AuthResponse(tokenResponse.getAccessToken(), user);
                    return ResponseEntity.ok(authResponse); // 200 OK
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build()); // 400 Bad Request
    }

    // ------------------- LOGIN -------------------
    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> login(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        AuthResponse authResponse = authService.loginAndGetUser(registerRequest, response);
        return ResponseEntity.ok(authResponse); // 200 OK
    }

    // ------------------- REFRESH TOKEN -------------------
    @GetMapping("/refresh")
    public ResponseEntity<String> refreshTokenCookie(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        TokenResponse tokenResponse = authService.refreshAccessToken(refreshToken);
        Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(604800);
        response.addCookie(cookie);

        return ResponseEntity.ok("{\"accessToken\":\"" + tokenResponse.getAccessToken() + "\"}"); // 200 OK
    }

    // ------------------- FORGOT PASSWORD -------------------
    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        authService.forgotPasswordVerification(request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Password reset email sent successfully!"); // 201 Created
    }

    // ------------------- VERIFY RESET TOKEN -------------------
    @PostMapping("/password/verify")
    public ResponseEntity<String> verifyResetToken(@RequestBody TokenRequest tokenRequest) {
        boolean valid = emailVerificationService.isValidEmailToken(tokenRequest.getToken());
        if (!valid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token."); // 400 Bad Request
        }
        return ResponseEntity.ok("Token valid. You can now reset your password."); // 200 OK
    }

    // ------------------- RESET PASSWORD -------------------
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        emailVerificationService.resetPassword(
                resetPasswordRequest.getToken(),
                resetPasswordRequest.getNewPassword(),
                resetPasswordRequest.getConfirmPassword()
        );
        return ResponseEntity.ok("Password reset successfully!"); // 200 OK
    }
}