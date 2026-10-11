package com.parking.config;

import com.parking.repository.UserRepository;
import com.parking.security.JwtAuthenticationFilter;
import com.parking.security.JwtService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // =========================================================
    // USER DETAILS SERVICE
    // =========================================================

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> {

            var user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "User not found: " + username
                            )
                    );

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        };
    }

    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // =========================================================
    // AUTHENTICATION PROVIDER
    // =========================================================

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // =========================================================
    // AUTHENTICATION MANAGER
    // =========================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationProvider authenticationProvider) {

        return new ProviderManager(authenticationProvider);
    }

    // =========================================================
    // JWT FILTER
    // =========================================================

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {

        return new JwtAuthenticationFilter(
                jwtService,
                userDetailsService
        );
    }

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider)
            throws Exception {

        http

                // =================================================
                // CSRF
                // =================================================
                .csrf(csrf -> csrf.disable())

                // =================================================
                // CORS
                // =================================================
                .cors(cors -> {
                })

                // =================================================
                // SESSION MANAGEMENT
                // =================================================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =================================================
                // AUTHORIZATION RULES
                // =================================================
                .authorizeHttpRequests(auth -> auth

                        // -----------------------------------------
                        // PUBLIC AUTH APIs
                        // -----------------------------------------
                        .requestMatchers(
                                "/auth/**"
                        ).permitAll()

                        // -----------------------------------------
                        // ADMIN APIs
                        // -----------------------------------------
                        .requestMatchers(
                                "/admin/**"
                        ).hasRole("ADMIN")

                        // -----------------------------------------
                        // ADD PARKING SLOT - ADMIN ONLY
                        // -----------------------------------------
                        .requestMatchers(
                                "/parking/add"
                        ).hasRole("ADMIN")

                        // -----------------------------------------
                        // PARKING APIs
                        // -----------------------------------------
                        .requestMatchers(
                                "/parking/**"
                        ).authenticated()

                        // -----------------------------------------
                        // VEHICLE APIs
                        // -----------------------------------------
                        .requestMatchers(
                                "/vehicle/**"
                        ).authenticated()

                        // -----------------------------------------
                        // BILLING APIs
                        // -----------------------------------------
                        .requestMatchers(
                                "/billing/**"
                        ).authenticated()

                        // -----------------------------------------
                        // EVERYTHING ELSE
                        // -----------------------------------------
                        .anyRequest().authenticated()
                )

                // =================================================
                // AUTHENTICATION PROVIDER
                // =================================================
                .authenticationProvider(
                        authenticationProvider
                )

                // =================================================
                // JWT FILTER
                // =================================================
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}