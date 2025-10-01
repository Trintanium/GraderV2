package com.example.graderbackend.service.impl;

import com.example.graderbackend.entity.User;
import com.example.graderbackend.exception.AuthenticatedUserNotFoundException;
import com.example.graderbackend.exception.UserNotFoundException;
import com.example.graderbackend.repository.UserRepository;
import com.example.graderbackend.service.CurrentSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentSessionServiceImpl implements CurrentSessionService {

    @Autowired
    private UserRepository userRepository;

    private Authentication retrieveAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = this.retrieveAuthentication();
        if (authentication == null) {
            throw new AuthenticatedUserNotFoundException();
        }

        Object principal = authentication.getPrincipal();
        String email = principal instanceof UserDetails
                ? ((UserDetails) principal).getUsername()
                : principal.toString();

        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}