package com.skripsi.spk.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY =
            "skripsi-spk-secret-key-256-bit-minimal";

    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 jam

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY) // ✅ INI YANG BENAR
                .compact();
    }
}
