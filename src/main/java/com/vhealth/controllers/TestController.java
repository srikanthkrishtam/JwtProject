package com.vhealth.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vhealth.exception.TokenRefreshException;
import com.vhealth.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	@Autowired
	JwtUtils jwtUtils;

	@GetMapping("/all")
	public TokenRefreshException allAccess() {

		/*
		 * try { System.out.println("TestController.allAccess()11");
		 * jwtUtils.validateJwtToken(
		 * "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzcmlrYW50aCIsImlhdCI6MTY2MTk1MDExOCwiZXhwIjoxNjYxOTUwMTQ4fQ.w9NG6NagIDJrmvJiVdhpBJEJ7mAi219y9PQr1H3k6OumQdKYPF10AjgBvUHBXaKVNySG9UFXNeTs3pBJpiQVTg"
		 * ); return new TokenRefreshException("", ""); } catch (TokenRefreshException
		 * e) { System.out.println("TestController.allAccess() catch"); return new
		 * TokenRefreshException("cant set auth", "Token Expired");
		 * 
		 * }
		 */
		return new TokenRefreshException("cant set auth", "Token Expired");
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@GetMapping("/get")
	public String getMet(@RequestHeader Map<String, String> headers) {
		final String requestTokenHeader = headers.get("Authorization");
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			String jwtToken = requestTokenHeader.substring(7);
		}
		System.out.println("TestController.getMet()");

		return "Admin Board.";
	}
}
