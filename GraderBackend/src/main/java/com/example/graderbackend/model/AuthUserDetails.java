/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.User
 *  com.example.graderbackend.model.AuthUserDetails
 *  org.springframework.security.core.GrantedAuthority
 *  org.springframework.security.core.authority.SimpleGrantedAuthority
 *  org.springframework.security.core.userdetails.UserDetails
 */
package com.example.graderbackend.model;

import com.example.graderbackend.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUserDetails
implements UserDetails {
    private final User user;
    private Collection<? extends GrantedAuthority> authorities;

    public AuthUserDetails(User user) {
        this.user = user;
    }

    public AuthUserDetails(Long id, String username, Collection<? extends GrantedAuthority> authorities) {
        this.user = new User();
        this.user.setId(id);
        this.user.setUsername(username);
        this.authorities = authorities;
    }

    public Long getId() {
        return this.user.getId();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.user.getRole().name()));
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    public String getUsername() {
        return this.user.getEmail();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return this.user.isEmailVerified();
    }
}

