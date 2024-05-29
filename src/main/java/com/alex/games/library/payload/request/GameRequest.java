package com.alex.games.library.payload.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class GameRequest {

	@NotBlank
	private String name;

	@NotEmpty
	private List<Integer> genreId;

	@NotBlank
	private String description;

	public GameRequest() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getGenreId() {
		return genreId;
	}

	public void setGenreId(List<Integer> genreId) {
		this.genreId = genreId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
