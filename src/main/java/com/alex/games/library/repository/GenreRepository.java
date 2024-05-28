package com.alex.games.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.games.library.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

	Optional<Genre> findByName(String name);

}
