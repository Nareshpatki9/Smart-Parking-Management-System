package com.parking.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        // =========================
        // NO TOKEN
        // =========================

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // =========================
        // EXTRACT TOKEN
        // =========================

        String token =
                authorizationHeader.substring(7).trim();

        if (token.isEmpty()) {

            filterChain.doFilter(request, response);
            return;
        }

        try {

            // =========================
            // EXTRACT USERNAME
            // =========================

            String username =
                    jwtService.extractUsername(token);

            // =========================
            // CHECK AUTHENTICATION
            // =========================

            if (username != null
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                // =========================
                // LOAD USER
                // =========================

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(username);

                // =========================
                // VALIDATE TOKEN
                // =========================

                if (jwtService.isTokenValid(
                        token,
                        userDetails.getUsername())) {

                    // =========================
                    // EXTRACT ROLE
                    // =========================

                    String role =
                            jwtService.extractRole(token);

                    if (role == null
                            || role.trim().isEmpty()) {

                        throw new IllegalArgumentException(
                                "JWT role is missing"
                        );
                    }

                    role = role.trim().toUpperCase();

                    // =========================
                    // CREATE AUTHENTICATION
                    // =========================

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + role
                                            )
                                    )
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // =========================
                    // SET SECURITY CONTEXT
                    // =========================

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );
                }
            }

        } catch (Exception exception) {

            // Invalid / expired JWT
            SecurityContextHolder
                    .clearContext();

            System.out.println(
                    "JWT Authentication failed: "
                            + exception.getMessage()
            );
        }

        // Continue request
        filterChain.doFilter(
                request,
                response
        );
    }
}
