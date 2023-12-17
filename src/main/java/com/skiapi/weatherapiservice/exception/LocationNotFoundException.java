package com.skiapi.weatherapiservice.exception;

import jakarta.validation.constraints.NotBlank;

public class LocationNotFoundException extends Exception {
    public LocationNotFoundException(@NotBlank String s) {
    }
}
