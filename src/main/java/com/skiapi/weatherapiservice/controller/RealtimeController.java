package com.skiapi.weatherapiservice.controller;

import com.skiapi.weatherapicommon.DTO.RealtimeWeatherDTO;
import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapicommon.Entity.RealtimeWeather;
import com.skiapi.weatherapicommon.utility.CommonUtility;
import com.skiapi.weatherapiservice.exception.GeoLocationException;
import com.skiapi.weatherapiservice.exception.LocationNotFoundException;
import com.skiapi.weatherapiservice.service.GeoLocationService;
import com.skiapi.weatherapiservice.service.RealtimeWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/realtime")
@Slf4j
public class RealtimeController {

    @Autowired
    private GeoLocationService geoLocationService;

    @Autowired
    private RealtimeWeatherService realtimeWeatherService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String ip = CommonUtility.getIPAddress(request);
        try {
            Locations locationBehalfOfIP = geoLocationService.getLocations(ip);
            log.info("Successfully got the location fron geolocation service");
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationBehalfOfIP);
            log.info("Successfully got the realtimeWeather");
            RealtimeWeatherDTO realtimeWeatherDTO= modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
            return ResponseEntity.ok(realtimeWeatherDTO);
        }catch (GeoLocationException exception){
            log.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }catch (LocationNotFoundException exception){
            log.error(exception.getMessage(), exception);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
        try{
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
            RealtimeWeatherDTO dto = entity2DTO(realtimeWeather);
            return ResponseEntity.ok(dto);
        }catch (LocationNotFoundException locEx){
            log.error(locEx.getMessage(), locEx);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeatherData(@PathVariable("locationCode") String locationCode,
                                                       @RequestBody RealtimeWeather realtimeWeather) {
        try {
            RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);
            RealtimeWeatherDTO dto = entity2DTO(updatedRealtimeWeather);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
        return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
    }
}
