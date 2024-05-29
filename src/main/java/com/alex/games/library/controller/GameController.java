package com.alex.games.library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.games.library.model.Game;
import com.alex.games.library.service.GameService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class GameController {

	@Autowired
	GameService gameService;

	@GetMapping("/games")
	public ResponseEntity<?> getAllGames() {
		try {
			List<Game> gamesList = gameService.getAll();
			return ResponseEntity.ok(gamesList);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/game/{id}")
	public ResponseEntity<?> getGameById(@Valid @PathVariable Long id) {
		try {
			Optional<Game> game = gameService.getById(id);
			return ResponseEntity.ok(game);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/game")
	public ResponseEntity<?> getGameByGenre(@Valid @RequestBody List<Integer> genresId) {
		try {
			List<Game> game = gameService.getByGenres(genresId);
			return ResponseEntity.ok(game);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

}
