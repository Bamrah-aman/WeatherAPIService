package com.skiapi.weatherapiservice.controller;

import com.skiapi.weatherapiservice.service.GeoLocationService;
import com.skiapi.weatherapiservice.service.RealtimeWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/realtime")
@Slf4j
public class RealtimeController {

    @Autowired
    private GeoLocationService geoLocationService;

    @Autowired
    private RealtimeWeatherService realtimeWeatherService;

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {

    }
}
