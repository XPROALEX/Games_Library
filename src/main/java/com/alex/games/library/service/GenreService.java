package com.alex.games.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alex.games.library.model.Genre;
import com.alex.games.library.repository.GenreRepository;

@Service
public class GenreService {

	@Autowired
	GenreRepository genreRepository;

	public Optional<Genre> getById(Integer id) {
		return genreRepository.findById(id);
	}

	public Optional<Genre> getByName(String name) {
		return genreRepository.findByName(name);
	}

	public List<Genre> getAll() {
		return genreRepository.findAll();
	}

	public Genre addGenre(String name) {
		Genre genre = new Genre(name);
		genreRepository.save(genre);
		return genre;
	}

	public Genre edit(Genre existinGenre, Genre editGenre) {
		if (editGenre.getName() != null)
			existinGenre.setName(editGenre.getName());

		genreRepository.save(existinGenre);
		return existinGenre;
	}

	public void deleteById(Integer id) {
		genreRepository.deleteById(id);
	}
}
