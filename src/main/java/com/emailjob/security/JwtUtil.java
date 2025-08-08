package com.emailjob.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "your_secret_key_here"; // move to config in production
	private final long EXPIRATION_TIME = 86400000; // 1 day in ms

	// ===================== GENERATE TOKEN =====================
	// Custom token with id, email, role, adminId
	public String generateToken(Long id, String email, String role, Long adminId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", id);
		claims.put("role", role);
		claims.put("adminId", adminId);
		return createToken(claims, email);
	}

	public String generateTokenWithExtraClaims(Map<String, Object> extraClaims, String username) {
		return Jwts.builder().setClaims(extraClaims).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject) // here subject = email
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	// ===================== EXTRACT EMAIL =====================
	public String getEmailFromToken(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// ===================== VALIDATE TOKEN =====================
	public boolean validateToken(String token, UserDetails userDetails) {
		String email = getEmailFromToken(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// ===================== HELPER METHODS =====================
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}
