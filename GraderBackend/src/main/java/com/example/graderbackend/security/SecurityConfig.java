package com.example.graderbackend.security;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    // Security filter chain
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // =============================
                        // AuthController
                        // =============================
                        .requestMatchers("/auth/signUp",
                                "/auth/signIn",
                                "/auth/email/verify",
                                "/auth/password/forgot",
                                "/auth/password/verify",
                                "/auth/password/reset")
                        .permitAll()
                        .requestMatchers("/auth/refresh").authenticated()

                        // =============================
                        // ProblemController
                        // =============================
                        .requestMatchers(HttpMethod.GET, "/problem/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/problem").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/problem/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/problem/**").hasRole("ADMIN")

                        // =============================
                        // ProblemTagController
                        // =============================
                        .requestMatchers(HttpMethod.GET, "/problem-tag/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/problem-tag").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/problem-tag/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/problem-tag/**").hasRole("ADMIN")

                        // =============================
                        // SubmissionController
                        // =============================
                        .requestMatchers(HttpMethod.POST, "/submissions").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/submissions/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/submissions/my/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/submissions/{id}").hasAnyRole("USER", "ADMIN") // ตรวจ ownership ใน service

                        // =============================
                        // TagController
                        // =============================
                        .requestMatchers(HttpMethod.GET, "/tag/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tag").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tag/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tag/**").hasRole("ADMIN")

                        // =============================
                        // TestCaseController
                        // =============================
                        .requestMatchers(HttpMethod.GET, "/tasks/**/testcases/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/tasks/**/testcases").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tasks/**/testcases/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tasks/**/testcases/**").hasRole("ADMIN")

                        // =============================
                        // UserController
                        // =============================
                        .requestMatchers(HttpMethod.PUT, "/user/update").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")

                        // =============================
                        // Any other request
                        // =============================
                        .anyRequest().authenticated()
                )
                // add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    // CORS config
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}