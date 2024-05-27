package com.alex.games.library.controller;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.games.library.model.ERole;
import com.alex.games.library.model.Role;
import com.alex.games.library.model.Token;
import com.alex.games.library.model.User;
import com.alex.games.library.payload.request.LoginRequest;
import com.alex.games.library.payload.request.SignupRequest;
import com.alex.games.library.payload.response.MessageResponse;
import com.alex.games.library.payload.response.UserInfoResponse;
import com.alex.games.library.repository.RoleRepository;
import com.alex.games.library.repository.UserRepository;
import com.alex.games.library.security.jwt.JwtUtils;
import com.alex.games.library.security.service.UserDetailsImpl;
import com.alex.games.library.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetailsImpl);

		tokenService.createTokenEntity(userDetailsImpl, jwtCookie);

		List<String> roles = userDetailsImpl.getAuthorities().stream().map(role -> role.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new UserInfoResponse(
				userDetailsImpl.getId(), userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), roles));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(encoder.encode(signupRequest.getPassword()));

		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet();

		if (strRoles == null || strRoles.isEmpty()) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role moderatorRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(moderatorRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@SuppressWarnings("finally")
	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser() {
		try {
			UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			tokenService.deleteByUserId(user.getId());
		} catch (Exception e) {
			e.getMessage();
		} finally {
			ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
					.body(new MessageResponse("You've been signed out!"));
		}
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
		Optional<Token> token = tokenService.findByToken(jwtUtils.getJwtFromCookies(request));

		if (token.isPresent()) {
			if (token.get().getExpiryDate().compareTo(Instant.now()) < 0) {
				ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(token.get().getUser());

				tokenService.refreshToken(token.get(), jwtCookie);

				return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
						.body(new MessageResponse("Token is refreshed successfully!"));
			} else
				return ResponseEntity.ok().body("token is not expired");
		} else
			return ResponseEntity.badRequest().body(new MessageResponse("Token is not in database or is empty!"));
	}
}
