package com.alex.games.library.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.games.library.model.Game;
import com.alex.games.library.model.Genre;

import jakarta.validation.Valid;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

	@Query("SELECT g FROM Game g WHERE g.name LIKE %:name%")
	List<Game> findByNameContaining(@Param("name") String name);

	Set<Game> findByGenresIn(List<Genre> genre);

	Optional<Game> findByName(@Valid String name);

}
