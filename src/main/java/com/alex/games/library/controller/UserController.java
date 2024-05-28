package com.alex.games.library.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.games.library.model.Game;
import com.alex.games.library.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/library/{id}")
	public ResponseEntity<?> getAllLibraryGames(@Valid @PathVariable Long id) {
		try {
			List<Game> library = userService.getGameByUserId(id);
			return ResponseEntity.ok(library);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/library/{id}")
	public ResponseEntity<?> addGameToLibrary(@Valid @PathVariable Long id, @RequestBody Long gameId) {
		try {
			userService.addGameToLibraryById(id, gameId);

		} catch (Exception e) {
		}
		return null;
	}

}
