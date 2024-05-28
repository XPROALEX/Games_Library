package com.alex.games.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.games.library.model.Game;
import com.alex.games.library.model.Genre;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

	Optional<Game> findByName(String name);

	List<Game> findByGenresIn(List<Genre> genre);

}
