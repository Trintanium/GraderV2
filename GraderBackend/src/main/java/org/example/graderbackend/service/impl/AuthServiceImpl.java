/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.AuthResponse
 *  com.example.graderbackend.dto.RegisterRequest
 *  com.example.graderbackend.dto.TokenResponse
 *  com.example.graderbackend.dto.entity.UserDto
 *  com.example.graderbackend.entity.EmailVerification
 *  com.example.graderbackend.entity.User
 *  com.example.graderbackend.repository.UserRepository
 *  com.example.graderbackend.security.JWTUtils
 *  com.example.graderbackend.service.AuthService
 *  com.example.graderbackend.service.EmailVerificationService
 *  com.example.graderbackend.service.ModelMapperService
 *  com.example.graderbackend.service.impl.AuthServiceImpl
 *  jakarta.servlet.http.Cookie
 *  jakarta.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.authentication.AuthenticationManager
 *  org.springframework.security.authentication.BadCredentialsException
 *  org.springframework.security.authentication.UsernamePasswordAuthenticationToken
 *  org.springframework.security.core.Authentication
 *  org.springframework.security.core.userdetails.UserDetails
 *  org.springframework.security.core.userdetails.UserDetailsService
 *  org.springframework.security.crypto.password.PasswordEncoder
 *  org.springframework.stereotype.Service
 */
package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.AuthResponse;
import com.example.graderbackend.dto.RegisterRequest;
import com.example.graderbackend.dto.TokenResponse;
import com.example.graderbackend.dto.entity.UserDto;
import com.example.graderbackend.entity.EmailVerification;
import com.example.graderbackend.entity.User;
import com.example.graderbackend.repository.UserRepository;
import com.example.graderbackend.security.JWTUtils;
import com.example.graderbackend.service.AuthService;
import com.example.graderbackend.service.EmailVerificationService;
import com.example.graderbackend.service.ModelMapperService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl
implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailVerificationService verificationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapperService mapperService;

    public void registerUser(String username, String email, String password, String confirmPassword) {
        User user = this.userRepository.findByEmail(email).orElse(null);
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (user != null) {
            if (user.isEmailVerified()) {
                throw new IllegalArgumentException("Email already verified");
            }
            this.sendEmailVerification(user);
            return;
        }
        user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode((CharSequence)password));
        this.userRepository.save(user);
        this.sendEmailVerification(user);
    }

    public AuthResponse loginAndGetUser(RegisterRequest request, HttpServletResponse response) {
        try {
            this.authenticationManager.authenticate((Authentication)new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }
        catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
        User user = (User)this.userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException("Email not verified");
        }
        TokenResponse tokenResponse = this.createTokenResponse(user.getEmail());
        this.addRefreshTokenCookie(response, tokenResponse.getRefreshToken());
        return new AuthResponse(tokenResponse.getAccessToken(), (UserDto)this.mapperService.toDto(user, UserDto.class));
    }

    public TokenResponse createTokenResponse(String email) {
        UserDetails user = this.userDetailsService.loadUserByUsername(email);
        String accessToken = this.jwtUtils.generateAccessToken(user);
        String refreshToken = this.jwtUtils.generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        String newAccessToken = this.jwtUtils.refreshAccessToken(refreshToken);
        return new TokenResponse(newAccessToken, refreshToken);
    }

    public void forgotPasswordVerification(String email) {
        System.out.println(email);
        User user = (User)this.userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException("Email not verified");
        }
        EmailVerification emailVerification = this.verificationService.createEmailVerification(user);
        this.verificationService.sendForgotPasswordEmail(emailVerification);
    }

    public void sendEmailVerification(User user) {
        EmailVerification emailVerification = this.verificationService.createEmailVerification(user);
        this.verificationService.sendVerificationEmail(emailVerification);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(604800);
        response.addCookie(cookie);
    }
}

