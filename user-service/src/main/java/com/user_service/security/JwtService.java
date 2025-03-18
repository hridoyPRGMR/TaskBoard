package com.user_service.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	
	private static final String INTERNAL_SECRET_KEY_PATH = "internal-secret.key";
	private final String INTERNAL_KEY;

	public JwtService() {
		String envInternalKey = getInternalKey();
        this.INTERNAL_KEY = (envInternalKey == null || envInternalKey.isEmpty()) ? generateSecretKey() : envInternalKey;
	}
	
	private String getInternalKey() {
        try {
            return new String(Files.readAllBytes(Paths.get(INTERNAL_SECRET_KEY_PATH))).trim();
        } catch (IOException e) {
            return "";
        }
    }

	private String generateSecretKey() {
		try {
			String secretKey = "";
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
			return secretKey;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}
	
	 private SecretKey getInternalKeyObj() {
	        return Keys.hmacShaKeyFor(INTERNAL_KEY.getBytes());
	 }

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean validateInternalToken(String token, String username) {
		final String tokenUsername = extractUsername(token);
		return (tokenUsername.equals(username) && !isTokenExpired(token));
	}

	boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getInternalKeyObj()).build().parseSignedClaims(token).getPayload();
	}
	
	public String getAuthenticatedUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName();
		}
		return null;
	}
}
