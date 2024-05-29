package com.alex.games.library.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alex.games.library.model.Game;
import com.alex.games.library.model.Genre;
import com.alex.games.library.payload.request.GameRequest;
import com.alex.games.library.repository.GameRepository;

import jakarta.validation.Valid;

@Service
@Transactional
public class GameService {

	@Autowired
	GameRepository gameRepository;
	@Autowired
	GenreService genreService;

	public Optional<Game> getById(Long id) {
		return gameRepository.findById(id);
	}

	public List<Game> getByNameContaining(String name) {
		return gameRepository.findByNameContaining(name);
	}

	public List<Game> getAll() {
		return gameRepository.findAll();
	}

	public List<Game> getByGenres(List<Integer> genreId) {
		List<Genre> genres = new ArrayList<Genre>();
		genreId.forEach(id -> genres.add(genreService.getById(id).get()));
		Set<Game> gamesSet = gameRepository.findByGenresIn(genres);

		return gamesSet.stream().sorted(Comparator.comparing(Game::getName)).collect(Collectors.toList());
	}

	public Game addGame(GameRequest gameRequest) {
		Game game = new Game();
		game.setName(gameRequest.getName());
		game.setDescription(gameRequest.getDescription());
		Set<Genre> genres = new HashSet<Genre>();
		gameRequest.getGenreId().forEach(genre -> genres.add(genreService.getById(genre).get()));
		game.setGenres(genres);

		gameRepository.save(game);
		return game;
	}

	public void deleteById(Long id) {
		gameRepository.deleteById(id);
	}

	public Game edit(Game existinGame, Game editGame) {
		if (editGame.getName() != null)
			existinGame.setName(editGame.getName());
		if (editGame.getDescription() != null)
			existinGame.setDescription(editGame.getDescription());
		if (editGame.getGenres() != null)
			existinGame.setGenres(editGame.getGenres());

		gameRepository.save(existinGame);
		return existinGame;
	}

	public Optional<Game> getByName(@Valid String name) {
		return gameRepository.findByName(name);
	}

}
