/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.User
 *  com.example.graderbackend.repository.UserRepository
 *  com.example.graderbackend.service.CurrentSessionService
 *  com.example.graderbackend.service.impl.CurrentSessionServiceImpl
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.core.Authentication
 *  org.springframework.security.core.context.SecurityContextHolder
 *  org.springframework.security.core.userdetails.UserDetails
 *  org.springframework.stereotype.Service
 */
package com.example.graderbackend.service.impl;

import com.example.graderbackend.entity.User;
import com.example.graderbackend.repository.UserRepository;
import com.example.graderbackend.service.CurrentSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentSessionServiceImpl
implements CurrentSessionService {
    @Autowired
    private UserRepository userRepository;

    private Authentication retrieveAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getCurrentUser() {
        Authentication authentication = this.retrieveAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Authenticated user not found");
        }
        Object principal = authentication.getPrincipal();
        String email = principal instanceof UserDetails ? ((UserDetails)principal).getUsername() : principal.toString();
        return (User)this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with username: " + email));
    }
}

