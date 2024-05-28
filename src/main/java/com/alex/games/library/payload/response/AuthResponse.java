package com.alex.games.library.payload.response;

public class AuthResponse {

	private String message;
	private Boolean responseStatus;
	private UserInfoResponse user;
	
	public AuthResponse() {
	super();
	}

	public AuthResponse(String message, Boolean responseStatus, UserInfoResponse user) {
		super();
		this.message = message;
		this.responseStatus = responseStatus;
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Boolean responseStatus) {
		this.responseStatus = responseStatus;
	}

	public UserInfoResponse getUser() {
		return user;
	}

	public void setUser(UserInfoResponse user) {
		this.user = user;
	}

	
}
