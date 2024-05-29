package com.alex.games.library.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.alex.games.library.model.Token;
import com.alex.games.library.repository.TokenRepository;
import com.alex.games.library.repository.UserRepository;
import com.alex.games.library.security.jwt.JwtUtils;
import com.alex.games.library.security.service.UserDetailsImpl;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TokenService {

	@Value("${gamelibrary.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Autowired
	TokenRepository tokenRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtUtils jwtUtils;

	public Optional<Token> getByToken(String token) {
		return tokenRepository.findByToken(token);
	}

	public void createTokenEntity(UserDetailsImpl userDetailsImpl, ResponseCookie jwtCookie) {
		Token token = new Token();
		token.setUser(userRepository.findById(userDetailsImpl.getId()).get());
		token.setToken(jwtCookie.getValue());
		token.setExpiryDate(Instant.now().plusMillis(jwtExpirationMs));

		tokenRepository.save(token);
	}

	@Transactional
	public void deleteByUserId(Long userId) {
		tokenRepository.deleteByUser(userRepository.findById(userId).get());
	}

	@Transactional
	public void refreshToken(Token token, ResponseCookie jwtCookie) {
		Token newToken = new Token();

		newToken.setUser(token.getUser());
		newToken.setToken(jwtCookie.getValue());
		newToken.setExpiryDate(Instant.now().plusMillis(jwtExpirationMs));

		tokenRepository.delete(token);
		tokenRepository.save(newToken);
	}

}
