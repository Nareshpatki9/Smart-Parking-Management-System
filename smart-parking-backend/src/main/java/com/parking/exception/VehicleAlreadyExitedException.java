package com.parking.exception;

public class VehicleAlreadyExitedException
        extends RuntimeException {

    public VehicleAlreadyExitedException(
            String message) {

        super(message);
    }
}