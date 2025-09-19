/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.exceptions.JWTExpiredException
 *  com.example.graderbackend.exceptions.JWTInvalidException
 *  com.example.graderbackend.security.JWTAuthenticationFilter
 *  com.example.graderbackend.security.JWTUtils
 *  com.example.graderbackend.service.impl.UserDetailServiceImpl
 *  jakarta.servlet.FilterChain
 *  jakarta.servlet.ServletException
 *  jakarta.servlet.ServletRequest
 *  jakarta.servlet.ServletResponse
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.authentication.UsernamePasswordAuthenticationToken
 *  org.springframework.security.core.Authentication
 *  org.springframework.security.core.context.SecurityContextHolder
 *  org.springframework.security.core.userdetails.UserDetails
 *  org.springframework.security.web.authentication.WebAuthenticationDetailsSource
 *  org.springframework.stereotype.Component
 *  org.springframework.web.filter.OncePerRequestFilter
 */
package com.example.graderbackend.security;

import com.example.graderbackend.exceptions.JWTExpiredException;
import com.example.graderbackend.exceptions.JWTInvalidException;
import com.example.graderbackend.security.JWTUtils;
import com.example.graderbackend.service.impl.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthenticationFilter
extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);
        try {
            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                Date expDate;
                String token = authHeader.substring(BEARER_PREFIX.length());
                this.jwtUtils.validateTokenOrThrow(token, "ACCESS_TOKEN");
                Map claims = this.jwtUtils.getJWTClaims(token);
                Object expObj = claims.get("exp");
                if (expObj instanceof Long) {
                    expDate = new Date((Long)expObj * 1000L);
                } else if (expObj instanceof Integer) {
                    expDate = new Date(((Integer)expObj).longValue() * 1000L);
                } else if (expObj instanceof Date) {
                    expDate = (Date)expObj;
                } else {
                    throw new JWTInvalidException("Invalid exp claim type");
                }
                if (expDate.before(new Date())) {
                    throw new JWTExpiredException("Expired JWT token");
                }
                if (!"ACCESS_TOKEN".equals(claims.get("typ"))) {
                    throw new JWTInvalidException("Invalid JWT token type");
                }
                String email = (String)claims.get("sub");
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken((Object)userDetails, null, userDetails.getAuthorities());
                    auth.setDetails((Object)new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication((Authentication)auth);
                }
            }
        }
        catch (JWTExpiredException | JWTInvalidException e) {
            SecurityContextHolder.clearContext();
            response.setStatus(401);
            response.getWriter().write(e.getMessage());
            return;
        }
        filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
    }
}

