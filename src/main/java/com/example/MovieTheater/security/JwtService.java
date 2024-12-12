package com.example.MovieTheater.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";  // Your secret key
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // Token expiration time (10 hours)

    // Extract the username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generate a JWT token for the user
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Generate a JWT token with additional claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)  // Add custom claims if necessary
                .setSubject(userDetails.getUsername())  // Set the subject (usually the username or email)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set the issue time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Set expiration time
                .signWith(getSigningKey())  // Sign the token using the secret key
                .compact();  // Generate and return the token
    }

    // Check if the token is valid
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration date from the JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()  // Use Jwts.parser() for versions <= 0.12.x
                .setSigningKey(getSigningKey())  // Set the signing key for verification
                .parseClaimsJws(token)  // Parse the JWT token and get claims
                .getBody();  // Return the claims body
    }

    // Retrieve the signing key from the secret key
    private SecretKey getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);  // Decode the secret key from Base64
        return Keys.hmacShaKeyFor(keyBytes);  // Generate a HMAC-SHA key for signing
    }
}
