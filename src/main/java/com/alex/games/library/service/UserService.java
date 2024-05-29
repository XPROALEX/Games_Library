package com.alex.games.library.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alex.games.library.model.Game;
import com.alex.games.library.model.User;
import com.alex.games.library.payload.response.UserInfoResponse;
import com.alex.games.library.repository.UserRepository;

import jakarta.validation.Valid;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleService roleService;

	@Autowired
	GameService gameService;

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public Optional<User> getById(Long id) {
		return userRepository.findById(id);
	}

	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public User edit(User existinUser, User editUser) {
		if (editUser.getEmail() != null)
			existinUser.setEmail(editUser.getEmail());
		if (editUser.getUsername() != null)
			existinUser.setUsername(editUser.getUsername());
		if (editUser.getPassword() != null)
			existinUser.setPassword(editUser.getPassword());
		userRepository.save(existinUser);
		return existinUser;
	}

	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	public void delete(User user) {
		userRepository.delete(user);
	}

	public UserInfoResponse fromUserToUserInfoResponse(User user) {
		return new UserInfoResponse(user.getId(), user.getUsername(), user.getEmail(),
				roleService.fromSetToStringList(user.getRoles()),
				user.getGames().stream().sorted(Comparator.comparing(Game::getId)).collect(Collectors.toList()));
	}

	public List<Game> getGamesLibraryByUserId(Long id) {
		User user = userRepository.findById(id).get();
		Set<Game> games = user.getGames();
		// in questo caso ordinato per id ma potrei oridnarli per nome in in modo da
		// raggruppare le sage acnhe in momenti diversi di registrazione dei giochi
		List<Game> gamesList = games.stream().sorted(Comparator.comparing(Game::getId)).collect(Collectors.toList());
		return gamesList;
	}

	public void addGameToLibraryById(Long userId, Long gameId) {
		User user = userRepository.findById(userId).get();
		Game game = gameService.getById(gameId).get();
		user.getGames().add(game);
		userRepository.save(user);
	}

	public void removeGameToLibraryById(Long userId, @Valid Long gameId) {
		User user = userRepository.findById(userId).get();
		Game game = gameService.getById(gameId).get();
		user.getGames().remove(game);
		userRepository.save(user);
	}

}
