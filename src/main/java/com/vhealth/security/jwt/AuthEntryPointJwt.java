package com.vhealth.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vhealth.exception.TokenRefreshException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		
		System.out.println("AuthEntryPointJwt.commence()");
		final String TOKEN_EXPIRED = (String) request.getAttribute("TOKEN_EXPIRED");
		final String AUTH_NOT_FOUND = (String) request.getAttribute("AUTHORIZATION_NOT_FOUND_IN_HEADERS");
		final String SIGNATURE_NOT_FOUND = (String) request.getAttribute("SIGNATURE_EXCEPTION");
		final String MALFORMED_EXCEPTION = (String) request.getAttribute("MALFORMED_EXCEPTION");

		
		final Map<String, Object> body = new HashMap<>();

		if (TOKEN_EXPIRED != null && TOKEN_EXPIRED.equalsIgnoreCase("TOKEN_EXPIRED")) {
			System.out.println("AuthEntryPointJwt.commence() iff");
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
			body.put("error", "Failure");
			body.put("message", "Token Expired!");
		} else if (AUTH_NOT_FOUND != null && AUTH_NOT_FOUND.equalsIgnoreCase("AUTHORIZATION_NOT_FOUND_IN_HEADERS")) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
			body.put("error", "Failure");
			body.put("message", "Token Not Found In Headers!");
		} else if (SIGNATURE_NOT_FOUND != null && SIGNATURE_NOT_FOUND.equalsIgnoreCase("SIGNATURE_EXCEPTION")) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
			body.put("error", "Failure");
			body.put("message", "Token Is Not Valid Format!");
		}
		
		else  if(MALFORMED_EXCEPTION != null && MALFORMED_EXCEPTION.equalsIgnoreCase("MALFORMED_EXCEPTION")){
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
			body.put("error", "Failure");
			body.put("message", "Token Is Not Valid Format! Missing Some Char..");
		}

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);

		// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}

	/*
	 * public void commence(HttpServletRequest request, HttpServletResponse
	 * response, AuthenticationException authException) throws IOException,
	 * ServletException { final Map<String, Object> body = new HashMap<>(); //
	 * System.out.println("AuthEntryPointJwt.commence()" + //
	 * authException.getMessage());
	 * System.out.println("AuthEntryPointJwt.commence()::print:" +
	 * authException.getMessage()); if
	 * (authException.getMessage().equals("Bad credentials")) {
	 * System.out.println("AuthEntryPointJwt.commence( iff::)" +
	 * authException.getMessage());
	 * response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	 * 
	 * body.put("status", HttpServletResponse.SC_BAD_REQUEST); body.put("error",
	 * "Failed"); body.put("message", "Invalid Credentials....!");
	 * 
	 * }
	 * 
	 * else if(authException.getMessage().
	 * equals("Full authentication is required to access this resource")){
	 * response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	 * response.setStatus(HttpServletResponse.SC_NOT_FOUND );
	 * 
	 * body.put("status", HttpServletResponse.SC_NOT_FOUND); body.put("error",
	 * "Failed"); body.put("message", "Invalid Credentials....!"); }
	 * 
	 * 
	 * else { response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	 * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	 * 
	 * body.put("status", HttpServletResponse.SC_UNAUTHORIZED); body.put("error",
	 * "Failed"); body.put("message", "Token Expired..!"); }
	 * 
	 * final ObjectMapper mapper = new ObjectMapper();
	 * mapper.writeValue(response.getOutputStream(), body);
	 * 
	 * // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: //
	 * Unauthorized"); }
	 */
}
