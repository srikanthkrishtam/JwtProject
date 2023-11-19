package com.vhealth.advice;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vhealth.dto.ResponseCode;
import com.vhealth.dto.ResponseStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject<T> {

  private  com.vhealth.dto.ResponseStatus status;

  private T response;

  public ResponseObject() {
    super();
  }

  public ResponseObject(ResponseStatus responseStatus) {
    this.status = responseStatus;
  }

  public ResponseObject(ResponseCode statusCode) {
    this.status = new ResponseStatus(statusCode);
  }

  public ResponseObject(T response, ResponseCode responseCode) {
    this.response = response;
    this.status = new ResponseStatus(responseCode.getCode(), responseCode.getMessage());
  }

  public ResponseObject(ResponseCode responseCode, Map<String, String> errors) {
    this.status = new ResponseStatus(responseCode, errors);
  }

  public T getResponse() {
    return response;
  }

  public void setResponse(T response) {
    this.response = response;
  }

  public ResponseStatus getStatus() {
    return status;
  }

  public void setStatusFromEnum(ResponseCode responseCode) {
    this.status = new ResponseStatus(responseCode.getCode(), responseCode.getMessage());
  }

  public void setStatus(ResponseStatus status) {
    this.status = status;
  }

    /**
     * Success response direct method
     *
     * @return ResponseObject with SUCCESS status
     */
    public static <T> ResponseObject<T> success(T response) {
        return new ResponseObject<>(response, ResponseCode.SUCCESS);
    }

  @Override
  public String toString() {
    return "ResponseObject [status=" + status + ", response=" + response + "]";
  }
}
