package com.skiapi.weatherapiservice.service;

import com.skiapi.weatherapicommon.Entity.Locations;
import com.skiapi.weatherapicommon.Entity.RealtimeWeather;
import com.skiapi.weatherapiservice.exception.LocationNotFoundException;
import com.skiapi.weatherapiservice.repository.RealtimeWeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RealtimeWeatherService {

    @Autowired
    private RealtimeWeatherRepository repository;

    public RealtimeWeather getByLocation(Locations location) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        RealtimeWeather realtimeWeather = repository.findByCountryCodeAndCity(countryCode, cityName);
        if(realtimeWeather == null){
            throw  new LocationNotFoundException("No location found for country-code and city-name");
        }
        return realtimeWeather;
    }
}
