package com.vhealth.dto;

import java.util.Date;
import java.util.Map;


public class ResponseStatus {
	 private int code;

	    private String message;

	    private Long timestamp = new Date().getTime();

	    private Map<String, String> errors;

	    public ResponseStatus(int code, String message) {
	        super();
	        this.code = code;
	        this.message = message;
	    }
	    public ResponseStatus(ResponseCode statusCode) {
	        this(statusCode.getCode(), statusCode.getMessage());
	    }

	    public ResponseStatus(ResponseCode responseCode, Map<String, String> errors) {
	        this(responseCode.getCode(), responseCode.getMessage());
	        this.errors = errors;
	    }

	    public ResponseStatus() {}

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public Long getTimestamp() {
	        return timestamp;
	    }

	    public void setTimestamp(Long timestamp) {
	        this.timestamp = timestamp;
	    }

	    public Map<String, String> getErrors() {
	        return errors;
	    }

	    public void setErrors(Map<String, String> errors) {
	        this.errors = errors;
	    }

	    @Override
	    public String toString() {
	        return "ResponseStatus{" +
	                "code=" + code +
	                ", message='" + message + '\'' +
	                ", timestamp=" + timestamp +
	                ", errors=" + errors +
	                '}';
	    }

}
