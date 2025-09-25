package com.example.graderbackend.controller;

import com.example.graderbackend.dto.AuthResponse;
import com.example.graderbackend.dto.EmailRequest;
import com.example.graderbackend.dto.RegisterRequest;
import com.example.graderbackend.dto.ResetPasswordRequest;
import com.example.graderbackend.dto.TokenRequest;
import com.example.graderbackend.dto.TokenResponse;
import com.example.graderbackend.service.impl.AuthServiceImpl;
import com.example.graderbackend.service.impl.EmailVerificationServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private EmailVerificationServiceImpl emailVerificationService;

    @PostMapping("/signUp")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        authService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getConfirmPassword()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<AuthResponse> verifyEmail(@RequestBody TokenRequest request, HttpServletResponse response) {
        return emailVerificationService.verifyEmailAndReturnUser(request.getToken())
                .map(user -> {
                    TokenResponse tokenResponse = authService.createTokenResponse(user.getEmail());
                    Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(604800); // 7 วัน
                    response.addCookie(cookie);

                    AuthResponse authResponse = new AuthResponse(tokenResponse.getAccessToken(), user);
                    return ResponseEntity.ok(authResponse);
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> login(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        AuthResponse authResponse = authService.loginAndGetUser(registerRequest, response);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refreshTokenCookie(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(401).body("No refresh token found");
        }

        TokenResponse tokenResponse = authService.refreshAccessToken(refreshToken);
        Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(604800);
        response.addCookie(cookie);

        return ResponseEntity.ok("{\"accessToken\":\"" + tokenResponse.getAccessToken() + "\"}");
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        authService.forgotPasswordVerification(request.getEmail());
        return ResponseEntity.ok("Password reset email sent successfully!");
    }

    @PostMapping("/password/verify")
    public ResponseEntity<String> verifyResetToken(@RequestBody TokenRequest tokenRequest) {
        boolean valid = emailVerificationService.isValidEmailToken(tokenRequest.getToken());
        return ResponseEntity.ok(valid
                ? "Token valid. You can now reset your password."
                : "Invalid or expired token.");
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean success = emailVerificationService.resetPassword(
                resetPasswordRequest.getToken(),
                resetPasswordRequest.getNewPassword(),
                resetPasswordRequest.getConfirmPassword()
        );
        return ResponseEntity.ok(success
                ? "Password reset successfully!"
                : "Invalid or expired token.");
    }
}