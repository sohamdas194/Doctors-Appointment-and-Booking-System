package com.wipro.service;

import com.wipro.model.AuthenticatedUser;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtServiceImpl {

    private JwtEncoder jwtEncoder;

    public JwtServiceImpl(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(AuthenticatedUser authUser) {
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 300))
                .subject(authUser.getUserId().toString())
                .claim("scope", authUser.getUserRole())
                .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters
                                .from(claims)
                ).getTokenValue();
    }
}
