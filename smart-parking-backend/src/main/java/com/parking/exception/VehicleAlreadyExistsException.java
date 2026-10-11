package com.parking.exception;

public class VehicleAlreadyExistsException
        extends RuntimeException {

    public VehicleAlreadyExistsException(
            String message) {

        super(message);
    }
}