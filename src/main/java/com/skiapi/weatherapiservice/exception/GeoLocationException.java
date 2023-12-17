package com.skiapi.weatherapiservice.exception;

public class GeoLocationException extends Exception {

    public GeoLocationException(String message) {
        super(message);
    }

    public GeoLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
