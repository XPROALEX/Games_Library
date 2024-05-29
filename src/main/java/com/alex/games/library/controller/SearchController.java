package com.alex.games.library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.games.library.model.Game;
import com.alex.games.library.service.GameService;
import com.alex.games.library.service.GenreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/search")
public class SearchController {
	@Autowired
	GameService gameService;

	@Autowired
	GenreService genreService;

	@GetMapping("/games/{name}")
	public ResponseEntity<?> searchGamesByName(@Valid @PathVariable String name) {
		List<Game> game = gameService.getByNameContaining(name);
		if (!game.isEmpty())
			return ResponseEntity.ok(game);
		else
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/game/{name}")
	public ResponseEntity<?> searchGameByName(@Valid @PathVariable String name) {
		Optional<Game> game = gameService.getByName(name);
		if (game.isPresent())
			return ResponseEntity.ok(game);
		else
			return ResponseEntity.notFound().build();
	}

}
