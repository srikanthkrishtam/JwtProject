package com.vhealth.advice;

import java.security.SignatureException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.vhealth.dto.CloudBaseException;
import com.vhealth.dto.ResponseCode;
import com.vhealth.exception.TokenRefreshException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@RestControllerAdvice
public class TokenControllerAdvice {
	private static final Logger LOG = LoggerFactory.getLogger(TokenControllerAdvice.class);

	@ExceptionHandler(value = TokenRefreshException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
		System.out.println("TokenControllerAdvice.handleTokenRefreshException()");
		return new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(),
				request.getDescription(false));
	}

	/**
	 * This method handles custom TejaCloudBaseException. Sets the custom exception
	 * message in the response
	 *
	 * @param exception
	 * @return ResponseEntity
	 */
	@ExceptionHandler(CloudBaseException.class)
	private ResponseEntity<ResponseObject<String>> processCustomException(CloudBaseException exception) {
		ResponseObject<String> response = new ResponseObject<>();
		System.out.println("TokenControllerAdvice.processCustomException(11)");
		response.setStatusFromEnum(ResponseCode.valueOf(exception.getMessage()));
		LOG.error("-- processCustomException() - Handled Base exception - {}", exception);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	

	/**
	 * Exception handler to handle MethodArgumentNotValidException.
	 *
	 * @param exception
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseObject<String>> processMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {

		Map<String, String> errors = new TreeMap<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String field = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(field, message);
		});
		ResponseObject<String> response = new ResponseObject<>(ResponseCode.BAD_REQUEST, errors);
		response.setStatusFromEnum(ResponseCode.BAD_REQUEST);
		LOG.error("-- processMethodArgumentNotValidException() - Handled Invalid Argument exception - {}", exception);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception handler to handle MethodArgumentTypeMismatchException.
	 *
	 * @param ex
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ResponseObject<String>> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex) {
		ResponseObject<String> response = new ResponseObject<>();
		response.setStatusFromEnum(ResponseCode.REQUEST_PARAMETER_TYPE_MISMATCH);

		LOG.error("-- handleMethodArgumentTypeMismatchException() - Handled MethodArgumentTypeMismatch exception - {}",
				ex);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception handler to handle MissingServletRequestParameterException.
	 *
	 * @param ex
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ResponseObject<String>> handleMissingServletRequestParameterException(
			MissingServletRequestParameterException ex) {
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error(
				"-- handleMissingServletRequestParameterException() - Handled MissingServletRequestParameter exception - {}",
				ex);
		response.setStatusFromEnum(ResponseCode.MISSING_REQUEST_PARAMETER);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ResponseObject<String>> handleSQLIntegrityConstraintViolationException(
			ConstraintViolationException ex) {
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error(
				"-- handleSQLIntegrityConstraintViolationException() - Handled SQLIntegrityConstraintViolationException exception - {}",
				ex);
		response.setStatusFromEnum(ResponseCode.DUPLICATE_ENTRY);
		return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ResponseObject<String>> handleDataIntegrityViolationException(
			DataIntegrityViolationException ex) {
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error("-- handleDataIntegrityViolationException() - Handled DataIntegrityViolationException exception - {}",
				ex.getMessage());
		response.setStatusFromEnum(ResponseCode.DUPLICATE_ENTRY);
		return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
	}

	/**
	 * Exception handler to handle HttpMediaTypeNotSupportedException.
	 *
	 * @param ex
	 * @return ResponseEntity
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ResponseObject<String>> handleHttpMediaTypeNotSupportedException(
			HttpMediaTypeNotSupportedException ex) {
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error("-- handleHttpMediaTypeNotSupportedException() - Handled HttpMediaTypeNotSupported exception - {}",
				ex);
		response.setStatusFromEnum(ResponseCode.INVALID_CONTENT_TYPE);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ResponseObject<String>> handleException(Exception ex) {
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error("-- handleException() - {}", ex);
		response.setStatusFromEnum(ResponseCode.UNKNOWN_ERROR_OCCURRED);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<ResponseObject<String>> AccessDenaid(AccessDeniedException ex) {
		System.out.println("TokenControllerAdvice.AccessDenaid()");
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error("-- handleException() - {}", ex);
		response.setStatusFromEnum(ResponseCode.UNKNOWN_ERROR_OCCURRED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
	

	
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<ResponseObject<String>> tokenExp(ExpiredJwtException ex) {
		System.out.println("TokenControllerAdvice.token Exp::;()");
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error("-- handleException() - {}", ex);
		response.setStatusFromEnum(ResponseCode.UNAUTHORIZED_REQUEST);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
	
	
	
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = SignatureException.class)
	public ResponseEntity<ResponseObject<String>> SignatureException(SignatureException ex) {
		System.out.println("TokenControllerAdvice.SignatureException Exp::;()");
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error("-- handleException() - {}", ex);
		response.setStatusFromEnum(ResponseCode.UNAUTHORIZED_REQUEST);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
	
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ResponseObject<String>> methodNotSupport(HttpRequestMethodNotSupportedException ex) {
		System.out.println("TokenControllerAdvice.HttpRequestMethodNotSupportedException Exp::;()");
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error("-- handleException() - {}", ex);
		response.setStatusFromEnum(ResponseCode.UNAUTHORIZED_REQUEST);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = MalformedJwtException.class)
	public ResponseEntity<ResponseObject<String>> MalformedJwtException(MalformedJwtException ex) {
		System.out.println("TokenControllerAdvice.HttpRequestMethodNotSupportedException Exp::;()");
		ResponseObject<String> response = new ResponseObject<>();
		LOG.error("-- handleException() - {}", ex);
		response.setStatusFromEnum(ResponseCode.ACCESS_TOKEN_EXPIRED);
		return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
	}
	
	
}
