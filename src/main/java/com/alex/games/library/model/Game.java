package com.alex.games.library.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="games", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "game_genre", 
	joinColumns = @JoinColumn(name = "game_id"),
	inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> genres= new HashSet<Genre>();

	private String description;

	public Game() {
		super();
	}

	public Game(String name, Set<Genre> genres, String description) {
		super();
		this.name = name;
		this.genres = genres;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
