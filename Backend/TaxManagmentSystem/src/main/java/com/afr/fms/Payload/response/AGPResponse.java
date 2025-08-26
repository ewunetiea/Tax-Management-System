package com.afr.fms.Payload.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@NoArgsConstructor
public class AGPResponse {
	
	public static final String ERROR = "error";
	  public static final String SUCCESS = "success";

	  private Map<String, Object> data = new HashMap<>();

	  public AGPResponse(String key, Object data) {
	    this.data.put(key, data);
	  }

	  @JsonAnySetter
	  public void addData(String key, Object data) {
	    this.data.put(key, data);
	  }

	  public  ResponseEntity<AGPResponse> response(HttpStatus status) {
	    return new ResponseEntity<>(this, status);
	  }

	  public static ResponseEntity<AGPResponse> success(String successMessage) {
	    return response(SUCCESS, successMessage, OK, APPLICATION_JSON_VALUE);
	  }

	  public ResponseEntity<AGPResponse> successEntity(String successMessage) {
	    addData(SUCCESS, successMessage);
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
	    headers.add("Content-Type", APPLICATION_JSON_VALUE);
	    return new ResponseEntity<>(this, headers, OK);
	  }

	  public static ResponseEntity<AGPResponse> success(String successMessage, String contentType) {
	    return response(SUCCESS, successMessage, OK, contentType);
	  }


	  public static ResponseEntity<AGPResponse> error(String errorMessage, HttpStatus statusCode) {
	    return error(errorMessage, statusCode, APPLICATION_JSON_VALUE);
	  }

	  public ResponseEntity<AGPResponse> errorEntity(String errorMessage, HttpStatus statusCode) {
	    addData(ERROR, errorMessage);
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
	    headers.add("Content-Type", APPLICATION_JSON_VALUE);
	    return new ResponseEntity<>(this, headers, statusCode);
	  }

	  public static ResponseEntity<AGPResponse> error(String errorMessage, HttpStatus statusCode, String contentType) {
	    return response(ERROR, errorMessage, statusCode, contentType);
	  }


	  public static ResponseEntity<AGPResponse> response(String key, String message, HttpStatus statusCode, String contentType) {
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
	    headers.add("Content-Type", contentType);
	    return new ResponseEntity<>(new AGPResponse(key, message), headers, statusCode);
	  }

	  public static ResponseEntity<Object> response(Object value) {
	    return new ResponseEntity<>(value, OK);
	  }

	  public static ResponseEntity<AGPResponse> response(String key, Object value) {
	    return new ResponseEntity<>(new AGPResponse(key, value), OK);
	  }

	  public static ResponseEntity<AGPResponse> response(String key, Object value, HttpStatus status) {
	    return new ResponseEntity<>(new AGPResponse(key, value), status);
	  }

	  public static ResponseEntity<AGPResponse> response(Map<String, String> messages, HttpStatus status) {
	    return response(messages, status, APPLICATION_JSON_VALUE);
	  }

	  public static ResponseEntity<AGPResponse> response(Map<String, String> messages, HttpStatus status, String contentType) {
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
	    headers.add("Content-Type", contentType);
	    AGPResponse response = new AGPResponse();
	    response.setData(messages);
	    return new ResponseEntity<>(response, headers, status);
	  }

	  @JsonAnyGetter
	  @SuppressWarnings("unused")
	  public Map<String, Object> getData() {
	    return data;
	  }

	  private void setData(Map<String, String> errors) {
	    for (String key : errors.keySet()) {
	      addData(key, errors.get(key));
	    }
	  }

	  @JsonIgnore
	  public String getErrorMsg() {
	    return (String) data.get(ERROR);
	  }

	  @JsonIgnore
	  public String getSuccessMsg() {
	    return (String) data.get(SUCCESS);
	  }
}
