package com.alex.games.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.games.library.model.Token;
import com.alex.games.library.model.User;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token>findByToken(String token);

	void deleteByUser(User user);
	
//	int deleteByUser(User user);
}
