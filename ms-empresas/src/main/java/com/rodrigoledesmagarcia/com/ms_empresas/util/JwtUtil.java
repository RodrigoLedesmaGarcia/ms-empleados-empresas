package com.rodrigoledesmagarcia.com.ms_empresas.util;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;

public class JwtUtil {
    // Usa una clave secreta larga y privada (puedes ponerla en application.properties)
    private static final String SECRET = "miSuperClaveSecretaQueDebeSerMuyLarga1234!";
    private static final SecretKey KEY = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
    private static final long EXPIRATION_SECONDS = 3600; // 1 hora

    public static String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(EXPIRATION_SECONDS)))
                .signWith(KEY)
                .compact();
    }

    public static String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
