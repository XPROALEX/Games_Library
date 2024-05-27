package com.alex.games.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.games.library.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
