package com.alex.games.library.payload.response;

import java.util.ArrayList;
import java.util.List;

import com.alex.games.library.model.Game;

public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private List<String> roles = new ArrayList<String>();
	private List<Game> games = new ArrayList<Game>();

	public UserInfoResponse() {
		super();
	}

	public UserInfoResponse(Long id, String username, String email, List<String> roles, List<Game> games) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.games = games;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

}
