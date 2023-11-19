package com.vhealth.dto;


public class CloudBaseException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private final int code;
    private final String message;

    private final Object object;

    public CloudBaseException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
        this.object = null;
    }
    

    public CloudBaseException(ResponseCode responseStatus, Object object) {
        super();
        this.code = responseStatus.getCode();
        this.message = responseStatus.getMessage();
        this.object = object;
    }

    public CloudBaseException(ResponseCode responseStatus) {
        super();
        this.code = responseStatus.getCode();
        this.message = responseStatus.getMessage();
        this.object=null;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getObject() {
        return object;
    }

    /**
     * To avoid stack trace
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
	public String toString() {
		return "CloudBaseException []";
	}
}
