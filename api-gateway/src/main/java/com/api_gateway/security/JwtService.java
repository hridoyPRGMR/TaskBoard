package com.api_gateway.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	private static final String SECRET_KEY_PATH = "jwt-secret.key";
	private final String SECRET_KEY;
	private static final long CLIENT_TOKEN_EXPIRATION = 86400000;  // 24 hours
	
	
	private static final String INTERNAL_SECRET_KEY_PATH = "internal-secret.key";
	private final String INTERNAL_KEY;
	 private static final long INTERNAL_TOKEN_EXPIRATION = 86400000; //5min

	private JwtService() {
		String envSecret = getSecretKey();
		this.SECRET_KEY = envSecret.isEmpty() || envSecret == null ? generateSecretKey() : envSecret;
		
		String envInternalKey = getInternalKey();
        this.INTERNAL_KEY = (envInternalKey == null || envInternalKey.isEmpty()) ? generateSecretKey() : envInternalKey;
	}

	private String getSecretKey() {
		try {
			return new String(Files.readAllBytes(Paths.get(SECRET_KEY_PATH))).trim();
		} catch (IOException e) {
			 return "";
		}
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
	
	public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
	
	public String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + INTERNAL_TOKEN_EXPIRATION))
				.signWith(getKey())
				.compact();
	}
	
	public String generateInternalToken(String subject) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + CLIENT_TOKEN_EXPIRATION))
				.signWith(getInternalKeyObj())
				.compact();
	}

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
	
	private SecretKey getInternalKeyObj() {
	     return Keys.hmacShaKeyFor(INTERNAL_KEY.getBytes());
	}
	 
	 
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean validateToken(String token, String username) {
		final String tokenUsername = extractUsername(token);
		return (tokenUsername.equals(username) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
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
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
	}
	
}
