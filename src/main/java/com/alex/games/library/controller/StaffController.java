package com.alex.games.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.games.library.model.Game;
import com.alex.games.library.model.Genre;
import com.alex.games.library.payload.request.GameRequest;
import com.alex.games.library.service.GameService;
import com.alex.games.library.service.GenreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

	@Autowired
	GameService gameService;

	@Autowired
	GenreService genreService;

	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	@PostMapping("/game")
	public ResponseEntity<?> addGame(@Valid @RequestBody GameRequest gameRequest) {
		try {
			Game game = gameService.addGame(gameRequest);
			return ResponseEntity.ok(game);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	@PostMapping("/genre")
	public ResponseEntity<?> addGenre(@Valid @RequestBody String name) {
		try {
			Genre genre = genreService.addGenre(name);
			return ResponseEntity.ok(genre);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	

}
