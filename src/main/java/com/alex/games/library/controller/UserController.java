package com.alex.games.library.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.games.library.model.Game;
import com.alex.games.library.security.service.UserDetailsImpl;
import com.alex.games.library.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/library")
	public ResponseEntity<?> getAllLibraryGames() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
			Long userId = userDetailsImpl.getId();
			List<Game> library = userService.getGamesLibraryByUserId(userId);
			return ResponseEntity.ok(library);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/library")
	public ResponseEntity<?> addGameToLibrary(@Valid @RequestBody Long gameId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
			Long userId = userDetailsImpl.getId();
			userService.addGameToLibraryById(userId, gameId);
			return ResponseEntity.ok("Game successfully added to library! " );
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/library")
	public ResponseEntity<?> removeGameToLibrary(@Valid @RequestBody Long gameId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
			Long userId = userDetailsImpl.getId();
			userService.removeGameToLibraryById(userId, gameId);
			return ResponseEntity.ok("Game successfully removed to library! " );
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
