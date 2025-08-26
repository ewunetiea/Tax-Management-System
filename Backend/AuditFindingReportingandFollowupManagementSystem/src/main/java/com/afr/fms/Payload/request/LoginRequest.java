package com.afr.fms.Payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	private String userAgent;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserAgent() {

		return userAgent;
	}

	public String setUserAgent() {
		return userAgent;
	}

}
