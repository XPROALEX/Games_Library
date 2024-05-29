package com.alex.games.library.controller;

import java.util.List;
import java.util.NoSuchElementException;

import com.alex.games.library.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.games.library.model.Game;
import com.alex.games.library.model.User;
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
			User user = userService.findByUsername(userDetailsImpl.getUsername()).get();
			List<Game> library = userService.getGamesByUser(user);
			return ResponseEntity.ok(library);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/library")
	public ResponseEntity<?> addGameToLibrary(@Valid  @RequestBody Long gameId) {
		try {
			userService.addGameToLibraryById(2L,gameId);

		} catch (Exception e) {
		}
		return null;
	}

}
