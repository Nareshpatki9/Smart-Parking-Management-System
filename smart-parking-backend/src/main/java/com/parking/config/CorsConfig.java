package com.parking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // =========================
        // ALLOWED FRONTEND ORIGINS
        // =========================

        configuration.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:5173",
                        "http://localhost:3000"
                )
        );

        // =========================
        // ALLOWED HTTP METHODS
        // =========================

        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        // =========================
        // ALLOWED HEADERS
        // =========================

        configuration.setAllowedHeaders(
                Arrays.asList("*")
        );

        // Required for authenticated requests
        configuration.setAllowCredentials(true);

        // =========================
        // APPLY TO ALL ENDPOINTS
        // =========================

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}