package com.vhealth.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vhealth.dto.CloudBaseException;
import com.vhealth.dto.ResponseCode;
import com.vhealth.exception.TokenRefreshException;
import com.vhealth.security.services.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, TokenRefreshException {

		final String requestTokenHeader = request.getHeader("Authorization");
		System.out.println("AuthTokenFilter.doFilterInternal(1)" + requestTokenHeader);
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			System.out.println("AuthTokenFilter.doFilterInternal()" + jwtToken);
			username = jwtUtils.getUserNameFromJwtToken(jwtToken);
			/*
			 * try { username = jwtUtils.getUserNameFromJwtToken(jwtToken); } catch
			 * (IllegalArgumentException e) {
			 * 
			 * throw new CloudBaseException(ResponseCode.BAD_CREDENTIALS); } catch
			 * (ExpiredJwtException e) { System.out.println("JWT Token has expired");
			 * //request.setAttribute("TOKEN_EXPIRED", "TOKEN_EXPIRED"); throw new
			 * CloudBaseException(1,""); } catch (CloudBaseException e) {
			 * System.out.println("AuthTokenFilter.doFilterInternal()Cloud"); throw new
			 * CloudBaseException(ResponseCode.BAD_CREDENTIALS);
			 * 
			 * } catch (io.jsonwebtoken.SignatureException s) {
			 * System.out.println("AuthTokenFilter.doFilterInternal()SignatureException::");
			 * request.setAttribute("SIGNATURE_EXCEPTION", "SIGNATURE_EXCEPTION"); } catch
			 * (MalformedJwtException n) { System.out.
			 * println("AuthTokenFilter.MalformedJwtException if any char miss in token()");
			 * request.setAttribute("MALFORMED_EXCEPTION", "MALFORMED_EXCEPTION");
			 * 
			 * }
			 */
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
			request.setAttribute("AUTHORIZATION_NOT_FOUND_IN_HEADERS", "AUTHORIZATION_NOT_FOUND_IN_HEADERS");

		}

		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtUtils.validateJwtToken(jwtToken, userDetails)) {
System.out.println("AuthTokenFilter.doFilterInternal() if::");
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

	/*
	 * try { System.out.println("AuthTokenFilter.doFilterInternal(1)"); String jwt =
	 * parseJwt(request); System.out.println("AuthTokenFilter.doFilterInternal(2)");
	 * if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
	 * System.out.println("AuthTokenFilter.doFilterInternal(3)"); String username =
	 * jwtUtils.getUserNameFromJwtToken(jwt); UserDetails userDetails =
	 * userDetailsService.loadUserByUsername(username);
	 * UsernamePasswordAuthenticationToken authentication = new
	 * UsernamePasswordAuthenticationToken( userDetails, null,
	 * userDetails.getAuthorities()); authentication.setDetails(new
	 * WebAuthenticationDetailsSource().buildDetails(request));
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(authentication);
	 * System.out.println("AuthTokenFilter.doFilterInternal()44"); } } catch
	 * (Exception e) { // logger.error("Cannot set user authentication: {}",
	 * e.getMessage());
	 * System.out.println("AuthTokenFilter.doFilterInternal()catch::::"+e.getMessage
	 * () );
	 * 
	 * //throw new TokenRefreshException("Cannot set user authentication ",
	 * "Token Expired"); } filterChain.doFilter(request, response); }
	 * 
	 * private String parseJwt(HttpServletRequest request) { String headerAuth =
	 * request.getHeader("Authorization");
	 * 
	 * if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
	 * return headerAuth.substring(7, headerAuth.length()); }
	 * 
	 * return null; }
	 */
}
