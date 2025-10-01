/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.TokenResponse
 *  com.example.graderbackend.entity.User
 *  com.example.graderbackend.exceptions.JWTExpiredException
 *  com.example.graderbackend.exceptions.JWTInvalidException
 *  com.example.graderbackend.model.AuthUserDetails
 *  com.example.graderbackend.repository.UserRepository
 *  com.example.graderbackend.security.JWTUtils
 *  com.nimbusds.jose.JOSEException
 *  com.nimbusds.jose.JWSAlgorithm
 *  com.nimbusds.jose.JWSHeader$Builder
 *  com.nimbusds.jose.JWSSigner
 *  com.nimbusds.jose.JWSVerifier
 *  com.nimbusds.jose.crypto.RSASSASigner
 *  com.nimbusds.jose.crypto.RSASSAVerifier
 *  com.nimbusds.jose.jwk.RSAKey
 *  com.nimbusds.jose.jwk.gen.RSAKeyGenerator
 *  com.nimbusds.jwt.JWTClaimsSet
 *  com.nimbusds.jwt.JWTClaimsSet$Builder
 *  com.nimbusds.jwt.SignedJWT
 *  jakarta.annotation.PostConstruct
 *  lombok.Generated
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.security.core.userdetails.UserDetails
 *  org.springframework.stereotype.Service
 */
package com.example.graderbackend.security;

import com.example.graderbackend.dto.TokenResponse;
import com.example.graderbackend.entity.User;
import com.example.graderbackend.exception.JWTExpiredException;
import com.example.graderbackend.exception.JWTInvalidException;
import com.example.graderbackend.model.AuthUserDetails;
import com.example.graderbackend.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JWTUtils {
    @Value(value="${app.spring.jwt.key-id}")
    private String keyId;
    private RSAKey rsaPrivateJWK;
    private RSAKey rsaPublicJWK;
    private final long ACCESS_TOKEN_EXPIRATION = 86400000L;
    private final long REFRESH_TOKEN_EXPIRATION = 604800000L;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        try {
            this.rsaPrivateJWK = (RSAKey)new RSAKeyGenerator(2048).keyID(this.keyId).generate();
            this.rsaPublicJWK = this.rsaPrivateJWK.toPublicJWK();
        }
        catch (JOSEException e) {
            throw new RuntimeException("Failed to generate JWK keys", e);
        }
    }

    private String generateToken(UserDetails user, long expirationMillis, String type) {
        try {
            RSASSASigner signer = new RSASSASigner(this.rsaPrivateJWK);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(user.getUsername()).issuer("graderADMIN").issueTime(new Date()).expirationTime(new Date(System.currentTimeMillis() + expirationMillis)).claim("uid", (Object)((AuthUserDetails)user).getId()).claim("authorities", (Object)user.getAuthorities()).claim("typ", (Object)type).build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(this.rsaPrivateJWK.getKeyID()).build(), claimsSet);
            signedJWT.sign((JWSSigner)signer);
            return signedJWT.serialize();
        }
        catch (JOSEException e) {
            throw new RuntimeException("Token generation failed", e);
        }
    }

    public String generateAccessToken(UserDetails user) {
        return this.generateToken(user, 86400000L, "ACCESS_TOKEN");
    }

    public String generateRefreshToken(UserDetails user) {
        return this.generateToken(user, 604800000L, "REFRESH_TOKEN");
    }

    public TokenResponse generateTokenResponse(UserDetails user) {
        return new TokenResponse(this.generateAccessToken(user), this.generateRefreshToken(user));
    }

    public void validateTokenOrThrow(String token, String expectedType) {
        try {
            SignedJWT signedJWT = SignedJWT.parse((String)token);
            RSASSAVerifier verifier = new RSASSAVerifier(this.rsaPublicJWK);
            if (!signedJWT.verify((JWSVerifier)verifier)) {
                throw new JWTInvalidException("Invalid JWT signature");
            }
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            if (claims.getExpirationTime().before(new Date())) {
                throw new JWTExpiredException("JWT expired");
            }
            String type = claims.getStringClaim("typ");
            if (!expectedType.equals(type)) {
                throw new JWTInvalidException("Invalid JWT type");
            }
        }
        catch (JOSEException | ParseException e) {
            throw new JWTInvalidException("Failed to parse JWT", e);
        }
    }

    public String refreshAccessToken(String refreshToken) {
        this.validateTokenOrThrow(refreshToken, "REFRESH_TOKEN");
        try {
            SignedJWT signedJWT = SignedJWT.parse((String)refreshToken);
            String email = signedJWT.getJWTClaimsSet().getSubject();
            User user = (User)this.userRepository.findByEmail(email).orElseThrow(() -> new JWTInvalidException("User not found"));
            return this.generateAccessToken((UserDetails)new AuthUserDetails(user));
        }
        catch (ParseException e) {
            throw new JWTInvalidException("Failed to parse refresh token", (Throwable)e);
        }
    }

    public Map<String, Object> getJWTClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse((String)token);
            return signedJWT.getJWTClaimsSet().getClaims();
        }
        catch (ParseException e) {
            throw new JWTInvalidException("Cannot parse JWT claims", (Throwable)e);
        }
    }

    public boolean isExpiredClaims(Map<String, Object> claims) {
        Date expDate = (Date)claims.get("exp");
        return expDate != null && expDate.before(new Date());
    }

    @Generated
    public RSAKey getRsaPrivateJWK() {
        return this.rsaPrivateJWK;
    }

    @Generated
    public RSAKey getRsaPublicJWK() {
        return this.rsaPublicJWK;
    }
}

