package com.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =====================================================
    // PARKING FULL
    // =====================================================

    @ExceptionHandler(ParkingFullException.class)
    public ResponseEntity<Map<String, Object>>
    handleParkingFull(
            ParkingFullException exception) {

        return createErrorResponse(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    // =====================================================
    // VEHICLE ALREADY EXISTS
    // =====================================================

    @ExceptionHandler(
            VehicleAlreadyExistsException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleVehicleAlreadyExists(
            VehicleAlreadyExistsException exception) {

        return createErrorResponse(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    // =====================================================
    // VEHICLE ALREADY EXITED
    // =====================================================

    @ExceptionHandler(
            VehicleAlreadyExitedException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleVehicleAlreadyExited(
            VehicleAlreadyExitedException exception) {

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    // =====================================================
    // VEHICLE NOT AUTHORIZED
    // =====================================================

    @ExceptionHandler(
            VehicleNotAuthorizedException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleVehicleNotAuthorized(
            VehicleNotAuthorizedException exception) {

        return createErrorResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage()
        );
    }

    // =====================================================
    // VALIDATION ERRORS
    // =====================================================

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> validationErrors =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );

        response.put(
                "error",
                "Validation Failed"
        );

        response.put(
                "messages",
                validationErrors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // =====================================================
    // ILLEGAL ARGUMENT
    // =====================================================

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleIllegalArgument(
            IllegalArgumentException exception) {

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    // =====================================================
    // GENERAL EXCEPTION
    // =====================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleGeneralException(
            Exception exception) {

        // Print actual error in Spring Boot console
        exception.printStackTrace();

        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong. Please try again."
        );
    }

    // =====================================================
    // COMMON ERROR RESPONSE
    // =====================================================

    private ResponseEntity<Map<String, Object>>
    createErrorResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                status.value()
        );

        response.put(
                "error",
                status.getReasonPhrase()
        );

        response.put(
                "message",
                message
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
