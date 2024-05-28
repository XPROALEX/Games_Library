package com.alex.games.library.controller;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
import com.alex.games.library.payload.response.AuthResponse;
import com.alex.games.library.payload.response.UserInfoResponse;
import com.alex.games.library.security.jwt.JwtUtils;
import com.alex.games.library.security.service.UserDetailsImpl;
import com.alex.games.library.service.RoleService;
import com.alex.games.library.service.TokenService;
import com.alex.games.library.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

			ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetailsImpl);

			tokenService.createTokenEntity(userDetailsImpl, jwtCookie);

			User user = userService.findByUsername(userDetailsImpl.getUsername()).get();

			UserInfoResponse userInfoResponse = userService.fromUserToUserInfoResponse(user);

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
					.body(new AuthResponse("Welcome ", true, userInfoResponse));
		} catch (Exception e) {
			return ResponseEntity.ok(new AuthResponse("User or password not correct", false, null));
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (userService.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity.ok(new AuthResponse("Error: Username is already taken!", false, null));
		}

		if (userService.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.ok(new AuthResponse("Error: Email is already in use!", false, null));
		}

		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(encoder.encode(signupRequest.getPassword()));

		Set<String> strRoles = signupRequest.getRole();

		Set<Role> roles = new HashSet<Role>();

		if (strRoles == null || strRoles.isEmpty()) {
			Role userRole = roleService.getByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleService.getByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role moderatorRole = roleService.getByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(moderatorRole);

					break;
				default:
					Role userRole = roleService.getByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userService.save(user);

		UserInfoResponse userInfoResponse = userService.fromUserToUserInfoResponse(user);

		return ResponseEntity.ok(new AuthResponse("User registered successfully! ", true, userInfoResponse));
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

					.body(new AuthResponse("You've been signed out!", true, null));
		}
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
		Optional<Token> token = tokenService.getByToken(jwtUtils.getJwtFromCookies(request));

		if (token.isPresent()) {
			if (token.get().getExpiryDate().compareTo(Instant.now()) < 0) {

				User user = token.get().getUser();

				ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

				tokenService.refreshToken(token.get(), jwtCookie);

				UserInfoResponse userInfoResponse = userService.fromUserToUserInfoResponse(user);

				return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
						.body(new AuthResponse("Token is refreshed successfully!", true, userInfoResponse));
			} else
				return ResponseEntity.ok().body("token is not expired");
			// debug possibile rimozione di testo
		} else
			return ResponseEntity.ok(new AuthResponse("Token is not in database or is empty!", false, null));
	}
}
