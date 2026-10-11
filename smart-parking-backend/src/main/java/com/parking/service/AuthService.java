package com.parking.service;

import com.parking.dto.LoginRequest;
import com.parking.dto.LoginResponse;
import com.parking.dto.RegisterRequest;

import com.parking.entity.User;

import com.parking.repository.UserRepository;

import com.parking.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.authenticationManager =
                authenticationManager;

        this.jwtService =
                jwtService;
    }

    // =====================================================
    // REGISTER
    // =====================================================

    public String register(
            RegisterRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Registration data is required"
            );
        }

        if (request.getUsername() == null ||
                request.getUsername().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Username is required"
            );
        }

        if (request.getPassword() == null ||
                request.getPassword().isEmpty()) {

            throw new IllegalArgumentException(
                    "Password is required"
            );
        }

        String username =
                request.getUsername()
                        .trim();

        String password =
                request.getPassword();

        // -------------------------------------------------
        // VALIDATE USERNAME
        // -------------------------------------------------

        if (username.length() < 3) {

            throw new IllegalArgumentException(
                    "Username must contain at least 3 characters"
            );
        }

        // -------------------------------------------------
        // VALIDATE PASSWORD
        // -------------------------------------------------

        if (password.length() < 6) {

            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters"
            );
        }

        // -------------------------------------------------
        // CHECK DUPLICATE USERNAME
        // -------------------------------------------------

        if (userRepository.existsByUsername(username)) {

            return "Username already exists";
        }

        // -------------------------------------------------
        // ENCRYPT PASSWORD
        // -------------------------------------------------

        String encodedPassword =
                passwordEncoder.encode(password);

        // -------------------------------------------------
        // CREATE USER
        // -------------------------------------------------

        User user =
                new User();

        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("USER");

        // -------------------------------------------------
        // SAVE USER
        // -------------------------------------------------

        userRepository.save(user);

        return "User registered successfully";
    }

    // =====================================================
    // LOGIN
    // =====================================================

    public LoginResponse login(
            LoginRequest request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Login data is required"
            );
        }

        if (request.getUsername() == null ||
                request.getUsername().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Username is required"
            );
        }

        if (request.getPassword() == null ||
                request.getPassword().isEmpty()) {

            throw new IllegalArgumentException(
                    "Password is required"
            );
        }

        String username =
                request.getUsername()
                        .trim();

        String password =
                request.getPassword();

        // -------------------------------------------------
        // AUTHENTICATE USERNAME + PASSWORD
        // -------------------------------------------------

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        // -------------------------------------------------
        // FIND AUTHENTICATED USER
        // -------------------------------------------------

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        // -------------------------------------------------
        // GENERATE JWT
        // -------------------------------------------------

        String token =
                jwtService.generateToken(
                        user.getUsername(),
                        user.getRole()
                );

        // -------------------------------------------------
        // RETURN LOGIN RESPONSE
        // -------------------------------------------------

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole(),
                "Login successful"
        );
    }
}
