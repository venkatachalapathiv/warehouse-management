package com.company.wms.controller;

import io.jsonwebtoken.Claims;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.company.wms.security.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public record LoginRequest(@NotBlank String username, @NotBlank String password) {
	}

	public record TokenResponse(String accessToken, String refreshToken, String tokenType, long expiresIn) {
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));

		List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.filter(a -> a.startsWith("ROLE_")).map(a -> a.substring("ROLE_".length())).toList();

		List<String> scopes = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.filter(a -> a.startsWith("SCOPE_")).map(a -> a.substring("SCOPE_".length())).toList();

		String access = jwtService.generateAccessToken(auth.getName(), roles, scopes);
		String refresh = jwtService.generateRefreshToken(auth.getName());

		return ResponseEntity.ok(new TokenResponse(access, refresh, "Bearer", 900));
	}

	public record RefreshRequest(@NotBlank String refreshToken) {
	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest req) {
		Claims claims = jwtService.parse(req.refreshToken());
		if (!"refresh".equals(claims.get("typ"))) {
			return ResponseEntity.status(401).build();
		}

		String access = jwtService.generateAccessToken(claims.getSubject(), List.of("USER"), List.of("read"));
		String newRefresh = jwtService.generateRefreshToken(claims.getSubject());

		return ResponseEntity.ok(new TokenResponse(access, newRefresh, "Bearer", 900));
	}
}