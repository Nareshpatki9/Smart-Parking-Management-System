package com.parking.exception;

public class VehicleNotAuthorizedException
        extends RuntimeException {

    public VehicleNotAuthorizedException(
            String message) {

        super(message);
    }
}
