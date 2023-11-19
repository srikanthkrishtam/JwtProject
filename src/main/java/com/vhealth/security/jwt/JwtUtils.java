package com.vhealth.security.jwt;

import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.vhealth.dto.CloudBaseException;
import com.vhealth.dto.JwtConstants;
import com.vhealth.dto.ResponseCode;
import com.vhealth.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${bezkoder.app.jwtSecret}")
	private String jwtSecret;

	@Value("${bezkoder.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generateJwtToken(UserDetailsImpl userPrincipal) {
		return generateTokenFromUsername(userPrincipal.getUsername());
	}

	public String generateTokenFromUsername(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, JwtConstants.SECRETKEY).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(JwtConstants.SECRETKEY).parseClaimsJws(token).getBody().getSubject();
	}

	/*
	 * public boolean validateJwtToken(String authToken) throws
	 * TokenRefreshException { try {
	 * Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken); return
	 * true; } catch (SignatureException e) {
	 * System.out.println("JwtUtils.validateJwtToken()::::"+e.getMessage());
	 * //logger.error("Invalid JWT signature: {}", e.printStackTrace()); } catch
	 * (MalformedJwtException e) { logger.error("Invalid JWT token: {}",
	 * e.getMessage()); } catch (ExpiredJwtException e) {
	 * System.out.println("JwtUtils.validateJwtToken() Expired:::");
	 * logger.error("JWT token is expired: {}:::", e.getMessage()+":;;!!!"); throw
	 * new TokenRefreshException("Cannot set user authentication ",
	 * "Token Expired");
	 * 
	 * } catch (UnsupportedJwtException e) {
	 * logger.error("JWT token is unsupported: {}", e.getMessage()); } catch
	 * (IllegalArgumentException e) { logger.error("JWT claims string is empty: {}",
	 * e.getMessage()); throw new
	 * TokenRefreshException("Token cant be null or empty ", e.getMessage());
	 * 
	 * }catch (Exception e) {
	 * System.out.println("JwtUtils.validateJwtToken()  catchhh"); }
	 * 
	 * return false; }
	 */

	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	public boolean validateJwtToken(String jwtToken, UserDetails userDetails) {
		final String username = getUserNameFromJwtToken(jwtToken);
		System.out.println("JwtUtils.validateJwtToken()");
		if (isTokenExpired(jwtToken)) {
			System.out.println("JwtUtils.validateJwtToken() exp: if::");
			throw new CloudBaseException(ResponseCode.ACCESS_TOKEN_EXPIRED);

		}
		return (username.equals(userDetails.getUsername()));
	}

	public static void main(String k[]) {
		JwtUtils JwtUtils = new JwtUtils();
		String name = JwtUtils.getUserNameFromJwtToken(
				"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzcmlrYW50aCIsImlhdCI6MTY2MjI5MjIyNCwiZXhwIjoxNjYyMjkyNDA0fQ.aQGSRpyMiesEjhgOzRgdpD2cfYtuDyM5Q3o-61qlzQMVUvDok4V5lgIpJPaNyzNwgweJxvUwttJ5o6_08muZNg");
	System.out.println("JwtUtils.main()"+name);
	}
}
