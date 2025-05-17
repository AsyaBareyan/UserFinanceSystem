package org.example.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtTestUtil {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "f2b35778641bdc4d1fa039daa514e079fb556839c0b9fc28dca64c3c2861b05f".getBytes()
    );

    public static String generateToken(Long userId) {
        return Jwts.builder()
                .claim("USER_ID", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
