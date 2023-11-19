package com.vhealth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

	private String token;
	private String message;
	private static final long serialVersionUID = 1L;

	public TokenRefreshException(String token, String message) {
		// super(String.format("Failed for [%s]: %s", token, message));
		this.message = message;
		this.token = token;

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
